package common.bean;

public class Rooms {
	private String code;
	private String ip_address;
	private String name;
	private String desciption;
	
	public Rooms () {
		
	}

	public Rooms(String code, String name, String desciption, String ip_address) {
		super();
		this.code = code;
		this.name = name;
		this.desciption = desciption;
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

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}
}
