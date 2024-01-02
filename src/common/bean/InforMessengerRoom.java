package common.bean;

import java.sql.Timestamp;

public class InforMessengerRoom {
	private int id;
	private String picture;
	private String name;
	private Timestamp createdAt;
	private String message;
	private String file;

	public InforMessengerRoom() {

	}

	public InforMessengerRoom(int id, String picture, String name, Timestamp createdAt, String message, String file) {
		super();
		this.id = id;
		this.picture = picture;
		this.name = name;
		this.createdAt = createdAt;
		this.message = message;
		this.file = file;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
