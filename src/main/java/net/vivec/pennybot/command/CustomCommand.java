package net.vivec.pennybot.command;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.entities.User;
import net.vivec.pennybot.database.DatabaseQueryHandler;

public class CustomCommand extends Command {
	
	private byte[] image;
	private String message;
	private List<User> mentions;
	
	@Override
	protected void execute(CommandEvent event) {
		this.execute(new CommandEventEx(event));
	}
	
	private void execute(CommandEventEx event) {
		if(checkBans(event.getAuthor().getId())) {
			event.reply("You are not allowed to use this command");
			return;
		}
		String filename = null;
		if(this.image != null && this.image.length > 0) {
			filename = this.getName() + this.getContentExtension(this.image);
		}
		event.reply(this.message, this.image, filename, this.mentions);
	}
	
	private boolean checkBans(String authorId) {
		return DatabaseQueryHandler.getInstance().checkCommandIsBannedForUser(authorId, this.getName());
	}
	
	private String getContentExtension(byte[] image) {
		String contentType = null;
		try {
			contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(this.image));
		} catch (IOException e) {}
		if(contentType == null || !contentType.startsWith("image/")) {
			return null;
		}
		contentType = contentType.substring("image/".length());
		return "."+contentType;
	}
	
	public CustomCommand(String name, String message, byte[] image, List<User> mentions) {
		this.name = name;
		this.message = message;
		this.image = image;
		if(mentions != null) {
			Collections.copy(this.mentions, mentions);
		}
	}

}
