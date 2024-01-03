package host.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.bean.InforMessengerRoom;
import common.bean.Rooms;
import common.dao.UserDAO;
import connection.DatabaseConnection;
import host.bean.PortIPClientMirror;

public class HRoomDAO {
	private Connection con = null;

	public HRoomDAO() {
		con = DatabaseConnection.getInstance().getConnection();
	}
	
	public void creatOneRoom(Rooms room, int idUser) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"INSERT INTO rooms (code, name, description, ip_address) VALUES (?, ?, ?, ?)",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setString(1, room.getCode());
		p.setString(2, room.getName());
		p.setString(3, room.getDesciption());
		p.setString(4, "");
		p.execute();
		ResultSet r = p.getGeneratedKeys();
		r.next();
		int roomID = r.getInt(1);
		p = con.prepareStatement("INSERT INTO rooms_hosts (room_id, host_id) VALUES (?, ?)",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setInt(1, roomID);
		p.setInt(2, idUser);
		r.next();
		p.execute();
		r.close();
		p.close();
	}
	
	public int postShare(String mess, int userId, int roomId) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"INSERT INTO shares_commons (messenger, user_id, room_id) VALUES (?, ?, ?)",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setString(1, mess);
		p.setInt(2, userId);
		p.setInt(3, roomId);
		p.execute();
		ResultSet r = p.getGeneratedKeys();
		r.next();
		int messId = r.getInt(1);
		r.close();
		p.close();
		return messId;
	}
	
	public void addNotificationPostShare(int idRoom, int messId) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"INSERT INTO notification_room (notification, room_id_noti, share_common_id) VALUES ('Bạn có thông báo mới từ nhóm', ?, ?)",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setInt(1, idRoom);
		p.setInt(2, messId);
		p.execute();
		p.close();
	}
	
	public InforMessengerRoom getMessageById(int messId) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT shares_commons.id, shares_commons.messenger, shares_commons.file, "
				+ "shares_commons.created_at, users.picture, users.name "
				+ "FROM shares_commons INNER JOIN "
				+ "rooms ON shares_commons.room_id = rooms.id "
				+ "INNER JOIN users ON shares_commons.user_id = users.id "
				+ "WHERE shares_commons.id = ?");
		p.setInt(1, messId);
		ResultSet r = p.executeQuery();
		InforMessengerRoom infor = new InforMessengerRoom();
		while (r.next()) {
			infor.setId(r.getInt("id"));
			infor.setMessage(r.getString("messenger"));
			infor.setFile(r.getString("file"));
			infor.setCreatedAt(r.getTimestamp("created_at"));
			infor.setPicture(r.getString("picture"));
			infor.setName(r.getString("name"));
		}
		return infor;
	}
	
	public List<PortIPClientMirror> getAllPortIPClinetOnline(String codeRoom, List<String> listCodeUser) throws SQLException  {
		String sqlLstCodeUser = "";
		for(String codeUser : listCodeUser) {
			sqlLstCodeUser += "'" + codeUser + "'" + ",";
		}
		if (sqlLstCodeUser.endsWith(",")) {
            sqlLstCodeUser = sqlLstCodeUser.substring(0, sqlLstCodeUser.length() - 1);
        }
		String sql = "SELECT users.port, users.ip_address FROM users "
				+ "INNER JOIN members_rooms ON users.id = members_rooms.member_id "
				+ "INNER JOIN rooms ON members_rooms.room_id = rooms.id "
				+ "WHERE rooms.code = ? AND users.code IN (" + sqlLstCodeUser + ")";
		PreparedStatement p = con.prepareStatement(sql);
		p.setString(1, codeRoom);
		ResultSet r = p.executeQuery();
		List<PortIPClientMirror> lstPortIP = new ArrayList<>();
		while(r.next()) {
			PortIPClientMirror infor = new PortIPClientMirror();
			infor.setPort(r.getString("port"));
			infor.setIp_address(r.getString("ip_address"));
			lstPortIP.add(infor);
		}
		return lstPortIP;
	}
	
	public int removeMessageInRoom(int id) throws SQLException {
		String sql1 = "DELETE FROM notification_room WHERE share_common_id = ?";
		PreparedStatement p = con.prepareStatement(sql1);
		p.setInt(1, id);
		int rowsAffected = p.executeUpdate();
		if (rowsAffected > 0) {
		    String sql2 = "DELETE FROM shares_commons WHERE id = ?";
		    PreparedStatement p1 = con.prepareStatement(sql2);
		    p1.setInt(1, id);
		    p1.executeUpdate();
		}
		return 1;
	}
	
	public int removeMemberInRoom(int roomID, int memberID) throws SQLException {
		String sql1 = "DELETE FROM notification_room WHERE room_id_noti = ? AND member_room_id IN (SELECT id FROM members_rooms WHERE member_id = ? AND room_id = ?)";
		PreparedStatement p = con.prepareStatement(sql1);
		p.setInt(1, roomID);
		p.setInt(2, memberID);
		p.setInt(3, roomID);
		int deletedRows1 = p.executeUpdate();
		if (deletedRows1 > 0) {
			String sql2 = "DELETE FROM members_rooms WHERE room_id = ? AND member_id = ?";
			PreparedStatement p1 = con.prepareStatement(sql2);
			p1.setInt(1, roomID);
			p1.setInt(2, memberID);
			p1.executeUpdate();
		}
		return deletedRows1;
	}
	
	public List<Integer> converCodeToIDMember(List<String> codeMembers, int roomId) throws SQLException {
		UserDAO userDAO = new UserDAO();
		List<Integer> idMembers = new ArrayList<>(); 
		for (String codeMember : codeMembers) {
			idMembers.add(userDAO.getIdUserByCode(codeMember));
		}
		return addMembersRoom(idMembers, roomId);
	}
	
	public List<Integer> addMembersRoom(List<Integer> idMembers, int roomId) throws SQLException {
		List<Integer> listID = new ArrayList<Integer>();
		for (int idMember : idMembers) {
			String sql = "INSERT INTO members_rooms (room_id, member_id) VALUES (?, ?)";
			PreparedStatement p = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			p.setInt(1, roomId);
			p.setInt(2, idMember);
			p.execute();
			ResultSet r = p.getGeneratedKeys();
			r.next();
			int memberRoomId = r.getInt(1);
			String sql1 = "INSERT INTO notification_room (notification, room_id_noti, member_room_id) VALUES ('Bạn được thêm vào nhóm', ?, ?)";
			PreparedStatement p1 = con.prepareStatement(sql1);
			p1.setInt(1, roomId);
			p1.setInt(2, memberRoomId);
			p1.execute();
			listID.add(memberRoomId);
		}
		return listID;
	}
	
	public List<PortIPClientMirror> checkOnlineMember(List<String> codeMembersOnline) throws SQLException {
		String sqlLstCodeMember = "";
		for(String codeMember : codeMembersOnline) {
			sqlLstCodeMember += "'" + codeMember + "'" + ",";
		}
		if (sqlLstCodeMember.endsWith(",")) {
			sqlLstCodeMember = sqlLstCodeMember.substring(0, sqlLstCodeMember.length() - 1);
        }
		String sql = "SELECT users.port, users.ip_address FROM users where users.code IN (" + sqlLstCodeMember + ")";
		PreparedStatement p = con.prepareStatement(sql);
		ResultSet r = p.executeQuery();
		List<PortIPClientMirror> lstPortIP = new ArrayList<>();
		while(r.next()) {
			PortIPClientMirror infor = new PortIPClientMirror();
			infor.setPort(r.getString("port"));
			infor.setIp_address(r.getString("ip_address"));
			lstPortIP.add(infor);
		}
		return lstPortIP;
	}
}
