package client.bean;

import java.sql.Date;
import java.text.DecimalFormat;

import javax.swing.JButton;


public class ModelRoom {
	private String code;
	private String name;
	private String user_host;
	private String ip_address;
	private Date joined;
	private JButton accessButton;
	
	public ModelRoom() {
		
	}

	public ModelRoom(String code, String name, String user_host, String ip_address, Date joined) {
		super();
		this.code = code;
		this.name = name;
		this.user_host = user_host;
		this.ip_address = ip_address;
		this.joined = joined;
		this.accessButton = new JButton("Truy cập");
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser_host() {
		return user_host;
	}

	public void setUser_host(String user_host) {
		this.user_host = user_host;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public JButton getAccessButton() {
		return accessButton;
	}

	public void setAccessButton(JButton accessButton) {
		this.accessButton = accessButton;
	}
}

