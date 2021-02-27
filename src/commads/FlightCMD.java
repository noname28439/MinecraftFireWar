package commads;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.Main;

public class FlightCMD implements CommandExecutor {

	private int oft = 0;
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		
			
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(args.length==0) {
					
				boolean fly = p.getAllowFlight();
				
				if (fly == true) {
					
					p.setAllowFlight(false);
					p.sendMessage(Main.PREFIX + "Du kannst nun nicht mehr fliegen.");
						
				} if (fly == false) {
					
					p.setAllowFlight(true);
					p.sendMessage(Main.PREFIX + "Du kannst nun fliegen.");
					
				}
				
		
			}else
					
					p.sendMessage(ChatColor.RED+"Usage: /spectate {Playername}");
		}
		
		
		 
			
			
		
		return false;

	}	

}