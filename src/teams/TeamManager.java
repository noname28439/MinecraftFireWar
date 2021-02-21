package teams;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.Main;

public class TeamManager {

	private static HashMap<Player, Team> teamConnector = new HashMap<>();
	
	public static final int MAX_BALANCE_OFFSET = 10;
	
	public static String teamSelectionInventoryTitle = "ß6ßl<--Teamauswahl-->";
	
	public static Team getTeamByButtonMaterial(Material buttonMaterial) {
		for(Team t : Team.values())
			if(t.getButtonMaterial()==buttonMaterial)
				return t;
		return null;
	}
	
	public static Team getPlayerTeam(Player player) {
		return teamConnector.containsKey(player) ? teamConnector.get(player) : null;
	}
	
	public static void setPlayerTeam(Player player, Team team) {
		if(getPlayerTeam(player)!=null)
			getPlayerTeam(player).getTeamPlayers().remove(player);
		team.getTeamPlayers().add(player);
		
		teamConnector.put(player, team);
		player.setDisplayName(team.getChatColor()+player.getName());
		player.setPlayerListName(team.getChatColor()+player.getName());
		player.sendMessage("Du bist jetzt in Team "+team.getChatColor()+team.getTeamName());
	}
	
	public static void balanceTeams() {
		int offset = getMostPlayersTeam().getTeamPlayers().size()-getLeastPlayersTeam().getTeamPlayers().size();
		if(offset>=MAX_BALANCE_OFFSET) {
			Bukkit.broadcastMessage(Main.PREFIX+"ß7Wegen zu groﬂen Unterschiede der Anzahl der Spieler der einzelnen Teams wurden die Teams neu sortiert!");
			while(offset>=MAX_BALANCE_OFFSET) {
				Player player = getMostPlayersTeam().getTeamPlayers().get(getMostPlayersTeam().getTeamPlayers().size()-1);
				setPlayerTeam(player, getLeastPlayersTeam());
				player.sendMessage(Main.PREFIX+"Wegen zu groﬂen Unterschiede der Anzahl der Spieler der einzelnen Teams wurdest du in "+getPlayerTeam(player).getChatColor()+"Team "+getPlayerTeam(player).getTeamName()+" ß7 verschoben!");
				offset = getMostPlayersTeam().getTeamPlayers().size()-getLeastPlayersTeam().getTeamPlayers().size();
			}
		}
	}
	
	
	public static void sortInPlayer(Player player) {
		Team team = getLeastPlayersTeam();
		setPlayerTeam(player, team);
		player.sendMessage(Main.PREFIX+"ß7Du wurdest "+team.getChatColor()+"Team "+team.getTeamName()+" ß7zugeordnet!");
	}
	
	private static Team getLeastPlayersTeam() {
		Team currentLeastPlayersTeam = Team.values()[0];
		for(Team currentTeam : Team.values()) {
			if(currentTeam.getTeamPlayers().size()< currentLeastPlayersTeam.getTeamPlayers().size())
				currentLeastPlayersTeam = currentTeam;
		}
		return currentLeastPlayersTeam;
	}
	
	private static Team getMostPlayersTeam() {
		Team currentMostPlayersTeam = Team.values()[0];
		for(Team currentTeam : Team.values()) {
			if(currentTeam.getTeamPlayers().size()> currentMostPlayersTeam.getTeamPlayers().size())
				currentMostPlayersTeam = currentTeam;
		}
		return currentMostPlayersTeam;
	}
	
	public static Inventory getSelectionInventory() {
		Inventory inv = Bukkit.createInventory(null, 9*1,teamSelectionInventoryTitle);
		for(Team currentTeam : Team.values()) {
			ItemStack TeamButton = new ItemStack(currentTeam.getButtonMaterial(), 1);
			ItemMeta meta = TeamButton.getItemMeta();
			meta.setDisplayName(currentTeam.getChatColor()+currentTeam.getTeamName());
			TeamButton.setItemMeta(meta);
			inv.addItem(TeamButton);
			
		}
		return inv;
	}
	
	
}
