package game;

import java.sql.Timestamp;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;

import main.Main;
import teams.Team;
import teams.TeamManager;

public class BuildState extends GameState{

	static World buildStateWorld;
	
	static final int BaseDistance = 20;
	
	static int SchedulerID;
	static int seconds = 0;
	static final int BuildTimeSec = (int)(5*60);
	
	public static String currentTime() {
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		return "["+timeStamp.getHours()+"-"+timeStamp.getMinutes()+"-"+timeStamp.getSeconds()+"]";
	}
	
	static void givePlayerRandomBuildItem(Player p) {
		ItemStack toAdd = new ItemStack(TeamManager.getPlayerTeam(p).getButtonMaterial(), new Random().nextInt(5)+1);
		
		int choice = new Random().nextInt(25);
		
		if(choice==0)
			toAdd = new ItemStack(Material.LADDER, 1);
		if(choice==1)
			toAdd = new ItemStack(Material.LADDER, 1);
		if(choice==2)
			toAdd = new ItemStack(Material.JUNGLE_WOOD, new Random().nextInt(2)+1);
		if(choice==3)
			toAdd = new ItemStack(Material.ACACIA_STAIRS, 1);
		if(choice==4)
			toAdd = new ItemStack(Material.BLACK_BANNER, 1);
		if(choice==5)
			toAdd = new ItemStack(Material.DARK_OAK_TRAPDOOR, 1);
		if(choice==6)
			toAdd = new ItemStack(Material.DARK_OAK_FENCE_GATE, 1);
		if(choice==7)
			toAdd = new ItemStack(Material.DARK_OAK_DOOR, 1);
		if(choice==8)
			toAdd = new ItemStack(Material.LADDER, 1);
		if(choice==9)
			if(new Random().nextInt(5)==0)
				toAdd = new ItemStack(Material.TNT, 1);
		
		if(choice==15)
			toAdd = new ItemStack(Material.SAND, 1);
		if(choice==16)
			toAdd = new ItemStack(Material.SAND, 1);
		if(choice==17)
			toAdd = new ItemStack(Material.GRAVEL, 1);
		if(choice==18)
			if(new Random().nextInt(2)==0)
				toAdd = new ItemStack(Material.ANVIL, 1);
		
		if(choice==9)
			if(new Random().nextInt(5)==0)
				toAdd = new ItemStack(Material.TNT, 1);
		
		
		p.getInventory().addItem(toAdd);
	}
	
	@Override
	public void start() {
		
		//Start Scheduler
		SchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				seconds++;
				//Bukkit.broadcastMessage("tick! ["+seconds+"]");
				
				for(Player cp : Bukkit.getOnlinePlayers())
					if(TeamManager.getPlayerTeam(cp)!=null)
						givePlayerRandomBuildItem(cp);
				
				switch (seconds) {
				case 1:
					Bukkit.broadcastMessage("Fangt an, euch f�r den Kampf vorzubereiten!");
					break;
					
				case BuildTimeSec-30: case BuildTimeSec-60: case BuildTimeSec-15:
					Bukkit.broadcastMessage("Noch "+(BuildTimeSec-seconds)+" Sekunden!");
					break;

				case BuildTimeSec:
					Bukkit.broadcastMessage("Der Kampf beginnt!");
					break;
				}
				
				if(seconds>BuildTimeSec) {
					GameStateManager.setGameState(new FightState());
				}
				
			}
		}, 1*20, 1*20);
		
		
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String worldName = "InGameWorld."+ts.getYear()+"."+ts.getMonth()+"."+ts.getDay()+"."+ts.getHours()+"."+ts.getMinutes();
		//Generate new GameWorld
		WorldCreator wc = new WorldCreator(worldName);
		wc.generator(new VoidChunkGenerator());
		System.out.println("World settings built!");
		buildStateWorld = Bukkit.createWorld(wc);
		System.out.println("World created!");
		while(Bukkit.getWorld(worldName)==null) {
			System.out.println("World not accessable!");
			try { Thread.currentThread().sleep(100); } catch (InterruptedException e) {}
		}
		System.out.println("Setting up World!");
		Bukkit.getWorld(worldName).getBlockAt(0, 10, 0).setType(Material.COBWEB);
		System.out.println("Teleporting Players...");
		for(Player cp : Bukkit.getOnlinePlayers())
			cp.teleport(new Location(Bukkit.getWorld(worldName), 0, 100, 0));
		
		
		int zDistance = BaseDistance;
		for(Team ct : Team.values()) {
			Location spawn = new Location(Bukkit.getWorld(worldName), 0, 15, zDistance);
			ct.setRespawnPoint(spawn);
			zDistance=-zDistance;
			//Base creation
			World toSet = Bukkit.getWorld(worldName);
			for(int x = 0; x<3;x++)
				for(int z = 0; z<3; z++) {
					toSet.getBlockAt(new Location(Bukkit.getWorld(worldName), 0, 10, zDistance).add(x, 0, z).add(-1, 0, -1)).setType(Material.WHITE_WOOL);
				}
					
			
			for(Player cp: ct.getTeamPlayers()) {
				cp.setGameMode(GameMode.SURVIVAL);
				cp.teleport(spawn);
				cp.getInventory().clear();
			}
				
		}
			
		
		
		
	}

	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(SchedulerID);
		for(Player cp : Bukkit.getOnlinePlayers())
			cp.getInventory().clear();
		
	}

	@Override
	int getID() {return GameStateManager.BuildState;}
	
}

class VoidChunkGenerator extends ChunkGenerator {
	
	
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunkData = super.createChunkData(world);
		
		// Set biome.
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				biome.setBiome(x, z, Biome.PLAINS);
			}
		}
		
		// Return the new chunk data.
		return chunkData;
	}
	
	@Override
	public boolean canSpawn(World world, int x, int z) {
		return true;
	}
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		return new Location(world, 0, 100, 0);
	}
}