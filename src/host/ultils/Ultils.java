package host.ultils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

import common.dao.UserDAO;
import host.bean.PortIPClientMirror;

public class Ultils {
	public String handelSendNotificationRoom(List<PortIPClientMirror> listPortIP, String codeRoom) {
		String response = "{\"status\":\"Send Request Post Infor Successfully\"}";
		Thread thread = new Thread(() -> {
			for (PortIPClientMirror PortIP : listPortIP) {
				try {
					Socket socket = new Socket(PortIP.getIp_address(), Integer.parseInt(PortIP.getPort()));
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					String request = "notificationRoom_" + codeRoom;
					writer.println(request);
					writer.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return response;
	}

	public String handelSendAddMember(List<PortIPClientMirror> listPortIP, String codeRoom) {
		String response = "{\"status\":\"Send Request Post Infor Successfully\"}";
		Thread thread = new Thread(() -> {
			for (PortIPClientMirror PortIP : listPortIP) {
				try {
					Socket socket = new Socket(PortIP.getIp_address(), Integer.parseInt(PortIP.getPort()));
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					String request = "notificationAddMember_" + codeRoom;
					writer.println(request);
					writer.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return response;
	}

	public String handelRemoteAction(String action, String codeMember, String codeRoom) throws SQLException {
		String response = "{\"status\":\"Action Handel Failed\"}";
		UserDAO userDAO = new UserDAO();
		String ipAddress = userDAO.getIPAddressUser(codeMember);
		int port = userDAO.getPortByUser(codeMember);
		try {
			Socket socket = new Socket(ipAddress, port);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			String request = "";
			switch (action) {
			case "shutdown":
				request = action + "_" + ipAddress;
				writer.println(request);
				writer.flush();
				response = "Action Handel Successfully";
				return response;
			case "restart":
				request = action + "_" + ipAddress;
				writer.println(request);
				writer.flush();
				response = "Action Handel Successfully";
				return response;
			case "lock":
				request = action + "_" + ipAddress;
				writer.println(request);
				writer.flush();
				response = "Action Handel Successfully";
				return response;
			default:
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public String handelMirrorAtion(String action, String codeMember, String ipAddress, int port,
			List<PortIPClientMirror> portIPClientMirrorList) {
		String response = "Action Handel Failed";
		try {
			Socket socket = new Socket(ipAddress, port);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			String request = "";
			switch (action) {
			case "mirror":
				request = action + "_" + ipAddress;
				writer.println(request);
				writer.flush();
				String portMirror = reader.readLine();
				String requestMirror = "mirror_" + ipAddress + "_" + portMirror;
				if (portIPClientMirrorList.size() > 0) {
					for (PortIPClientMirror portIP : portIPClientMirrorList) {
						Socket socketMirror = new Socket(portIP.getIp_address(), Integer.parseInt(portIP.getPort()));
						PrintWriter writerMirror = new PrintWriter(socketMirror.getOutputStream());
						writerMirror.println(requestMirror);
						writerMirror.flush();
					}
				}
				response = "Action Handel Successfully" + "_" + portMirror;
				return response;
			default:
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
