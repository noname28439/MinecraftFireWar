package teams;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TeamListener implements Listener{

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		HumanEntity p = e.getWhoClicked();
		Inventory inv = e.getInventory();
		String title = e.getView().getTitle();
		ItemStack clicked = e.getCurrentItem();
		if(clicked==null)
			return;
		
		if(title.equalsIgnoreCase(TeamManager.teamSelectionInventoryTitle)) {
			TeamManager.setPlayerTeam((Player)p, TeamManager.getTeamByButtonMaterial(e.getCurrentItem().getType()));
			e.setCancelled(true);
		}
		
		
		
	}
	
}
