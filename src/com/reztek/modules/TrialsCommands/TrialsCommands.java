package com.reztek.modules.TrialsCommands;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.reztek.JDBExtendedBot;
import com.reztek.Base.CommandModule;
import com.reztek.Global.GlobalDefs;
import com.reztek.Utils.BotUtils;
import com.reztek.modules.GuardianControl.Guardian;
import com.reztek.modules.GuardianControl.GuardianControlCommands;
import com.reztek.modules.GuardianControl.Guardian.GuardianWeaponStats;
import com.reztek.modules.TrialsCommands.Badges.TrialsDetailedBadge;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TrialsCommands extends CommandModule {
	
	public static final String PLUGIN_ID = "TRIALSCOMMANDS";
	public static final String PLUGIN_VER = "1.2";
	
	private static final String DTR_MAP_URL = "https://api.destinytrialsreport.com/currentMap";
	private static final String BUNGIE_BASE = "https://www.bungie.net";
	
	protected TrialsList p_trialsList = null;
	
	public static Collection<String> GetDependencies() {
		ArrayList<String> deps = new ArrayList<String>();
		deps.add(GuardianControlCommands.PLUGIN_ID);
		return deps;
	}

	public TrialsCommands() {
		super(PLUGIN_ID);
		setVersion(PLUGIN_VER);
		// I have a task!
		p_trialsList = new TrialsList();
		p_trialsList.setTaskName("TrialsList Refresh");
		addCommand(new String[] {
				"fireteam-ps", "fireteam-xb", "fireteam", 
				"fireteam+-ps", "fireteam+-xb", "fireteam+",
				"fireteam#-ps", "fireteam#-xb", "fireteam#", 
				"trials#-ps", "trials#-xb", "trials#",
				"trials+-ps", "trials+-xb", "trials+",
				"trials-ps", "trials-xb", "trials",
				"trialsmap", "trialslist-importcsv", "trialslist-csv",
				"trialslist", "trialslistgold", "trialslistsilver", "trialslistbronze",
				"trialslistwood", "trialsrefresh", "trialsaddtolist-ps", 
				"trialsaddtolist-xb", "trialsaddtolist", "trialsremovefromlist-ps", 
				"trialsremovefromlist-xb", "trialsremovefromlist"
		});
		setModuleNameAndAuthor("Trials of Osiris", "ChaseHQ85");
		p_trialsList.setTaskDelay(100);
		JDBExtendedBot.GetBot().addTask(p_trialsList);
	}

	@Override
	public void processCommand(String command, String args, MessageReceivedEvent mre) {
			switch (command) {
			case "fireteam-ps":
			case "fireteam-xb":
			case "fireteam":
			case "fireteam+-ps":
			case "fireteam+-xb":
			case "fireteam+":
				if (args == null) {
					sendHelpString(mre, "!fireteam[or !fireteam-ps or !fireteam-xb] PlayerNameHere");
				} else {
					fireteamInfoBadge(mre.getChannel(), args, Guardian.platformCodeFromCommand(command));
				}
				break;
			case "fireteam#-ps":
			case "fireteam#-xb":
			case "fireteam#":
				if (args == null) {
					sendHelpString(mre, "!fireteam+[or !fireteam+-ps or !fireteam+-xb] PlayerNameHere");
				} else {
					fireteamInfo(mre.getChannel(), args, Guardian.platformCodeFromCommand(command), true);
				}
				break;
			case "trials-ps":
			case "trials-xb":
			case "trials":
			case "trials+-ps":
			case "trials+-xb":
			case "trials+":
				if (args == null) {
					Guardian.PlatformCodeFromNicknameData d = Guardian.platformCodeFromNickname(mre.getMember().getEffectiveName());
					trialsInfoBadge(mre.getChannel(), d.getNickname(), d.usesTag() ? d.getPlatform() : Guardian.platformCodeFromCommand(command));
				} else {
					trialsInfoBadge(mre.getChannel(), args, Guardian.platformCodeFromCommand(command));
				}
			break;
			case "trials#-ps":
			case "trials#-xb":
			case "trials#":
				if (args == null) {
					Guardian.PlatformCodeFromNicknameData d = Guardian.platformCodeFromNickname(mre.getMember().getEffectiveName());
					trialsInfo(mre.getChannel(), d.getNickname(), d.usesTag() ? d.getPlatform() : Guardian.platformCodeFromCommand(command), true);
				} else {
					trialsInfo(mre.getChannel(), args, Guardian.platformCodeFromCommand(command), true);
				}
				break;
			case "trialsmap":
				trialsMap(mre);
				break;
			case "trialslist-importcsv":
				if (mre.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
					trialsImportCSV(mre);
				}
				break;
			case "trialslist-csv":
				if (mre.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
					trialListCSV(mre);
				} 
				break;
			case "trialslist":
				if (mre.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
					trialsList(mre.getChannel(), TrialsList.TRIALS_ALL, Color.WHITE);
				}
				break;
			case "trialslistgold":
				trialsList(mre.getChannel(), TrialsList.TRIALS_GOLD, new Color(212,175,55));
				break;
			case "trialslistsilver":
				trialsList(mre.getChannel(), TrialsList.TRIALS_SILVER, new Color(192,192,192));
				break;
			case "trialslistbronze":
				trialsList(mre.getChannel(), TrialsList.TRIALS_BRONZE, new Color(205, 127, 50));
				break;
			case "trialslistwood":
				trialsList(mre.getChannel(), TrialsList.TRIALS_WOOD, new Color(160, 82, 45));
				break;
			case "trialsrefresh":
				if (mre.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
					trialsRefresh(mre.getChannel());
				}
				break;
			case "trialsaddtolist-ps":
			case "trialsaddtolist-xb":
			case "trialsaddtolist":
				if (mre.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
					if (args == null) {
						sendHelpString(mre, "!trialsAddToList[or !trialsAddToList-ps or !trialsAddToList-xb] PlayerNameHere");
					} else {
						trialsAddToList(mre.getChannel(), args, Guardian.platformCodeFromCommand(command));
					}
				}
				break;
			case "trialsremovefromlist-ps":
			case "trialsremovefromlist-xb":
			case "trialsremovefromlist":
				if (mre.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
					if (args == null) {
						sendHelpString(mre, "!trialsRemoveFromList[or !trialsRemoveFromList-ps or !trialsRemoveFromList-xb] PlayerNameHere");
					} else {
						trialsRemoveFromList(mre.getChannel(), args, Guardian.platformCodeFromCommand(command));
					}
				}
				break;
		}
	}
	
	protected void trialsImportCSV(MessageReceivedEvent mre) {
		if (mre.getMessage().getAttachments().size() < 1) {
			mre.getChannel().sendMessage("Hmm... did you forget to attach a file?").queue();
		} else {
			if (mre.getMessage().getAttachments().get(0).getFileName().split("\\.")[1].equalsIgnoreCase("csv")) {
				// its a csv :)
				String tmpName = String.valueOf(System.currentTimeMillis()) + "-" + mre.getAuthor().getName() + "-tmp.tmp";
				File csvFile = new File(GlobalDefs.TMP_LOCATION + tmpName);
				try {
					csvFile.deleteOnExit();
					mre.getMessage().getAttachments().get(0).download(csvFile);
					CSVParser parser = CSVParser.parse(csvFile, StandardCharsets.UTF_8, CSVFormat.EXCEL.withHeader());
					Set<String> headers = parser.getHeaderMap().keySet();
					if (headers.contains("playername") && headers.contains("platform") && headers.contains("show")) {
						List<CSVRecord> records = parser.getRecords();
						mre.getChannel().sendMessage("Import of **" + records.size() + "** Record(s) starting...").queue();
						for (CSVRecord record : records) {
							if (record.get("show").equalsIgnoreCase("1")) {
								p_trialsList.addPlayer(mre.getChannel(), Guardian.guardianFromName(record.get("playername"), record.get("platform")), false);
							}
						}
						mre.getChannel().sendMessage("**Import complete**").queue();
					} else {
						mre.getChannel().sendMessage("The CSV Headers must be 'playername, platform, show' fix and re-upload").queue();
					}
				} catch (IOException e) {
					mre.getChannel().sendMessage("**Error creating temporary file**");
					e.printStackTrace();
				}
			} else {
				mre.getChannel().sendMessage("Sorry, I only accept CSV's for import.").queue();
			}
		}
	}
	
	protected void trialsMap(MessageReceivedEvent mre) {
		JSONObject ob = new JSONObject("{\"DTRArray\":" + BotUtils.GetJSONStringGet(DTR_MAP_URL, null) + "}").getJSONArray("DTRArray").getJSONObject(0);
		MessageBuilder mb = new MessageBuilder();
		EmbedBuilder emBu = new EmbedBuilder();
		emBu.setImage(BUNGIE_BASE + ob.getString("pgcrImage"));
		emBu.setColor(java.awt.Color.YELLOW);
		mb.append(mre.getAuthor().getAsMention() + " the Trials of Osiris map for this week is **"+ob.getString("activityName") + "**");
		mb.setEmbed(emBu.build());
		mre.getChannel().sendMessage(mb.build()).queue();
	}
	
	protected void trialListCSV(MessageReceivedEvent mre) {
		mre.getChannel().sendMessage("On it " + mre.getAuthor().getAsMention() + ", lets take this to a private chat!").queue();
		try {
			p_trialsList.sendListCSV(mre.getAuthor().openPrivateChannel().submit().get());
		} catch (InterruptedException | ExecutionException e) {
			mre.getChannel().sendMessage("Hmm... " + mre.getAuthor().getAsMention() + ", I tried to open a private chat with you but I was denied.").queue();
		}
	}
	
	protected void trialsList(MessageChannel mc, String indexStart, Color color) {
		p_trialsList.showList(mc, indexStart, color);
	}
	
	protected void trialsRefresh(MessageChannel mc) {
		p_trialsList.refreshList(mc, true);
	}
	
	protected void trialsRemoveFromList(MessageChannel mc, String playerName, String platform) {
		Guardian g = Guardian.guardianFromName(playerName, platform);
		if (g != null) {
			p_trialsList.removePlayer(mc,g);
		} else {
			mc.sendMessage("Hmm... Cant seem to find " + playerName + ", You sure you have the right platform or spelling?").queue();
		}
	}
	
	protected void trialsAddToList (MessageChannel mc, String playerName, String platform) {
		Guardian g = Guardian.guardianFromName(playerName, platform);
		if (g != null) {
			if (g.getTrialsRank() == "N/A" || g.getTrialsRank() == null) {
				mc.sendMessage("Sorry " + playerName + " hasn't played enough Trials of Osiris this season to be added.").queue();
			} else {
				p_trialsList.addPlayer(mc,g,true);
			}
		} else {
			mc.sendMessage("Hmm... Cant seem to find " + playerName + ", You sure you have the right platform or spelling?").queue();
		}
	}
	
	protected void trialsInfoBadge(MessageChannel mc, String playerName, String platform) {
		try {
			Guardian g = Guardian.guardianFromName(playerName, platform);
			TrialsDetailedBadge tb = TrialsDetailedBadge.TrialsDetailedBadgeFromGuardian(g);
			EmbedBuilder em = new EmbedBuilder();
			em.setImage(tb.finalizeBadge());
			mc.sendMessage(em.build()).queue();
			tb.cleanup();
		} catch (IOException e) {}
	}
	
	protected void trialsInfo(MessageChannel mc, String playerName, String platform, boolean verbose) {
		Guardian g = Guardian.guardianFromName(playerName, platform);
		if (g != null) {
			String bestWeps = "--  This Week Weapons  : Kills ( HS )--\n";
			int x = 0;
			for (GuardianWeaponStats ws : g.getThisWeekMapWeaponStats()) {
				x++;
				bestWeps += String.valueOf(x) + ". " + ws.getWeaponName() + BotUtils.GetPaddingForLen(ws.getWeaponName(), 18) + "  : " + 
						BotUtils.GetPaddingForLen(ws.getWeaponKills(), 5) + ws.getWeaponKills() + " (" + BotUtils.GetPaddingForLen(ws.getHeadshotPercentage(),6)  + ws.getHeadshotPercentage() + ")\n";
			}
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.YELLOW);
			eb.setTitle("**" + g.getName() + "**'s Trials of Osiris " + (verbose ? "Detailed " : "") + "Weekly Stats", null);
			eb.setThumbnail(g.getCharacterLastPlayedEmblem());
			eb.setDescription("```md\n" +
			"[Trials Elo]("+ BotUtils.GetPaddingForLen(g.getTrialsELO(), 4) + g.getTrialsELO() +")" + (verbose ? "<RK:"+ BotUtils.GetPaddingForLen(g.getTrialsRank(), 6) + g.getTrialsRank() +">" : "") + "\n" +
			"[Weekly K/D]("+ BotUtils.GetPaddingForLen(g.getThisWeekTrialsKD(), 4)+ g.getThisWeekTrialsKD() +")" + (verbose ? "<GP: "+ BotUtils.GetPaddingForLen(g.getThisWeekTrialsMatches(), 5) + g.getThisWeekTrialsMatches() +">" : "") + "\n" +
			"[Season K/D]("+ BotUtils.GetPaddingForLen(g.getThisYearTrialsKD(), 4)+ g.getThisYearTrialsKD() +")" + (verbose ? "<GP: "+ BotUtils.GetPaddingForLen(g.getThisYearTrialsMatches(), 5) + g.getThisYearTrialsMatches() +">" : "") + "\n" +
			"[Flawlesses]("+ BotUtils.GetPaddingForLen(g.getLighthouseCount(), 4)+ g.getLighthouseCount() +")" + (verbose ? "<WK: "+ BotUtils.GetPaddingForLen(g.getThisWeekTrialsFlawless(), 5) + g.getThisWeekTrialsFlawless() + ">" : "") + "\n" +
			bestWeps +
			"```");
			mc.sendMessage(eb.build()).queue();
		} else {
			mc.sendMessage("Hmm... Cant seem to find " + playerName + ", You sure you have the right platform or spelling?").queue();
		}
	}
	
	protected void fireteamInfoBadge(MessageChannel mc, String playerName, String platform) {
		Guardian g = Guardian.guardianFromName(playerName, platform);
		ArrayList<Guardian> gFireteam = new ArrayList<Guardian>();
		if (g != null) {
			ArrayList<HashMap<String,String>> members = g.getTrialsFireteamMembershipId();
			for (HashMap<String, String> hashMap : members) {
				gFireteam.add(Guardian.guardianFromName(hashMap.get("name"), hashMap.get("platform")));
			}
			try {
				mc.sendMessage("**" + g.getName() + "**'s Current Fireteam ").queue();
				EmbedBuilder em = new EmbedBuilder();
				em.setColor(Color.YELLOW);
				TrialsDetailedBadge gb = TrialsDetailedBadge.TrialsDetailedBadgeFromGuardian(g);
				em.setImage(gb.finalizeBadge());
				mc.sendMessage(em.build()).queue();
				gb.cleanup();
				for (Guardian gFt : gFireteam) {
					EmbedBuilder eFt = new EmbedBuilder();
					eFt.setColor(Color.YELLOW);
					TrialsDetailedBadge ftb = TrialsDetailedBadge.TrialsDetailedBadgeFromGuardian(gFt);
					eFt.setImage(ftb.finalizeBadge());
					mc.sendMessage(eFt.build()).queue();
					ftb.cleanup();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void fireteamInfo(MessageChannel mc, String playerName, String platform, boolean verbose) {
		Guardian g = Guardian.guardianFromName(playerName, platform);
		ArrayList<Guardian> gFireteam = new ArrayList<Guardian>();
		if (g != null) {
			ArrayList<HashMap<String,String>> members = g.getTrialsFireteamMembershipId();
			for (HashMap<String, String> hashMap : members) {
				gFireteam.add(Guardian.guardianFromName(hashMap.get("name"), hashMap.get("platform")));
			}
			String bestWeps = "--  This Week Weapons  : Kills ( HS )--\n";
			int x = 0;
			for (GuardianWeaponStats ws : g.getThisWeekMapWeaponStats()) {
				x++;
				bestWeps += String.valueOf(x) + ". " + ws.getWeaponName() + BotUtils.GetPaddingForLen(ws.getWeaponName(), 18) + "  : " + 
						BotUtils.GetPaddingForLen(ws.getWeaponKills(), 5) + ws.getWeaponKills() + " (" + BotUtils.GetPaddingForLen(ws.getHeadshotPercentage(),6)  + ws.getHeadshotPercentage() + ")\n";
			}
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.YELLOW);
			eb.setTitle(g.getName(), null);
			eb.setFooter(g.getCharacterLastPlayedSubclass(), g.getCharacterLastPlayedSubclassIcon());
			eb.setThumbnail(g.getCharacterLastPlayedEmblem());
			eb.setDescription("```md\n" +
					"[Trials Elo]("+ BotUtils.GetPaddingForLen(g.getTrialsELO(), 4) + g.getTrialsELO() +")" + (verbose ? "<RK:"+ BotUtils.GetPaddingForLen(g.getTrialsRank(), 6) + g.getTrialsRank() +">" : "") + "\n" +
					"[Weekly K/D]("+ BotUtils.GetPaddingForLen(g.getThisWeekTrialsKD(), 4)+ g.getThisWeekTrialsKD() +")" + (verbose ? "<GP: "+ BotUtils.GetPaddingForLen(g.getThisWeekTrialsMatches(), 5) + g.getThisWeekTrialsMatches() +">" :"") +"\n" +
					"[Season K/D]("+ BotUtils.GetPaddingForLen(g.getThisYearTrialsKD(), 4)+ g.getThisYearTrialsKD() +")" + (verbose ? "<GP: "+ BotUtils.GetPaddingForLen(g.getThisYearTrialsMatches(), 5) + g.getThisYearTrialsMatches() +">" : "") + "\n" +
					"[Flawlesses]("+ BotUtils.GetPaddingForLen(g.getLighthouseCount(), 4)+ g.getLighthouseCount() +")" + (verbose ? "<WK: "+ BotUtils.GetPaddingForLen(g.getThisWeekTrialsFlawless(), 5) + g.getThisWeekTrialsFlawless() +  ">" : "") + "\n" +
					(verbose ? bestWeps : "") +
					"```");
			mc.sendMessage("**" + g.getName() + "**'s Current Fireteam " + (verbose ? "Detailed " : "") + "Weekly Stats").queue();
			mc.sendMessage(eb.build()).queue();
			for (Guardian gFt : gFireteam) {
				String bestWepsFt = "--  This Week Weapons  : Kills ( HS )--\n";
				int xFt = 0;
				for (GuardianWeaponStats ws : gFt.getThisWeekMapWeaponStats()) {
					xFt++;
					bestWepsFt += String.valueOf(xFt) + ". " + ws.getWeaponName() + BotUtils.GetPaddingForLen(ws.getWeaponName(), 18) + "  : " + 
							BotUtils.GetPaddingForLen(ws.getWeaponKills(), 5) + ws.getWeaponKills() + " (" + BotUtils.GetPaddingForLen(ws.getHeadshotPercentage(),6)  + ws.getHeadshotPercentage() + ")\n";
				}
				eb = new EmbedBuilder();
				eb.setColor(Color.YELLOW);
				eb.setFooter(gFt.getCharacterLastPlayedSubclass(), gFt.getCharacterLastPlayedSubclassIcon());
				eb.setThumbnail(gFt.getCharacterLastPlayedEmblem());
				eb.setTitle(gFt.getName(), null);
				eb.setDescription("```md\n" +
					"[Trials Elo]("+ BotUtils.GetPaddingForLen(gFt.getTrialsELO(), 4) + gFt.getTrialsELO() +")" + (verbose ? "<RK:"+ BotUtils.GetPaddingForLen(gFt.getTrialsRank(), 6) + gFt.getTrialsRank() +">" :"") +"\n" +
					"[Weekly K/D]("+ BotUtils.GetPaddingForLen(gFt.getThisWeekTrialsKD(), 4)+ gFt.getThisWeekTrialsKD() +")" + (verbose ? "<GP: "+ BotUtils.GetPaddingForLen(gFt.getThisWeekTrialsMatches(), 5) + gFt.getThisWeekTrialsMatches() +">" :"") +"\n" +
					"[Season K/D]("+ BotUtils.GetPaddingForLen(gFt.getThisYearTrialsKD(), 4)+ gFt.getThisYearTrialsKD() +")" + (verbose ? "<GP: "+ BotUtils.GetPaddingForLen(gFt.getThisYearTrialsMatches(), 5) + gFt.getThisYearTrialsMatches() +">" :"") +"\n" +
					"[Flawlesses]("+ BotUtils.GetPaddingForLen(gFt.getLighthouseCount(), 4)+ gFt.getLighthouseCount() +")" + (verbose ? "<WK: "+ BotUtils.GetPaddingForLen(gFt.getThisWeekTrialsFlawless(), 5) + gFt.getThisWeekTrialsFlawless() +  ">" :"") +"\n" +
					(verbose ? bestWepsFt : "") +
					"```");
				mc.sendMessage(eb.build()).queue();
			}
		} else {
			mc.sendMessage("Hmm... Cant seem to find " + playerName + ", You sure you have the right platform or spelling?").queue();
		}
	}
}
