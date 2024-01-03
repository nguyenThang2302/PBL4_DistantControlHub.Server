package client.bean;

public class ModelNotification {
	private String notification;
	private String createdAt;
	private String nameRoom;
	
	public ModelNotification() {
		
	}

	public ModelNotification(String notification, String createdAt, String nameRoom) {
		super();
		this.notification = notification;
		this.createdAt = createdAt;
		this.nameRoom = nameRoom;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getNameRoom() {
		return nameRoom;
	}

	public void setNameRoom(String nameRoom) {
		this.nameRoom = nameRoom;
	}
}
