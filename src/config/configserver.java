package config;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class configserver {
	private final int port = 6996;
	private String ip = "";
	
	public static String getIPAddress() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        return localhost.getHostAddress();
    }

	public int getPort() {
		return port;
	}

	public String getIp() throws UnknownHostException {
		this.ip = getIPAddress();
		return ip;
	}
	
}
