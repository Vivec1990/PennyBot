package net.vivec.pennybot;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.User;
import net.vivec.pennybot.command.CustomCommand;
import net.vivec.pennybot.command.RemindMe;
import net.vivec.pennybot.database.DatabaseQueryHandler;
import net.vivec.pennybot.database.dto.CustomCommandDTO;

public class PennyBot {
	
	private String discordToken;
	private Logger logger = Logger.getLogger(PennyBot.class.getName());
	private JDA jda;
	
	public PennyBot(String discordToken) {
		this.setDiscordToken(discordToken);
		this.setJda(this.connect());
		if(this.getJda() == null) {
			throw new IllegalStateException("JDA object was not correctly created. PennyBot will shut down.");
		} else {
			this.getJda().addEventListener(initCustomCommandHandler());
			this.getJda().addEventListener(initSystemCommandHandler());
		}
	}
	
	private JDA connect() {
		try {
			return new JDABuilder(AccountType.BOT)
					.setToken(getDiscordToken())
					.buildBlocking();
		} catch (LoginException e) {
			logger.log(Level.SEVERE, "Error logging into the discord bot account. Check the token", e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Errpr building the JDA object.", e);
		}
		return null;
	}
	
	public CommandClient initSystemCommandHandler() {
		CommandClientBuilder systemClient = new CommandClientBuilder();
		systemClient.setPrefix("!");
		systemClient.setOwnerId("189811271690878976");
		systemClient.addCommand(new RemindMe());
		return systemClient.build();
	}
	
	public CommandClient initCustomCommandHandler() {
		CommandClientBuilder customClient = new CommandClientBuilder();
		customClient.setPrefix("!");
		customClient.setAlternativePrefix("+");
		
		customClient.setOwnerId("189811271690878976");
		
		List<CustomCommandDTO> availableCommands = DatabaseQueryHandler.getInstance().getAllCustomCommands();
		for(CustomCommandDTO dto : availableCommands) {
			String[] mentionedUserIDs = dto.getMentions() == null ? null : dto.getMentions().split(",");
			List<User> mentionedUsers = null;
			if(mentionedUserIDs != null && mentionedUserIDs.length > 0) {
				for(String userID : mentionedUserIDs) {
					try {
						User user = this.getJda().getUserById(userID);
						mentionedUsers.add(user);
					} catch(NullPointerException npe) {
						continue;
					}
				}
			}
			CustomCommand cmd = new CustomCommand(dto.getName(), dto.getMessage(), dto.getImage(), mentionedUsers);
			customClient.addCommand(cmd);
		}
		
		return customClient.build();
	}
	
	public String getDiscordToken() {
		return discordToken;
	}

	public void setDiscordToken(String discordToken) {
		this.discordToken = discordToken;
	}

	public JDA getJda() {
		return jda;
	}

	private void setJda(JDA jda) {
		this.jda = jda;
	}

	public static void main(String[] args) {
		new PennyBot("NDEzMDA4Njc3NDY1ODgyNjQ2.DWSk0g.ohfjvFrHpeAcoFfp1kN17CGeB-w");
	}
	
}
