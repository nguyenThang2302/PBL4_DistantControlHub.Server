package common.bean;

public class User {
	private String code;
	private String ip_address;
	private String name;
	private String email;
	private String password;
	private String picture;
	private String repeat_password;
	
	public User(){
		
	}
	
	public User(String name, String email, String password, String picture, String repeat_password, String code, String ip_address){
		this.name = name;
		this.email = email;
		this.password = password;
		this.repeat_password = repeat_password;
		this.code = code;
		this.picture = picture;
		this.ip_address = ip_address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getRepeat_password() {
		return repeat_password;
	}

	public void setRepeat_password(String repeat_password) {
		this.repeat_password = repeat_password;
	}
}
