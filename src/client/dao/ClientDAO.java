package client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import client.bean.ModelNotification;
import client.bean.ModelRoom;
import connection.DatabaseConnection;

public class ClientDAO {
	private Connection con = null;

	public ClientDAO() {
		con = DatabaseConnection.getInstance().getConnection();
	}
	
	public List<ModelRoom> getAllRoomByClient(String code) throws SQLException {
		List<ModelRoom> listRoom = new ArrayList<ModelRoom>();
		PreparedStatement p = con.prepareStatement(
				"SELECT rooms.code, rooms.name,(SELECT users.name as user_host FROM users WHERE id = rooms_hosts.host_id), \r\n"
						+ "rooms.ip_address, DATE(members_rooms.created_at) as joined \r\n"
						+ "FROM rooms INNER JOIN rooms_hosts ON\r\n"
						+ "rooms.id = rooms_hosts.room_id INNER JOIN members_rooms ON\r\n"
						+ "rooms.id = members_rooms.room_id INNER JOIN users ON\r\n"
						+ "members_rooms.member_id = users.id\r\n" + "WHERE users.code = ?");
		p.setString(1, code);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			ModelRoom room = new ModelRoom();
			room.setCode(r.getString("code"));
			room.setName(r.getString("name"));
			room.setUser_host(r.getString("user_host"));
			room.setIp_address(r.getString("ip_address"));
			room.setJoined(r.getDate("joined"));
			listRoom.add(room);
		}
		return listRoom;
	}
	
	public List<ModelNotification> getAllNotification(int idUser) throws SQLException {
		List<ModelNotification> listNotification = new ArrayList<ModelNotification>();
		PreparedStatement p = con.prepareStatement("select notification_room.notification, \r\n"
				+ "TO_CHAR(notification_room.created_at, 'YYYY-MM-DD HH24:MI:SS') AS created_at,\r\n"
				+ "rooms.name from notification_room inner join rooms\r\n"
				+ "on notification_room.room_id_noti = rooms.id\r\n"
				+ "WHERE rooms.id IN (SELECT room_id FROM members_rooms WHERE member_id = ? GROUP BY room_id)\r\n"
				+ "order by created_at DESC");
		p.setInt(1, idUser);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			ModelNotification modelNotification = new ModelNotification();
			modelNotification.setNotification(r.getString("notification"));
			modelNotification.setCreatedAt(r.getString("created_at"));
			modelNotification.setNameRoom(r.getString("name"));
			listNotification.add(modelNotification);
		}
		return listNotification;
	}
}
