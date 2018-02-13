package net.vivec.pennybot.command;

import net.dv8tion.jda.core.entities.Message;

public class Command {

	private Message message;
	private CommandType type;
	
	public Command(Message message) {
		this.setMessage(message);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}

	public enum CommandType {
		SYSTEM, UTILITY, CUSTOM;
	}
}
