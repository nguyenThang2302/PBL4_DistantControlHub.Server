package common.bean;

import java.sql.Date;

public class InforMembersRoom {
	private String code;
	private String email;
	private String name;
	private String sex;
	private String phone;
	private Date birthday;
	private String country;
	private String status;
	
	public InforMembersRoom() {
		
	}

	public InforMembersRoom(String code, String email, String name, String sex, String phone, Date birthday,
			String country, String status) {
		super();
		this.code = code;
		this.email = email;
		this.name = name;
		this.sex = sex;
		this.phone = phone;
		this.birthday = birthday;
		this.country = country;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
