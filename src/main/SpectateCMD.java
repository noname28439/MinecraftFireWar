package main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(args.length==1) {
				if(p.getGameMode()==GameMode.SPECTATOR)
					p.teleport(Bukkit.getPlayer(args[0]));
				else
					p.sendMessage(ChatColor.RED+"Du musst im GameMode 3 sein!");
			}else
				p.sendMessage(ChatColor.RED+"Usage: /spectate {Playername}");
		}
			
		
		
		return false;
	}

}
