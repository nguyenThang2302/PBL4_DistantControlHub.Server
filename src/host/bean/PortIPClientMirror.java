package host.bean;

public class PortIPClientMirror {
	private String port;
    private String ip_address;
    public  PortIPClientMirror() {

    }
    public PortIPClientMirror(String port, String ip_address) {
        this.port = port;
        this.ip_address = ip_address;
    }
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
}
