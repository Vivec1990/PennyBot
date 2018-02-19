package net.vivec.pennybot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.vivec.pennybot.database.dto.CustomCommandDTO;

public class DatabaseQueryHandler {

	private static final String DB_LOCATION = System.getProperty("user.home") + "/PennyBot/";
	private static final String DB_NAME = "pennybotv2.db";

	private Connection conn = null;
	
	private static DatabaseQueryHandler INSTANCE = null;
	
	public static DatabaseQueryHandler getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new DatabaseQueryHandler();
		}
		return INSTANCE;
	}
	
	public Connection getDatabaseConnection() {
		if(conn == null) {
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:"+DB_LOCATION+DB_NAME);
			} catch (SQLException e) {}
		}
		return conn;
	}
	
	public List<CustomCommandDTO> getAllCustomCommands() {
		List<CustomCommandDTO> results = new ArrayList<CustomCommandDTO>();
		
		String selectStmt = "SELECT name, message, image, mentions FROM custom_commands;";
		Connection db = getDatabaseConnection();
		
		if(db != null) {
			Statement stmt;
			try {
				stmt = db.createStatement();
				ResultSet rs = stmt.executeQuery(selectStmt);
				while(rs.next()) {
					CustomCommandDTO dto = new CustomCommandDTO();
					dto.setName(rs.getString("name"));
					dto.setMessage(rs.getString("message"));
					dto.setImage(rs.getBytes("image"));
					dto.setMentions(rs.getString("mentions"));
					results.add(dto);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public List<String> getBannedCommandsByUserID(String id) {
		List<String> results = new ArrayList<String>();
		
		String selectStmt = "SELECT command_name FROM command_ban WHERE user_id = ?";
		Connection db = getDatabaseConnection();
		if(db != null) {
			PreparedStatement stmt;
			try {
				stmt = db.prepareStatement(selectStmt);
				stmt.setString(1, id);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					String commandName = rs.getString("command_name");
					results.add(commandName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public boolean checkCommandIsBannedForUser(String userId, String commandName) {
		String selectStmt = "SELECT command_name FROM command_ban WHERE user_id = ? and command_name = ?";
		Connection db = getDatabaseConnection();
		if(db != null) {
			PreparedStatement stmt;
			try {
				stmt = db.prepareStatement(selectStmt);
				stmt.setString(1, userId);
				stmt.setString(2, commandName);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					String test = "test";
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private static void transferOldData() throws SQLException {
		String oldDB = "jdbc:sqlite:" + DB_LOCATION + "pb.sqlite";
		String newDB = "jdbc:sqlite:" + DB_LOCATION + DB_NAME;
		String selectOld = "SELECT name, message, file, mention from\r\n" + 
				"command inner join command_msg on\r\n" + 
				"command.type_id = command_msg.id;";
		Connection oldCon = DriverManager.getConnection(oldDB);
		Statement stmt = oldCon.createStatement();
		ResultSet oldCmds = stmt.executeQuery(selectOld);
		
		Connection newCon = DriverManager.getConnection(newDB);
		
		String prepareInsert = "INSERT INTO custom_commands(name, message, image, mentions) VALUES(?, ?, ?, ?);";
		while(oldCmds.next()) {
			PreparedStatement insert = newCon.prepareStatement(prepareInsert);
			insert.setString(1, oldCmds.getString("name"));
			insert.setString(2, oldCmds.getString("message"));
			insert.setBytes(3, oldCmds.getBytes("file"));
			insert.setString(4, oldCmds.getString("mention"));
			insert.execute();
		}
		oldCon.close();
		newCon.close();
	}
	
	public static void main(String[] args) throws SQLException {
		transferOldData();
	}
	
}
