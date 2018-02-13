package net.vivec.pennybot.listener;

import java.time.Duration;
import java.time.OffsetDateTime;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.vivec.pennybot.command.Command;
import net.vivec.pennybot.command.CommandHelper;

public class ChatListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		User author = event.getAuthor();
		boolean isBot = author.isBot();
		if(isBot) return;
		if(!event.isFromType(ChannelType.TEXT)) return;
		
		Member member = event.getMember();
		
		OffsetDateTime memberJoined = member.getJoinDate();
		Duration memberAge = Duration.between(memberJoined, OffsetDateTime.now());
		if(memberAge.getSeconds() < 60 * 10) return;
		
		JDA jda = event.getJDA();
		Message message = event.getMessage();
		
		boolean containsCommandPrefix = false;
		for(String s : CommandHelper.COMMAND_PREFIXES) {
			if(message.getContentStripped().contains(s)) {
				containsCommandPrefix = true;
				break;
			}
		}
		if(!containsCommandPrefix) return;
		
		Command command = new Command(message);
		CommandHelper.getInstance().handleCommand(command);
		
	}
	
}
