package net.vivec.pennybot.command;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandEventEx extends CommandEvent {

	public CommandEventEx(MessageReceivedEvent event, String args, CommandClient client) {
		super(event, args, client);
	}
	
	public CommandEventEx(CommandEvent ev) {
		super(ev.getEvent(), ev.getArgs(), ev.getClient());
	}
	
	public void reply(String message, byte[] file, String filename, List<User> mentions) {
		if(message == null || message.isEmpty()) {
			message = " ";
		}
		MessageBuilder msg = new MessageBuilder(message);
		
		if(mentions != null && !mentions.isEmpty()) {
			for(User u : mentions) {
				msg.append(" ").append(u.getAsMention());
			}
			msg.append("\n");
		}
		
		MessageChannel chan = this.getEvent().getChannel();
		if(file != null && file.length > 0) {
			chan.sendFile(file, filename, msg.build()).queue();
		} else {
			chan.sendMessage(msg.build()).queue();
		}
		
	}

}
