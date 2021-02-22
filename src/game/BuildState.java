package game;

import java.sql.Timestamp;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import teams.Team;

public class BuildState extends GameState{

	static World buildStateWorld;
	
	static int BaseDistance = 30;
	
	public static String currentTime() {
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		return "["+timeStamp.getHours()+"-"+timeStamp.getMinutes()+"-"+timeStamp.getSeconds()+"]";
	}
	
	@Override
	public void start() {
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
		Bukkit.getWorld(worldName).getBlockAt(0, 10, 0).setType(Material.SLIME_BLOCK);
		System.out.println("Teleporting Players...");
		for(Player cp : Bukkit.getOnlinePlayers())
			cp.teleport(new Location(Bukkit.getWorld(worldName), 0, 100, 0));
		
		
		int zDistance = BaseDistance;
		for(Team ct : Team.values()) {
			Location spawn = new Location(Bukkit.getWorld(worldName), 0, 11, zDistance);
			zDistance=-zDistance;
			//Base creation
			World toSet = Bukkit.getWorld(worldName);
			for(int x = 0; x<3;x++)
				for(int z = 0; z<3; z++) {
					toSet.getBlockAt(new Location(Bukkit.getWorld(worldName), 0, 10, zDistance).add(x, 0, z).add(-1, 0, -1)).setType(Material.WHITE_WOOL);
				}
					
			
			for(Player cp: ct.getTeamPlayers()) {
				cp.teleport(spawn);
				cp.setBedSpawnLocation(spawn);
			}
				
		}
			
		
		
		
	}

	@Override
	public void stop() {
		
		
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