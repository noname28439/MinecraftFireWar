package main;

import org.bukkit.plugin.java.JavaPlugin;

import commads.TestCMD;

public class Main extends JavaPlugin{

	@Override
	public void onEnable() {
		getCommand("test").setExecutor(new TestCMD());
		
		System.out.println("FireFight Plugin loaded successfully...");
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
}
