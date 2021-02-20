package main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import commads.TestCMD;
import game.GameStateListener;
import game.GameStateManager;
import game.LobbyState;

public class Main extends JavaPlugin{

	public static World mainWorld = Bukkit.getWorld("world");
	
	@Override
	public void onEnable() {
		GameStateManager.setGameState(new LobbyState());
		
		//Reigster Commands
		getCommand("test").setExecutor(new TestCMD());
		
		
		//Reigster Listeners
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new GameStateListener(), this);
		
		System.out.println("FireFight Plugin loaded successfully...");
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
}
