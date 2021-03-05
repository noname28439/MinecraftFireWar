package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
	public static final int playerHP = 5;
	
	public static void setupPlayer(Player cp) {
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemMeta bowMeta = bow.getItemMeta();
		bowMeta.addEnchant(Enchantment.ARROW_INFINITE,1, true);
		bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK,3, true);
		bow.setItemMeta(bowMeta);
		cp.getInventory().addItem(bow);
		cp.getInventory().addItem(new ItemStack(Material.ARROW, 1));
		cp.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 10));
		cp.getInventory().addItem(new ItemStack(Material.GRAY_WOOL, 32));
		cp.getInventory().addItem(new ItemStack(Material.FEATHER, 1));
		
		cp.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 2));
	}
	
	public static void dropBuildItem(Location dropLoc) {

		int choice = new Random().nextInt(16);
		
		ItemStack result = new ItemStack(Material.LAVA_BUCKET, 1);
		
		if(choice==0)
			result = new ItemStack(Material.SLIME_BALL, new Random().nextInt(3)+1);
		if(choice==1)
			result = new ItemStack(Material.REDSTONE, new Random().nextInt(5)+1);
		if(choice==2)
			result = new ItemStack(Material.PINK_WOOL, new Random().nextInt(4)+1);
		if(choice==3)
			result = new ItemStack(Material.DARK_OAK_BUTTON, new Random().nextInt(4)+1);
		if(choice==4)
			result = new ItemStack(Material.CRIMSON_DOOR, new Random().nextInt(2)+1);
		if(choice==5)
			result = new ItemStack(Material.WARPED_PRESSURE_PLATE, new Random().nextInt(2)+1);
		if(choice==6)
			result = new ItemStack(Material.LEVER, new Random().nextInt(2)+1);
		if(choice==7)
			result = new ItemStack(Material.REPEATER, new Random().nextInt(2)+1);
		if(choice==8)
			result = new ItemStack(Material.OBSERVER, new Random().nextInt(2)+1);
		if(choice==9)
			result = new ItemStack(Material.DISPENSER, new Random().nextInt(2)+1);
		if(choice==10)
			result = new ItemStack(Material.SCAFFOLDING, new Random().nextInt(6)+1);
		if(choice==11)
			result = new ItemStack(Material.PINK_WOOL, new Random().nextInt(4)+1);
		if(choice==12)
			result = new ItemStack(Material.PINK_WOOL, new Random().nextInt(4)+1);
		if(choice==13)
			result = new ItemStack(Material.PINK_WOOL, new Random().nextInt(4)+1);
		if(choice==13)
			result = new ItemStack(Material.CHICKEN_SPAWN_EGG, new Random().nextInt(2)+1);
		if(choice==13)
			result = new ItemStack(Material.WHEAT_SEEDS, new Random().nextInt(9)+1);
		if(choice==14)
			result = new ItemStack(Material.ENDER_PEARL, new Random().nextInt(1));
		if(choice==15)
			result = new ItemStack(Material.BLAZE_ROD, 1);
		
		dropLoc.getWorld().dropItem(dropLoc, result);
		
	}
	
	@Override
	public void start() {
		
		SchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				BuildState.buildStateWorld.setGameRuleValue("doFireTick", "true");
				
				for(Block key : blockDelays.keySet()) {
					if(!blockDelays.containsKey(key))
						break;
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
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				ArrayList<Block> toReplaceBlocks = new ArrayList<>();
				for(Block cb : BuildState.toSelfRepairBlocks) {
					boolean hasNeighbours = false;
					for(int x = 0; x<3;x++)
						for(int y = 0; y<3;y++)
							for(int z = 0; z<3;z++) {
								Location lookupLocation = cb.getLocation().add(x-1, y-1, z-1);
								if(cb.getWorld().getBlockAt(lookupLocation).getType()!=Material.AIR)
									hasNeighbours = true;
							}
					
					if(hasNeighbours)
						toReplaceBlocks.add(cb);
				}
				
				for(Block cb : toReplaceBlocks)
					cb.setType(BuildState.toSelfRepairBlockMaterial);
				
			}
		}, 1*20, 5*20);
		
		
		for(Team ct : Team.values()) {
					
			
			for(Player cp: ct.getTeamPlayers()) {
				setupPlayer(cp);
				cp.setAllowFlight(false);
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
