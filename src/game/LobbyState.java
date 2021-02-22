package game;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.Main;
import net.md_5.bungee.api.ChatColor;
import teams.TeamManager;

public class LobbyState extends GameState{
	
	static World lobbyWorld = Main.mainWorld;
	static Location lobbyLocation = new Location(lobbyWorld, 0, 60, 0);
	
	public static ItemStack TeamSelector() {
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"TeamSelector");
		item.setItemMeta(meta);
		return item;
	}
	
	public static void setInventory(Player cp) {
		System.out.println("Setlobbystate Inv ["+cp.getName()+"]");
		cp.getInventory().clear();
		cp.getInventory().setItem(4, TeamSelector());
	}
	
	@Override
	public void start() {
		if(lobbyWorld!=null) {
			lobbyWorld.setDifficulty(Difficulty.PEACEFUL);
			lobbyWorld.setTime(1000);
		}else
			System.out.println("Warnung: LobbyWorld = null! (LobbyState.start)");
		
		for(Player cp : Bukkit.getOnlinePlayers()) {
			setInventory(cp);
			cp.teleport(lobbyLocation);
			cp.setGameMode(GameMode.SURVIVAL);
		}
			
			
		
	}

	@Override
	public void stop() {
		for(Player cp : Bukkit.getOnlinePlayers())
			if(TeamManager.getPlayerTeam(cp)==null)
				TeamManager.sortInPlayer(cp);
		
		TeamManager.balanceTeams();
		
	}

	@Override
	int getID() {return GameStateManager.LobbyState;}


}
