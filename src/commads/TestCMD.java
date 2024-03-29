package commads;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import game.BuildState;
import game.GameStateManager;
import game.InGameBuildTools;
import net.md_5.bungee.api.ChatColor;

public class TestCMD implements CommandExecutor {

	static void TravelWorldAddIndex(Player p, int steps) {
		File[] worldFiles = new File(".").listFiles(new FileFilter() {
		    @Override
		    public boolean accept(File file) {
		        return file.isDirectory()&&file.getName().startsWith("InGameWorld");
		    }
		});
		
		List<String> worldList = new ArrayList<String>();
		for(File cf : worldFiles) worldList.add(cf.getName());
		
		
		//List<World> worldList = Bukkit.getServer().getWorlds();
		
		
		int currentWorldIndex = worldList.indexOf(p.getWorld().getName());
		
		int targetWorldIndex = currentWorldIndex+steps;
		if(targetWorldIndex<0)
			targetWorldIndex = worldList.size()+targetWorldIndex;
		if(targetWorldIndex>=worldList.size())
			targetWorldIndex = targetWorldIndex-worldList.size();
		
		p.sendMessage("traveling to World "+String.valueOf(worldList.get(targetWorldIndex))+" [Index: "+String.valueOf(targetWorldIndex)+"/"+worldList.size()+"]");
		loadAndTravelWorld(p, worldList.get(targetWorldIndex));
			
	}
	
	static void travedWorld(Player p, World target) {
		p.setGameMode(GameMode.SPECTATOR);
		p.teleport(new Location(target, 0, 20, 0));
	}
	
	static void loadAndTravelWorld(Player p, String target) {
		World created = new WorldCreator(target).createWorld();
		travedWorld(p, created);
	}
	
	
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
				if(args[0].equalsIgnoreCase("start")) {
					GameStateManager.setGameState(new BuildState());
				}
					
				
				
				
			}else if(args.length==2) {
				if(args[0].equalsIgnoreCase("start")) {
					if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState) {
						BuildState.BuildTimeSec = (int)(Integer.valueOf(args[1])*60);
						GameStateManager.setGameState(new BuildState());
					}else {
						p.sendMessage(ChatColor.RED+"Du kannst diesen Befehl nur in der Lobby ausf�hren!");
					}
				}
			}
		}
		
		if(args.length==2) {
			if(args[0].equalsIgnoreCase("wn")) {
				loadAndTravelWorld(p, args[1]);
			}
		}else if(args.length==1) {
			if(args[0].equalsIgnoreCase("wb"))
				TravelWorldAddIndex(p, -1);
			if(args[0].equalsIgnoreCase("wf"))
				TravelWorldAddIndex(p, 1);
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
