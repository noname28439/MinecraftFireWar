package game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class GameStateListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(p.getInventory().getItemInMainHand()!=null) {
			ItemStack inHand = p.getInventory().getItemInMainHand();
			
			if(inHand.equals(LobbyState.TeamSelector())) {
				//Open TeamSelection Inventory
				
			}
			
			
		}
		
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState) {
			//Game currently in LobbyState
			LobbyState.setInventory(p);
			
		}else {
			//Game already started
			p.sendMessage(ChatColor.RED+"Das Spiel hat schon angefangen!");
		}
		
		
	}
	
	
	
}
