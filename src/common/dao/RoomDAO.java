package common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.bean.InforMembersRoom;
import common.bean.InforMessengerRoom;
import common.bean.Rooms;
import connection.DatabaseConnection;

public class RoomDAO {
	private Connection con = null;

	public RoomDAO() {
		con = DatabaseConnection.getInstance().getConnection();
	}
	
	public List<Rooms> getHostRoomByCodeUser(int idUser) throws SQLException {
		List<Rooms> listRoom = new ArrayList<Rooms>();
		PreparedStatement p = con.prepareStatement(
				"select code, name, description, ip_address from rooms inner join rooms_hosts "
				+ "on rooms.id = rooms_hosts.room_id where rooms_hosts.host_id = ? order by rooms.created_at desc");
		p.setInt(1, idUser);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			Rooms room = new Rooms();
			room.setCode(r.getString("code"));
			room.setName(r.getString("name"));
			room.setIp_address(r.getString("ip_address"));
			room.setDesciption(r.getString("description"));
			listRoom.add(room);
		}
		return listRoom;
	}
	
	public Rooms getRoomByCode(String code) throws SQLException {
		Rooms room = new Rooms();
		PreparedStatement p = con.prepareStatement("SELECT code, name, description, ip_address FROM rooms WHERE code = ?");
		p.setString(1, code);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			room.setCode(r.getString("code"));
			room.setName(r.getString("name"));
			room.setIp_address(r.getString("ip_address"));
			room.setDesciption(r.getString("description"));
		}
		return room;
	}
	
	public List<InforMessengerRoom> getAllMessagesRoom(String codeRoom) throws SQLException {
		List<InforMessengerRoom> listMessage = new ArrayList<>();
		PreparedStatement p = con.prepareStatement(
				"SELECT shares_commons.id, shares_commons.messenger, shares_commons.file, "
				+ "shares_commons.created_at, users.picture, users.name "
				+ "FROM shares_commons INNER JOIN "
				+ "rooms ON shares_commons.room_id = rooms.id "
				+ "INNER JOIN users ON shares_commons.user_id = users.id "
				+ "WHERE shares_commons.room_id IN ( "
				+ "SELECT id FROM rooms WHERE code = ?)");
		p.setString(1, codeRoom);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			InforMessengerRoom infor = new InforMessengerRoom();
			infor.setId(r.getInt("id"));
			infor.setMessage(r.getString("messenger"));
			infor.setFile(r.getString("file"));
			infor.setCreatedAt(r.getTimestamp("created_at"));
			infor.setPicture(r.getString("picture"));
			infor.setName(r.getString("name"));
			listMessage.add(infor);
		}
		return listMessage;
	}
	
	public List<InforMembersRoom> getAllMembersRoom(String codeRoom, List<String> listCodeUser) throws SQLException {
		List<InforMembersRoom> listMember = new ArrayList<>();
		PreparedStatement p = con.prepareStatement(
				"select users.code, users.email, users_infors.name, users_infors.sex, "
				+ "users_infors.phone, users_infors.birthday, users_infors.country from rooms "
				+ "inner join members_rooms on rooms.id = members_rooms.room_id "
				+ "inner join users on members_rooms.member_id = users.id "
				+ "inner join users_infors on users.id = users_infors.user_id "
				+ "where rooms.code = ? order by users.name asc");
		p.setString(1, codeRoom);
		ResultSet r = p.executeQuery();
		while(r.next()) {
			InforMembersRoom infor = new InforMembersRoom();
			if (listCodeUser.contains(r.getString("code"))) {
				infor.setStatus("Online");
			} else {
				infor.setStatus("Offline");
			}
			infor.setCode(r.getString("code"));
			infor.setEmail(r.getString("email"));
			infor.setName(r.getString("name"));
			infor.setSex(r.getString("sex"));
			infor.setPhone(r.getString("phone"));
			infor.setBirthday(r.getDate("birthday"));
			infor.setCountry(r.getString("country"));
			listMember.add(infor);
		}
		return listMember;
	}
	
	public int getIdByCodeRoom(String code) throws SQLException {
		String sql = "SELECT ID FROM rooms WHERE code = ?";
		PreparedStatement p = con.prepareStatement(sql);
		p.setString(1, code);
		ResultSet r = p.executeQuery();
		int id = 0;
		while(r.next()) {
			id = r.getInt("id");
		}
		return id;
	}
	
	public String getNameRoomByCode(String codeRoom) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"SELECT name FROM rooms WHERE code = ?");
		p.setString(1, codeRoom);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			return r.getString("name");
		}
		return null;
	}
}
