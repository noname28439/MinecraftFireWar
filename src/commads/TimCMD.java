package commads;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(args.length==1) {
				
				p.sendMessage("Na du kleiner! Du hast den Geheimbefehl entdeckt! Du wirst nie wissen was er bewirkt...");
				
			}else
				p.sendMessage(ChatColor.RED+"Usage: /spectate {Playername}");
		}
		
		
		
		
		
		
		
		
		return false;
	
	
	}
}
