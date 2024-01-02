package common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;


import common.bean.InforUser;
import common.bean.User;
import common.ultils.Ultils;
import connection.DatabaseConnection;

public class UserDAO {
	private Connection con = null;

	public UserDAO() {
		con = DatabaseConnection.getInstance().getConnection();
	}

	public User CheckAccount(User data) throws SQLException {
		if (isUserExists(data.getEmail())) {
			String pass = data.getPassword();
			User user = getUserByAcc(data.getEmail());
			if (user != null) {
				if (BCrypt.checkpw(pass, user.getPassword())) {
					return user;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public int AddAccount(User user) throws SQLException {
		if (isUserExists(user.getEmail()) || !user.getPassword().equals(user.getRepeat_password())) {
			return -1;
		} else {
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
			PreparedStatement p = con.prepareStatement(
					"insert into users (code, ip_address, name, email, password, picture ) values (?, ?, ?, ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			p.setString(1, user.getCode());
			p.setString(2, user.getIp_address());
			p.setString(3, user.getName());
			p.setString(4, user.getEmail());
			p.setString(5, user.getPassword());
			p.setString(6, user.getPicture());
			p.execute();
			ResultSet r = p.getGeneratedKeys();
			r.next();
			int userID = r.getInt(1);
			p = con.prepareStatement("insert into users_infors (user_id) values (?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			p.setInt(1, userID);
			r.next();
			p.execute();
			r.close();
			p.close();
			return 1;
		}
	}

	public void insertUser(User user) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"insert into users (code, ip_address, name, email, password, picture ) values (?, ?, ?, ?, ?, ?)",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setString(1, user.getCode());
		p.setString(2, user.getIp_address());
		p.setString(3, user.getName());
		p.setString(4, user.getEmail());
		p.setString(5, user.getPassword());
		p.setString(6, user.getPicture());
		p.execute();
		ResultSet r = p.getGeneratedKeys();
		r.next();
		int userID = r.getInt(1);
		p = con.prepareStatement("insert into users_infors (user_id) values (?)",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setInt(1, userID);
		r.next();
		p.execute();
		r.close();
		p.close();
	}

	public String generateVerifyCode() {
		DecimalFormat df = new DecimalFormat("000000");
		Random ran = new Random();
		String code = df.format(ran.nextInt(1000000));
		return code;
	}

	public boolean checkDuplicateEmail(String user) throws SQLException {
		boolean duplicate = false;
		PreparedStatement p = con.prepareStatement("SELECT email FROM users WHERE email = ? LIMIT 1");
		p.setString(1, user);
		ResultSet r = p.executeQuery();
		if (r.next()) {
			duplicate = true;
		}
		r.close();
		p.close();
		return duplicate;
	}

	public boolean isUserExists(String email) throws SQLException {
		boolean duplicate = false;
		PreparedStatement p = con.prepareStatement("SELECT email FROM users WHERE email = ? LIMIT 1");
		p.setString(1, email);
		ResultSet r = p.executeQuery();
		if (r.next()) {
			duplicate = true;
		}
		r.close();
		p.close();
		return duplicate;
	}
	
	public int getIdUserByCode(String code) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT id FROM users WHERE code = ?");
		p.setString(1, code);
		ResultSet r = p.executeQuery();
		if (r.next()) {
			return r.getInt("id");
		}
		return -1;
	}

	public User getUserByAcc(String email) throws SQLException {
		User user = null;
		PreparedStatement p = con.prepareStatement("SELECT * FROM users WHERE email = ?");
		p.setString(1, email);
		ResultSet r = p.executeQuery();
		if (r.next()) {
			user = new User();
			user.setCode(r.getString("code"));
			user.setName(r.getString("name"));
			user.setEmail(r.getString("email"));
			user.setPassword(r.getString("password"));
			user.setPicture(r.getString("picture"));
			user.setIp_address(r.getString("ip_address"));
			r.close();
		}
		p.close();
		return user;
	}
	
	public void updateIpAddress(String ip_address, String code) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"UPDATE users SET ip_address = ? WHERE code = ?",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setString(1, ip_address);
		p.setString(2, code);
		p.execute();
		p.close();
	}
	
	public void updateUser_passWord(String email, String newPassword) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"UPDATE users SET password =? WHERE email=?",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setString(1, newPassword);
		p.setString(2, email);
		p.execute();
		p.close();
	}
	
	public void updatePortUser(int port, String code) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"UPDATE users SET port = ? WHERE code = ?",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setInt(1, port);
		p.setString(2, code);
		p.execute();
		p.close();
	}
	
	public void updateStatusOnline(String code) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"UPDATE users SET status = 'online' WHERE code = ?",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setString(1, code);
		p.execute();
		p.close();
	}
	
	public void updateStatusOffline(String code) throws SQLException {
		PreparedStatement p = con.prepareStatement(
				"UPDATE users SET status = 'offline' WHERE code = ?",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p.setString(1, code);
		p.execute();
		p.close();
	}
	
	public User getUserByCodeOrEmail(String inforSearch) throws SQLException {
		User user = null;
		PreparedStatement p = con.prepareStatement("SELECT * FROM users WHERE email = ? OR email = ?");
		p.setString(1, inforSearch);
		p.setString(2, inforSearch);
		ResultSet r = p.executeQuery();
		if (r.next()) {
			user = new User();
			user.setCode(r.getString("code"));
			user.setName(r.getString("name"));
			user.setEmail(r.getString("email"));
			user.setPassword(r.getString("password"));
			user.setPicture(r.getString("picture"));
			user.setIp_address(r.getString("ip_address"));
			r.close();
		}
		p.close();
		return user;
	}
	
	public String getIPAddressUser(String code) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT ip_address FROM users WHERE code = ?");
		p.setString(1, code);
		ResultSet r = p.executeQuery();
		String ip = "";
		if (r.next()) {
			ip = r.getString("ip_address");
		}
		return ip;
	}
	
	public int getPortByUser(String code) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT port FROM users WHERE code = ?");
		p.setString(1, code);
		ResultSet r = p.executeQuery();
		int port = 0;
		if (r.next()) {
			port = r.getInt("port");
		}
		return port;
	}
	
	public InforUser getInforUserByEmail(String email) throws SQLException {
		PreparedStatement p = con.prepareStatement("select * from users_infors where user_id in ( "
				+ "select id from users where email = ? )");
		p.setString(1, email);
		ResultSet r = p.executeQuery();
		InforUser inforUser = null;
		if (r.next()) {
			inforUser = new InforUser();
			inforUser.setName(r.getString("name"));
			inforUser.setPhone(r.getString("phone"));
			inforUser.setSex(r.getString("sex"));
			inforUser.setBirthday(r.getDate("birthday"));
			inforUser.setCountry(r.getString("country"));
		}
		return inforUser;
	}
	
	public void updateProfileUser(String name, String sex, String email, String phone, String birthday, String country) throws SQLException, ParseException {
		Ultils ultilsService = new Ultils();
		PreparedStatement p1 = con.prepareStatement(
				"UPDATE users_infors SET name = ?, sex = ?, phone = ?, birthday = ?, country = ? WHERE user_id IN (SELECT id FROM users WHERE email = ?)",
				PreparedStatement.RETURN_GENERATED_KEYS);
		p1.setString(1, name);
		p1.setString(2, sex);
		p1.setString(3, phone);
		p1.setDate(4, ultilsService.convertStringToDate(birthday));
		p1.setString(5, country);
		p1.setString(6, email);
		p1.execute();
		p1.close();
	}
}
