package commads;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;



public class TimCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(args.length==0) {
				
				
				
				
				p.sendMessage("Na du kleiner! Du hast den Geheimbefehl entdeckt! Du wirst nie wissen was er bewirkt...");
				
				
				
				Inventory inv = Bukkit.createInventory(null, 9 * 3, "Troll Menü");
				
				ItemStack tntitem = new ItemStack(Material.TNT);
				tntitem.setAmount(64);
				ItemMeta tntitemmeta = tntitem.getItemMeta();
				tntitem.setItemMeta(tntitemmeta);
				inv.setItem(13, tntitem);
				
				
				ItemStack tridentitem = new ItemStack(Material.TRIDENT);
				ItemMeta tridentmeta = tridentitem.getItemMeta();
				tridentitem.setItemMeta(tridentmeta);
				inv.setItem(11, tridentitem);
				
				
				ItemStack eiitem = new ItemStack(Material.EGG);
				eiitem.setAmount(64);
				ItemMeta eiitemmeta = eiitem.getItemMeta();
				eiitem.setItemMeta(eiitemmeta);
				inv.setItem(15, eiitem);
				
				
				p.openInventory(inv);
				

				
				
			}else
				p.sendMessage(ChatColor.RED+"Usage: /spectate {Playername}");
		}
		
		
		
		
		
		
		
		
		return false;
	
	
	}
}
