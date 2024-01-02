package common.bean;

import java.sql.Date;

public class InforUser {
	private String name;
	private String sex;
	private String phone;
	private Date birthday;
	private String country;

	public InforUser() {

	}

	public InforUser(String name, String sex, String phone, Date birthday, String country) {
		super();
		this.name = name;
		this.sex = sex;
		this.phone = phone;
		this.birthday = birthday;
		this.country = country;
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
