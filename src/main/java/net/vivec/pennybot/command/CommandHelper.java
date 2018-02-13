package net.vivec.pennybot.command;

import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.vivec.pennybot.command.util.UtilityCommandHandler;

public class CommandHelper {

	public static final String CUSTOM_COMMAND_PREFIX = "!";
	public static final String SYSTEM_COMMAND_PREFIX = "@@";
	public static final String UTILITY_COMMAND_PREFIX = "!";
	public static final String[] COMMAND_PREFIXES = {
			CUSTOM_COMMAND_PREFIX, 
			SYSTEM_COMMAND_PREFIX, 
			UTILITY_COMMAND_PREFIX
	};
	
	private static CommandHelper INSTANCE;
	
	public static CommandHelper getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new CommandHelper();
		}
		return INSTANCE;
	}
	
	private String parseCommandNameFromMessage(Message message) {
		String messageContent = message.getContentRaw();
		return StringUtils.substringBetween(messageContent, "!", " ");
	}
	
	public static void main(String[] args) {
		System.out.println(CommandHelper.getInstance().parseCommandNameFromMessage(new MessageBuilder("ajksdhlfk asdfhalskd asdjkfhals !daiske ajsdkljalsjd").build()));
	}
	
	public void handleCommand(Command command) {
		String commandName = parseCommandNameFromMessage(command.getMessage());
	}
	
	private boolean checkSystemCommands(Command command) {
		return false;
	}
	
	private boolean checkUtilityCommands(Command command) {
		return UtilityCommandHandler.getInstance().commandExists("");
	}
}
