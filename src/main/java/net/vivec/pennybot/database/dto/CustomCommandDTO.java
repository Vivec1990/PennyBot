package net.vivec.pennybot.database.dto;

public class CustomCommandDTO {

	private String name;
	private String message;
	private byte[] image;
	private String mentions;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getMentions() {
		return mentions;
	}
	public void setMentions(String mentions) {
		this.mentions = mentions;
	}
	
}
