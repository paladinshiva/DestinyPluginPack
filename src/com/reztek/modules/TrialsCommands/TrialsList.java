package com.reztek.modules.TrialsCommands;

import java.awt.Color;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.reztek.Base.Taskable;
import com.reztek.Utils.BotUtils;
import com.reztek.Utils.MySQLConnector;
import com.reztek.modules.GuardianControl.Guardian;
import com.reztek.modules.TrialsCommands.Badges.TrialsListBadge;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageBuilder.SplitPolicy;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;

public class TrialsList extends Taskable {
	
	public static final String TRIALS_ALL    = "-1";
	public static final String TRIALS_GOLD   = "0";
	public static final String TRIALS_SILVER = "10";
	public static final String TRIALS_BRONZE = "20";
	public static final String TRIALS_WOOD   = "30";

	public void addPlayer(MessageChannel mc, Guardian guardian, boolean verbose) {
		if (guardian == null) return;
		try {
			ResultSet rs = MySQLConnector.getInstance().runQueryWithResult("SELECT * FROM trialsList WHERE membershipId = '" + guardian.getId() + "'");
			if (!rs.last()) {
				// userdoesn't exist add them
				MySQLConnector.getInstance().runUpdateQuery("INSERT INTO trialsList (membershipId,platform,playerName,rank,elo,flawlessCount) VALUES ('"
						+ guardian.getId() + "','" + guardian.getPlatform() + "','" + guardian.getName() + "','" 
						+ guardian.getTrialsRank() + "'," + guardian.getTrialsELO() + "," + guardian.getLighthouseCount() + ")");
				if (verbose) mc.sendMessage("Succesfully added " + guardian.getName() + " to the Trials of Osiris List").queue();
			} else {
				// user already exists in DB
				if (verbose) mc.sendMessage(guardian.getName() + " already exists in the Trials of Osiris List").queue();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void removePlayer (MessageChannel mc, Guardian guardian) {
		ResultSet rs = MySQLConnector.getInstance().runQueryWithResult("SELECT * FROM trialsList WHERE membershipId = '" + guardian.getId() + "'");
		try {
			if (!rs.last()) {
				mc.sendMessage(guardian.getName() + " is not in the Trials List").queue();
			} else {
				MySQLConnector.getInstance().runUpdateQuery("DELETE FROM trialsList WHERE membershipId = '" + guardian.getId() + "'");
				mc.sendMessage(guardian.getName() + " has been removed from the Trials of Osiris List").queue();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void showList(MessageChannel mc, String startIndex, Color color) {
		String Query = "SELECT * FROM trialsList WHERE rank IS NOT NULL AND rank != 0 ORDER BY rank ASC LIMIT 10 OFFSET " + startIndex;
		if (startIndex.equals(TRIALS_ALL)) {
			Query = "SELECT * FROM trialsList WHERE rank IS NOT NULL AND rank != 0 ORDER BY rank ASC";
			startIndex = "0";
		} 
		ResultSet rs = MySQLConnector.getInstance().runQueryWithResult(Query);
		try {
			if (startIndex.equals(TRIALS_ALL)) {
				int x = 1;
				String platformName = "";
				String trialsList = "";
				while (rs.next()) {
					if (rs.getString("platform").equalsIgnoreCase("1")) platformName = "XB";
					if (rs.getString("platform").equalsIgnoreCase("2")) platformName = "PS";
					trialsList += String.valueOf(x + Integer.valueOf(startIndex)) + "." + BotUtils.GetPaddingForLen(String.valueOf(x++ + Integer.valueOf(startIndex)), 3) + 
							rs.getString("playerName") + BotUtils.GetPaddingForLen(rs.getString("playerName"),18) +
							" (Elo: " + BotUtils.GetPaddingForLen(rs.getString("elo"), 4) + rs.getString("elo") + " |"+ platformName +
							"| Rank:" + BotUtils.GetPaddingForLen(rs.getString("rank"),6) + rs.getString("rank") + " | FC:" + 
							BotUtils.GetPaddingForLen(rs.getString("flawlessCount"), 4) + rs.getString("flawlessCount") + ")\n";
				}
				MessageBuilder mb = new MessageBuilder();
				mb.append(trialsList);
				int page = 0;
				for (Message msg : mb.buildAll(SplitPolicy.NEWLINE)) {
					EmbedBuilder em = new EmbedBuilder();
					em.setDescription((page == 0 ? "**Current Trials of Osiris Leaders**" : "") + "```" + msg.getRawContent() + "```");
					em.setColor(color);
					mc.sendMessage(em.build()).queue();
					page++;
				}
			} else {
				try {
					TrialsListBadge tb = TrialsListBadge.BadgeFromType(startIndex);
					while (rs.next()) {
						String platformName = "";
						if (rs.getString("platform").equalsIgnoreCase("1")) platformName = "XB";
						if (rs.getString("platform").equalsIgnoreCase("2")) platformName = "PS";
						tb.addPlayer(rs.getString("playerName"), rs.getString("rank"), rs.getString("elo"), platformName, rs.getString("flawlessCount"));
					}
					EmbedBuilder em = new EmbedBuilder();
					em.setColor(color);
					em.setImage(tb.finalizeBadge());
					mc.sendMessage(em.build()).queue();
					tb.cleanup();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void sendListCSV(PrivateChannel pc) {
		String csvOut = "";
		refreshList(pc, true);
		pc.sendMessage("*Now generating CSV File and sending... Please wait...*").queue();
		pc.sendTyping().queue();
		ResultSet rs = MySQLConnector.getInstance().runQueryWithResult("SELECT * FROM trialsList");
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int x = 1; x <= rsmd.getColumnCount(); ++x) {
				csvOut += rsmd.getColumnName(x) + (rsmd.getColumnCount() == x ? "" : ",");
			}
			csvOut += "\n";
			while (rs.next()) {
				for (int x = 1; x <= rsmd.getColumnCount(); ++x) {
					csvOut += rs.getString(x) + (rsmd.getColumnCount() == x ? "" : ",");
				}
				csvOut += "\n";
			}
			pc.sendFile(csvOut.getBytes(), "TrialListExport.csv", null).queue();;
		} catch (SQLException e) {
			e.printStackTrace();
			pc.sendMessage("**Error generating CSV File**").queue();
		}
	}
	
	public void refreshList(MessageChannel mc, boolean verbose) {
		if (verbose) {
			mc.sendMessage("*Updating Trials of Osiris List Please Wait...*").queue();
			mc.sendTyping().queue();
		}
		ResultSet rs = MySQLConnector.getInstance().runQueryWithResult("SELECT * FROM trialsList");
		Guardian g;
		try {
			while (rs.next()) {
				g = Guardian.guardianFromMembershipId(rs.getString("membershipId"),rs.getString("playerName"), rs.getString("platform"));
				MySQLConnector.getInstance().runUpdateQuery("UPDATE trialsList SET elo = " + g.getTrialsELO() + ", rank = " + g.getTrialsRank() + ", flawlessCount = " + g.getLighthouseCount() + " WHERE membershipId = '" + g.getId() + "'");
			}
			if (verbose) mc.sendMessage("*Updating Complete!*").queue();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void runTask() {
		System.out.println("TrialsList Task Running...");
		refreshList(null, false);
		System.out.println("TrialsList Task Complete...");
	}
}
