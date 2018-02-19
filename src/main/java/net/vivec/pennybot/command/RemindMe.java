package net.vivec.pennybot.command;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public class RemindMe extends Command{

	@Override
	protected void execute(CommandEvent event) {
		
		String message = event.getMessage().getContentStripped().substring(this.getName().length() + 2);
		String[] messageParts = message.split(" ");
		if(messageParts.length < 2) {
			event.reply("Incorrect command usage, you dunce.");
			return;
		}
		
		User userToRemind = null;
		if(messageParts[0].equals("me")) {
			 userToRemind = event.getAuthor();
		} else {
			if(event.getMessage().getMentionedMembers().isEmpty()) {
				event.reply("Incorrect command usage, you dunce.");
				return;
			}
			userToRemind = event.getMessage().getMentionedMembers().get(0).getUser();
		}
		
		
		String time = messageParts[1];
		Integer timeInSec = 0;
		if(time.matches("[+-]?\\d+")) {
			timeInSec = Integer.parseInt(time);
		} else if(time.endsWith("s") || time.endsWith("m") || time.endsWith("h")) {
			String timePortion = time.substring(0, time.length() - 1);
			if(timePortion.matches("[+-]?\\d+")) {
				String unit = time.substring(time.length() - 1);
				timeInSec = Integer.parseInt(timePortion);
				if("s".equals(unit)) {
					timeInSec *= 1;
				} else if("m".equals(unit)) {
					timeInSec *= 60;
				} else if("h".equals(unit)) {
					timeInSec *= 3600;
				} else {
					event.reply("Incorrect command usage, you dunce.");
					return;
				}
			} else {
				event.reply("Incorrect command usage, you dunce.");
				return;
			}
		}
		MessageBuilder initialResponse = new MessageBuilder();
		initialResponse.append("Ok, ").append(userToRemind.getAsMention()).append(". I will remind you in " + timeInSec + " seconds as requested.");
		
		String reminderMessage = null;
		if(messageParts.length > 2) {
			reminderMessage = String.join(" ", Arrays.copyOfRange(messageParts, 2, messageParts.length));
			initialResponse.append(" The reminder message will be: `" + reminderMessage + "`");
		}
		
		event.reply(initialResponse.build());
		
		final User userReminder = userToRemind;
		final String messageReminder = reminderMessage; 
		
		Timer scheduledReminder = new Timer();
		scheduledReminder.schedule(new TimerTask() {

			@Override
			public void run() {
				MessageBuilder msg = new MessageBuilder("Reminding you as requested! ");
				msg.append(userReminder.getAsMention());
				if(messageReminder != null) {
					msg.append("\nMessage: `" + messageReminder + "`");
				}
				event.reply(msg.build());
			}
			
		}, timeInSec * 1000);
	}
	
	public RemindMe() {
		this.name = "remind";
	}

}
