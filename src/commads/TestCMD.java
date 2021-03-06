package commads;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.BuildState;
import game.GameStateManager;
import game.InGameBuildTools;

public class TestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		Player p = (Player)sender;
		
		
		
		
		
		
		if(p.isOp()) {
			if(args.length==1) {
				if(args[0].equalsIgnoreCase("skip"))
					if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
						BuildState state = (BuildState) GameStateManager.getCurrentGameState();
						state.seconds = BuildState.BuildTimeSec-10;
					}
				if(args[0].equalsIgnoreCase("start"))
					GameStateManager.setGameState(new BuildState());
				
				
				
			}
		}
		
		if(args.length==2) {
			if(args[0].equalsIgnoreCase("vv")) {
				p.setGameMode(GameMode.SPECTATOR);
				p.teleport(new Location(Bukkit.getWorld(args[1]), 0, 100, 0));
			}
		}
		
		if(args.length==4) {
			if(args[0].equalsIgnoreCase("buildRect")) {
				int x = Integer.valueOf(args[1]);
				int y = Integer.valueOf(args[2]);
				int z = Integer.valueOf(args[3]);
				
				InGameBuildTools.fillRect(p, p.getLocation(), new Location(p.getWorld(), x, y, z));
				
			}
			if(args[0].equalsIgnoreCase("markArea")) {
				int x = Integer.valueOf(args[1]);
				int y = Integer.valueOf(args[2]);
				int z = Integer.valueOf(args[3]);
				
				InGameBuildTools.markArea(p, p.getLocation(), new Location(p.getWorld(), x, y, z), 5);
				
			}
		}
			
					
		
		return false;
	}

}
