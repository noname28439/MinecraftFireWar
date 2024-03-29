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
import settings.Settings;
import teams.Team;

public class FightState extends GameState{

	
	int SchedulerID;
	public static HashMap<Block, Integer> blockDelays = new HashMap<>();
	public static HashMap<String, Integer> playerLives = new HashMap<>();
	public static final int playerHP = Settings.playerHP;
	
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

		Random random = new Random();
		
		ItemStack[] lootTable = new ItemStack[] {
				new ItemStack(Material.SLIME_BALL, random.nextInt(3)+1),
				new ItemStack(Material.REDSTONE, random.nextInt(5)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.DARK_OAK_BUTTON, random.nextInt(4)+1),
				new ItemStack(Material.CRIMSON_DOOR, random.nextInt(2)+1),
				new ItemStack(Material.WARPED_PRESSURE_PLATE, random.nextInt(2)+1),
				new ItemStack(Material.LEVER, random.nextInt(2)+1),
				new ItemStack(Material.REPEATER, random.nextInt(2)+1),
				new ItemStack(Material.OBSERVER, random.nextInt(2)+1),
				new ItemStack(Material.DISPENSER, random.nextInt(2)+1),
				new ItemStack(Material.SCAFFOLDING, random.nextInt(6)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.CHICKEN_SPAWN_EGG, random.nextInt(2)+1),
				new ItemStack(Material.WHEAT_SEEDS, random.nextInt(9)+1),
				new ItemStack(Material.ENDER_PEARL, 1),
				new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),
				new ItemStack(Material.BLAZE_ROD, 1),
				new ItemStack(Material.SHIELD, 1),
				new ItemStack(Material.OBSIDIAN, 1),
				new ItemStack(Material.BELL, random.nextInt(2)+1),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.DRAGON_HEAD, 1),
				new ItemStack(Material.DIAMOND, random.nextInt(2)+1),
				new ItemStack(Material.COBWEB, 1),
				new ItemStack(Material.CAULDRON, 1),
				new ItemStack(Material.BIRCH_BOAT, 1),
				new ItemStack(Material.MINECART, 1),
				new ItemStack(Material.CHEST, 1),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.DIAMOND_SWORD, 1),
				new ItemStack(Material.SPECTRAL_ARROW, random.nextInt(2)+1),
				new ItemStack(Material.FEATHER, random.nextInt(2)+1),
				new ItemStack(Material.FEATHER, random.nextInt(2)+1),
				new ItemStack(Material.FEATHER, random.nextInt(2)+1),
				new ItemStack(Material.RAIL, random.nextInt(5)+2),
				new ItemStack(Material.POWERED_RAIL, random.nextInt(1)+2),
				new ItemStack(Material.BREAD, random.nextInt(2)+3),
				new ItemStack(Material.BREAD, random.nextInt(2)+3),
				new ItemStack(Material.BREAD, random.nextInt(2)+3),
				new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),
				new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),
				new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),
				new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),
				new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),
				new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.FIRE_CHARGE, 1),
				};
		
		
		ItemStack result = lootTable[new Random().nextInt(lootTable.length)];
		
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
					BuildState.buildStateWorld.setGameRuleValue("doFireTick", "false");
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
					if(cb.getType()==Material.AIR || cb.getType()==Material.FIRE)
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
