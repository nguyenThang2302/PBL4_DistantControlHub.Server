package server;

import java.io.BufferedReader;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

import common.bean.InforMembersRoom;
import common.bean.InforMessengerRoom;
import common.bean.InforUser;
import common.bean.ModelMessage;
import common.bean.Rooms;
import common.bean.User;
import common.dao.RoomDAO;
import common.dao.UserDAO;
import config.ServiceMail;
import config.configserver;
import host.bean.PortIPClientMirror;
import host.dao.HRoomDAO;
import host.ultils.Ultils;

public class Server {
	public static void main(String[] args) {
		try {
			connection.DatabaseConnection.getInstance().connectToDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ServerSocket serverSocket = null;
		configserver configServer = new configserver();
		List<String> listCodeUser = new ArrayList<String>();
		List<String> listCodeHost = new ArrayList<String>();
		try {
			serverSocket = new ServerSocket(configServer.getPort());
			config.Logger.logger("Server is starting on port 6996");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				config.Logger.logger("New client connected: " + clientSocket);
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
					String request = reader.readLine();
					String[] requestArr = request.split("_");
					if (requestArr[0].equals("user")) {
						listCodeUser.add(requestArr[1]);
					} else if (requestArr[0].equals("host")) {
						listCodeHost.add(requestArr[1]);
					} else if (requestArr[0].equals("login")) {
						User loginUser = new User();
						loginUser.setEmail(requestArr[1]);
						loginUser.setPassword(requestArr[2]);
						UserDAO userDAO = new UserDAO();
						User isLogin = new User();
						try {
							isLogin = userDAO.CheckAccount(loginUser);
							if (isLogin == null) {
								writer.println("{\"status\":\"Failed Login\"}");
								writer.flush();
							} else {
								config.Logger.logger("User " + isLogin.getCode() + " login successfully");
								userDAO.updateIpAddress(clientSocket.getInetAddress().toString().replace("/", ""),
										isLogin.getCode());
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("status", "Successfully Login");
								jsonObject.put("code", isLogin.getCode());
								jsonObject.put("name", isLogin.getName());
								jsonObject.put("email", isLogin.getEmail());
								jsonObject.put("picture", isLogin.getPicture());
								writer.println(jsonObject.toString());
								writer.flush();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (requestArr[0].equals("home")) {
						UserDAO userDAO = new UserDAO();
						RoomDAO roomDAO = new RoomDAO();
						try {
							config.Logger.logger("User " + requestArr[1] + " get all room");
							int idUser = userDAO.getIdUserByCode(requestArr[1]);
							List<Rooms> listRoom = new ArrayList<>();
							listRoom = roomDAO.getHostRoomByCodeUser(idUser);
							JSONArray jsonArray = new JSONArray();
							for (Rooms room : listRoom) {
								JSONObject roomJson = new JSONObject();
								roomJson.put("code", room.getCode());
								roomJson.put("name", room.getName());
								roomJson.put("description", room.getDesciption());
								jsonArray.put(roomJson);
							}
							writer.println(jsonArray.toString());
							writer.flush();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (requestArr[0].equals("detailroom")) {
						RoomDAO roomDAO = new RoomDAO();
						try {
							config.Logger.logger("User " + requestArr[1] + " get detail room " + requestArr[1]);
							Rooms detailRoom = roomDAO.getRoomByCode(requestArr[1]);

							JSONObject detailRoomJson = new JSONObject();
							detailRoomJson.put("code", detailRoom.getCode());
							detailRoomJson.put("name", detailRoom.getName());
							detailRoomJson.put("ip_address", detailRoom.getIp_address());
							detailRoomJson.put("description", detailRoom.getDesciption());

							List<InforMessengerRoom> listMess = roomDAO.getAllMessagesRoom(requestArr[1]);
							JSONArray jsonArrayMess = new JSONArray();
							for (InforMessengerRoom mess : listMess) {
								JSONObject messJson = new JSONObject();
								messJson.put("id", mess.getId());
								messJson.put("messenger", mess.getMessage());
								messJson.put("file", mess.getFile());
								messJson.put("created_at", mess.getCreatedAt());
								messJson.put("picture", mess.getPicture());
								messJson.put("name", mess.getName());
								jsonArrayMess.put(messJson);
							}

							List<InforMembersRoom> listMember = roomDAO.getAllMembersRoom(requestArr[1], listCodeUser);
							JSONArray jsonArrayMem = new JSONArray();
							for (InforMembersRoom infor : listMember) {
								JSONObject memJson = new JSONObject();
								memJson.put("code", infor.getCode());
								memJson.put("email", infor.getEmail());
								memJson.put("name", infor.getName());
								memJson.put("sex", infor.getSex());
								memJson.put("phone", infor.getPhone());
								memJson.put("birthday", infor.getBirthday());
								memJson.put("status", infor.getStatus());
								memJson.put("country", infor.getCountry());
								jsonArrayMem.put(memJson);
							}
							JSONObject jsonRes = new JSONObject();
							jsonRes.put("roomInfor", detailRoomJson);
							jsonRes.put("inforMessenger", jsonArrayMess);
							jsonRes.put("inforMemberRoom", jsonArrayMem);
							writer.println(jsonRes.toString());
							writer.flush();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (requestArr[0].equals("createroom")) {
						UserDAO userDAO = new UserDAO();
						HRoomDAO hroomDAO = new HRoomDAO();

						String name = requestArr[1];
						String description = requestArr[2];
						String userCode = requestArr[3];

						int userID = 0;
						try {
							userID = userDAO.getIdUserByCode(userCode);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						Random rd = new Random();
						String roomCode = System.currentTimeMillis() + rd.nextInt(1000) + "";
						config.Logger.logger("User " + userCode + " create room " + roomCode);

						Rooms insertRoom = new Rooms();
						insertRoom.setCode(roomCode);
						insertRoom.setName(name);
						insertRoom.setDesciption(description);

						try {
							hroomDAO.creatOneRoom(insertRoom, userID);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("status", "Create Room Successfully");
						writer.println(jsonObject.toString());
						writer.flush();
					} else if (requestArr[0].equals("postshare")) {
						RoomDAO roomDAO = new RoomDAO();
						UserDAO userDAO = new UserDAO();
						HRoomDAO hroomDAO = new HRoomDAO();
						Ultils ultils = new Ultils();

						String codeRoom = requestArr[1];
						String message = requestArr[2];
						String userCode = requestArr[3];

						int idRoom = 0;
						try {
							idRoom = roomDAO.getIdByCodeRoom(codeRoom);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						int userID = 0;
						try {
							userID = userDAO.getIdUserByCode(userCode);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						int messId = 0;
						try {
							messId = hroomDAO.postShare(message, userID, idRoom);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						config.Logger.logger("User " + userCode + " post share room " + codeRoom);

						try {
							hroomDAO.addNotificationPostShare(idRoom, messId);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						InforMessengerRoom inforMess = null;
						try {
							inforMess = hroomDAO.getMessageById(messId);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						if (listCodeUser.size() > 0) {
							List<PortIPClientMirror> listPortIP = new ArrayList<>();
							try {
								listPortIP = hroomDAO.getAllPortIPClinetOnline(codeRoom, listCodeUser);
								if (listPortIP.size() > 0) {
									ultils.handelSendNotificationRoom(listPortIP, codeRoom);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("id", inforMess.getId());
						jsonObject.put("messenger", inforMess.getMessage());
						jsonObject.put("file", inforMess.getFile());
						jsonObject.put("created_at", inforMess.getCreatedAt());
						jsonObject.put("picture", inforMess.getPicture());
						jsonObject.put("name", inforMess.getName());
						writer.println(jsonObject.toString());
						writer.flush();
					} else if (requestArr[0].equals("removepost")) {
						int id = Integer.parseInt(requestArr[1]);
						HRoomDAO hroomDAO = new HRoomDAO();
						try {
							hroomDAO.removeMessageInRoom(id);
							JSONObject jsonObject = new JSONObject();
							config.Logger.logger("Message " + id + " remove");
							jsonObject.put("status", "Remove Message In Room Successfully");
							writer.println(jsonObject.toString());
							writer.flush();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (requestArr[0].equals("searchmember")) {
						String iforSearch = requestArr[1];
						UserDAO userDAO = new UserDAO();
						User user = null;
						try {
							user = userDAO.getUserByCodeOrEmail(iforSearch);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("status", "Member has found");
						jsonObject.put("code", user.getCode());
						jsonObject.put("name", user.getName());
						jsonObject.put("email", user.getEmail());
						jsonObject.put("picture", user.getPicture());
						writer.println(jsonObject.toString());
						writer.flush();
					} else if (requestArr[0].equals("removemember")) {
						String codeMember = requestArr[1];
						String codeRoom = requestArr[2];

						UserDAO userDAO = new UserDAO();
						RoomDAO roomDAO = new RoomDAO();
						HRoomDAO hroomDAO = new HRoomDAO();

						int memberID = 0;
						try {
							memberID = userDAO.getIdUserByCode(codeMember);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						int roomID = 0;
						try {
							roomID = roomDAO.getIdByCodeRoom(codeRoom);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						try {
							hroomDAO.removeMemberInRoom(roomID, memberID);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("status", "Remove Member In Room Successfully");
							config.Logger.logger("User " + codeMember + " remove out room " + codeRoom);
							writer.println(jsonObject.toString());
							writer.flush();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (requestArr[0].equals("addmember")) {
						RoomDAO roomDAO = new RoomDAO();
						String codeRoom = requestArr[1];
						int roomID = 0;
						try {
							roomID = roomDAO.getIdByCodeRoom(codeRoom);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						String lstCodeMemberStr = requestArr[2];
						String[] listCodeMemberArr = lstCodeMemberStr.split("-");
						List<String> listCodeMember = new ArrayList<>();
						for (String codeMember : listCodeMemberArr) {
							listCodeMember.add(codeMember);
						}

						HRoomDAO hroomDAO = new HRoomDAO();
						try {
							hroomDAO.converCodeToIDMember(listCodeMember, roomID);
							JSONObject jsonObject = new JSONObject();
							config.Logger.logger("Add members " + lstCodeMemberStr + " in room " + codeRoom);
							jsonObject.put("status", "Add Members Successfully");
							writer.println(jsonObject.toString());
							writer.flush();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						List<String> codeMembersOnline = new ArrayList<>(listCodeMember);
						codeMembersOnline.retainAll(listCodeUser);

						if (codeMembersOnline.size() > 0) {
							List<PortIPClientMirror> listPortIP = new ArrayList<>();
							try {
								listPortIP = hroomDAO.checkOnlineMember(codeMembersOnline);
								if (listPortIP.size() > 0) {
									Ultils ultils = new Ultils();
									ultils.handelSendAddMember(listPortIP, codeRoom);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					} else if (requestArr[0].equals("remote")) {
						String action = requestArr[1];
						String codeMember = requestArr[2];
						String codeRoom = requestArr[3];

						Ultils ultils = new Ultils();
						try {
							String resHandelRemoteAction = ultils.handelRemoteAction(action, codeMember, codeRoom);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("status", resHandelRemoteAction);
							config.Logger.logger("Remote member " + codeMember + " in " + codeRoom);
							writer.println(jsonObject.toString());
							writer.flush();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (requestArr[0].equals("getipport")) {
						String codeMember = requestArr[1];

						UserDAO userDAO = new UserDAO();
						String ip = "";
						int port = 0;

						try {
							ip = userDAO.getIPAddressUser(codeMember);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						try {
							port = userDAO.getPortByUser(codeMember);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("ip", ip);
						jsonObject.put("port", port);
						config.Logger.logger("Get IPAddress and port of member " + codeMember);

						writer.println(jsonObject.toString());
						writer.flush();
					} else if (requestArr[0].equals("mirror")) {
						String action = requestArr[0];
						String codeRoom = requestArr[1];
						String codeMember = requestArr[2];

						UserDAO userDAO = new UserDAO();
						String ip = "";
						int port = 0;

						try {
							ip = userDAO.getIPAddressUser(codeMember);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						try {
							port = userDAO.getPortByUser(codeMember);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						HRoomDAO hroomDAO = new HRoomDAO();
						List<PortIPClientMirror> listPortIP = new ArrayList<>();
						try {
							listPortIP = hroomDAO.getAllPortIPClinetOnline(codeRoom, listCodeUser);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						Ultils ultils = new Ultils();
						String response = ultils.handelMirrorAtion(action, codeMember, ip, port, listPortIP);
						String[] responseArr = response.split("_");

						String status = responseArr[0];
						String portMirror = responseArr[1];

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("status", status);
						jsonObject.put("ip", ip);
						jsonObject.put("port", portMirror);
						config.Logger.logger("Mirror user " + codeMember + " in room " + codeRoom);
						writer.println(jsonObject.toString());
						writer.flush();
					} else if (requestArr[0].equals("getprofile")) {
						String email = requestArr[1];
						
						UserDAO userDAO = new UserDAO();
						InforUser inforUser = null;
						
						try {
							inforUser = userDAO.getInforUserByEmail(email);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", inforUser.getName());
						jsonObject.put("sex", inforUser.getSex());
						jsonObject.put("phone", inforUser.getPhone());
						jsonObject.put("birthday", inforUser.getBirthday());
						jsonObject.put("country", inforUser.getCountry());
						config.Logger.logger("Get profile of user " + email);
						writer.println(jsonObject.toString());
						writer.flush();
					} else if (requestArr[0].equals("updateprofile")) {
						String email = requestArr[1];
						String name = requestArr[2];
						String phone = requestArr[3];
						String sex = requestArr[4];
						String country = requestArr[5];
						String birthday = requestArr[6];
						
						UserDAO userDAO = new UserDAO();
						try {
							userDAO.updateProfileUser(name, sex, email, phone, birthday, country);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("status", "Update profile user successfully");
							config.Logger.logger("Update profile user " + email);
							
							writer.println(jsonObject.toString());
							writer.flush();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
					} else if (requestArr[0].equals("verifycode")) {
						String email = requestArr[1];
						
						UserDAO userDAO = new UserDAO();
						try {
							if (userDAO.isUserExists(email)) {
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("status", "Email already exists");
								writer.println(jsonObject.toString());
								writer.flush();
							} else {
								common.ultils.Ultils ultils = new common.ultils.Ultils();
								String code = ultils.generateVerifyCode();
								ModelMessage ms = new ServiceMail().sendMain(email, code);
								if (ms.isSuccess()) {
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("status", "Check email get verifycode");
									jsonObject.put("verifycode", code);
									config.Logger.logger("Send verify code to member " + email);
									writer.println(jsonObject.toString());
									writer.flush();
								} else {
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("status", "Email is not existing");
									writer.println(jsonObject.toString());
									writer.flush();
								}
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (requestArr[0].equals("registeruser")) {
						String email = requestArr[1];
						String password = requestArr[2];
						String repeatPassword = requestArr[3];
						
						Random rd = new Random();
						User user = new User();
						String user_code = System.currentTimeMillis() + rd.nextInt(1000) + "";
						user.setCode(user_code);
						user.setEmail(email);
						user.setPassword(password);
						user.setRepeat_password(repeatPassword);
						
						int check_pass = 0;
						UserDAO userDAO = new UserDAO();
						
						if (user.getPassword().equals(user.getRepeat_password())) {
							check_pass = 1;
						}
						user.setPicture("https://static.vecteezy.com/system/resources/previews/000/439/863/original/vector-users-icon.jpg");
						int count = 0;
						try {
							count = userDAO.AddAccount(user);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						JSONObject jsonObject = new JSONObject();
						if (count > 0 && check_pass == 1) {
							jsonObject.put("status", "Account Register Successfully");
							config.Logger.logger("Register user " + user_code);
							writer.println(jsonObject.toString());
							writer.flush();
						} else if (check_pass == 0) {
							jsonObject.put("status", "Error Password Confirm");
							writer.println(jsonObject.toString());
							writer.flush();
						} else {
							jsonObject.put("status", "Email already exists");
							writer.println(jsonObject.toString());
							writer.flush();
						}
					}
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
