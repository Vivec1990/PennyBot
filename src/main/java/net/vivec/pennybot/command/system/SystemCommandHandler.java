package net.vivec.pennybot.command.system;

import net.dv8tion.jda.core.entities.User;

public class SystemCommandHandler {

	/**
	 * By default, system commands are only usable by the administrator, configure the user id here
	 * TODO: implement a way to unlock this for other groups/users dynamically
	 */
	private final static String ADMIN_ID = "189811271690878976L";
	
	private static SystemCommandHandler INSTANCE;
	
	public static SystemCommandHandler getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SystemCommandHandler();
		}
		return INSTANCE;
	}
	
	public void handleSystemCommand(User author) {
		if(!ADMIN_ID.equals(author.getId())) return;
	}
	
	
}
