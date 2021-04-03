package main;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import commads.SpectateCMD;
import commads.TestCMD;
import commads.TimCMD;
import commads.BuildCMD;
import commads.FlightCMD;
import game.BuildState;
import game.GameStateListener;
import game.GameStateManager;
import game.InGameBuildTools;
import game.LobbyState;
import teams.TeamListener;

public class Main extends JavaPlugin{

	public static World mainWorld = Bukkit.getWorld("world");
	
	public static String PREFIX = "§7[§cFireFight§7] ";
	
	public static JavaPlugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		GameStateManager.setGameState(new LobbyState());
		
		
		
		
		//Reigster Commands
		getCommand("test").setExecutor(new TestCMD());
		getCommand("spectate").setExecutor(new SpectateCMD());
		getCommand("geheimbefehl").setExecutor(new TimCMD());
		getCommand("build").setExecutor(new BuildCMD());
		getCommand("fly").setExecutor(new FlightCMD());
		
		//Reigster Listeners
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new GameStateListener(), this);
		pm.registerEvents(new TeamListener(), this);
		
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				//Bukkit.broadcastMessage("mainTick");
				for(Player cp : Bukkit.getOnlinePlayers()) {
					//Bukkit.broadcastMessage("tick "+cp.getName());
					if(BuildState.playerBuildStartPoints.get(cp)==null||BuildState.playerBuildEndPoints.get(cp)==null)
						continue;
					//Bukkit.broadcastMessage("DrawTick "+cp.getName());
					InGameBuildTools.markArea(cp, BuildState.playerBuildStartPoints.get(cp), BuildState.playerBuildEndPoints.get(cp), 1);
					//Bukkit.broadcastMessage("DrawSuccess "+cp.getName());
				}
				
			}
		}, 20, 2*20);
		
		System.out.println("FireFight Plugin loaded successfully...");
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
}
