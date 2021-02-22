package game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import teams.Team;

public class FightState extends GameState{

	
	int SchedulerID;
	public static HashMap<Block, Integer> blockDelays = new HashMap<>();
	public static HashMap<String, Integer> playerLives = new HashMap<>();
	
	public static void setupPlayer(Player cp) {
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemMeta bowMeta = bow.getItemMeta();
		bowMeta.addEnchant(Enchantment.ARROW_INFINITE,1, true);
		bow.setItemMeta(bowMeta);
		cp.getInventory().addItem(bow);
		cp.getInventory().addItem(new ItemStack(Material.ARROW, 1));
		cp.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 10));
		cp.getInventory().addItem(new ItemStack(Material.GRAY_WOOL, 32));
		cp.getInventory().addItem(new ItemStack(Material.FEATHER, 1));
		
		cp.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 2));
	}
	
	@Override
	public void start() {
		
		SchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				for(Block key : blockDelays.keySet()) {
					blockDelays.put(key, blockDelays.get(key)-1);
					if(blockDelays.get(key)<0)
						blockDelays.remove(key);
				}
				
				ArrayList<Team> aliveTeams = new ArrayList<>();
				for(Team ct : Team.values()) {
					boolean hasPlayersAlive = false;
					for(Player cp: ct.getTeamPlayers()) {
						if(cp.getGameMode()==GameMode.SURVIVAL)
							hasPlayersAlive = true;
					}
					if(hasPlayersAlive)
						aliveTeams.add(ct);
				}
				
				if(aliveTeams.size()==1) {
					Bukkit.broadcastMessage("Team "+aliveTeams.get(0).getTeamName()+" hat das Match gewonnen!");
					Bukkit.reload();
				}
					
					
				
			}
		}, 1*20, 1*20);
		
		
		for(Team ct : Team.values()) {
					
			
			for(Player cp: ct.getTeamPlayers()) {
				setupPlayer(cp);
			}
				
		}
		
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public
	int getID() {return GameStateManager.FightState;}
	
}
