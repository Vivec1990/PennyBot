package net.vivec.pennybot.command.util;

import java.util.ArrayList;
import java.util.List;

public class UtilityCommandHandler {

	List<UtilityCommand> registeredCommands;
	private static UtilityCommandHandler INSTANCE;
	
	private UtilityCommandHandler() {
		this.registeredCommands = new ArrayList<UtilityCommand>();
	}
	
	public static UtilityCommandHandler getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new UtilityCommandHandler();
		}
		return INSTANCE;
	}
	
	public boolean register(UtilityCommand command) {
		if(checkDuplicate(command)) return false;
		registeredCommands.add(command);
		return true;
	}
	
	private boolean checkDuplicate(UtilityCommand command) {
		for(UtilityCommand comm : registeredCommands) {
			if(comm.getName().equalsIgnoreCase(command.getName())) return true;
		}
		return false;
	}
	
	public boolean commandExists(String name) {
		UtilityCommand util = new UtilityCommand(name);
		return checkDuplicate(util);
	}
	
}
