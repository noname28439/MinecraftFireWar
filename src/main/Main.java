package main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import commads.SpectateCMD;
import commads.TestCMD;
import commads.TimCMD;
import commads.FlightCMD;
import game.GameStateListener;
import game.GameStateManager;
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
		getCommand("fly").setExecutor(new FlightCMD());
		
		//Reigster Listeners
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new GameStateListener(), this);
		pm.registerEvents(new TeamListener(), this);
		
		System.out.println("FireFight Plugin loaded successfully...");
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
}
