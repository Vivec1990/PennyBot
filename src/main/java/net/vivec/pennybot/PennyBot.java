package net.vivec.pennybot;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class PennyBot {
	
	private String discordToken;
	private Logger logger = Logger.getLogger(PennyBot.class.getName());
	private JDA jda;
	
	public PennyBot(String discordToken) {
		this.setDiscordToken(discordToken);
		this.connect();
		if(this.getJda() == null) {
			throw new IllegalStateException("JDA object was not correctly created. PennyBot will shut down.");
		}
	}
	
	private JDA connect() {
		try {
			jda = new JDABuilder(AccountType.BOT)
					.setToken(getDiscordToken())
					.buildBlocking();
			return jda;
		} catch (LoginException e) {
			logger.log(Level.SEVERE, "Error logging into the discord bot account. Check the token", e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Errpr building the JDA object.", e);
		}
		return null;
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

	public void setJda(JDA jda) {
		this.jda = jda;
	}

	public static void main(String[] args) {
		new PennyBot("NDEzMDA4Njc3NDY1ODgyNjQ2.DWSk0g.ohfjvFrHpeAcoFfp1kN17CGeB-w");
	}
	
}
