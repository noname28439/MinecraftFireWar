package game;

import java.util.Random;

import javax.swing.border.EtchedBorder;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import main.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.IHopper;
import teams.Team;
import teams.TeamManager;

public class GameStateListener implements Listener {

	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState)
			if(!p.isOp())
				e.setCancelled(true);
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
			
		}
			
		if(!BuildState.playerBuildStartPoints.containsKey(p))
			BuildState.playerBuildStartPoints.put(p, null);
		if(!BuildState.playerBuildEndPoints.containsKey(p))
			BuildState.playerBuildEndPoints.put(p, null);
		
		if(p.getInventory().getItemInMainHand()!=null) {
			ItemStack inHand = p.getInventory().getItemInMainHand();
			
			if(inHand.equals(LobbyState.TeamSelector())) {
				if(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK)
				p.openInventory(TeamManager.getSelectionInventory());
				
			}
			if(inHand.getType()==Material.PINK_DYE) {
				if(e.getAction()==Action.RIGHT_CLICK_AIR)
					BuildState.playerBuildStartPoints.put(p, p.getLocation());
				else if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
					BuildState.playerBuildStartPoints.put(p, e.getClickedBlock().getLocation());
				p.sendMessage("StartPos: "+BuildState.playerBuildStartPoints.get(p));
			}
			if(inHand.getType()==Material.MAGENTA_DYE) {
				if(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK)
					if(e.getAction()==Action.RIGHT_CLICK_AIR)
						BuildState.playerBuildEndPoints.put(p, p.getLocation());
					else if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
						BuildState.playerBuildEndPoints.put(p, e.getClickedBlock().getLocation());
				p.sendMessage("EndPos: "+BuildState.playerBuildEndPoints.get(p));
			}
			
		}	
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
				if(e.getClickedBlock().getType()==Material.TNT) {
					if(!FightState.blockDelays.containsKey(e.getClickedBlock())) {
						
						int choice = new Random().nextInt(4);
						
						ItemStack result = new ItemStack(Material.CAKE, 1);
						
						if(choice==0)
							if(new Random().nextInt(2)!=0)
								result = new ItemStack(Material.TRIDENT, 1);
							else
								result = new ItemStack(Material.SNOWBALL, 1);
						if(choice==1)
							if(new Random().nextInt(5)!=0)
								result = new ItemStack(Material.EGG, 1);
							else
								result = new ItemStack(Material.TNT, 1);
						if(choice==2)
							if(new Random().nextInt(2)!=0)
								result = new ItemStack(Material.FEATHER, 1);
							else
								result = new ItemStack(Material.BOOKSHELF, 1);
						if(choice==3)
							result = new ItemStack(Material.FEATHER, 1);
						
						p.getWorld().dropItem(e.getClickedBlock().getLocation().add(0, 1, 0), result);
						
						FightState.blockDelays.put(e.getClickedBlock(), 15);
					}
				
				}
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
			if(e.getClickedBlock().getType()==Material.BOOKSHELF) {
				if(!FightState.blockDelays.containsKey(e.getClickedBlock())) {
					FightState.dropBuildItem(e.getClickedBlock().getLocation().add(0, 1, 0));
					FightState.blockDelays.put(e.getClickedBlock(), 5);
				}
			}
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
				if(e.getClickedBlock().getType()==Material.TARGET) {
					Team team = TeamManager.getPlayerTeam(p);
					Location toSet = e.getClickedBlock().getLocation().clone().add(0, 1, 0);
					team.setRespawnPoint(toSet);
					for(Player cp : team.getTeamPlayers())
						cp.sendMessage("Team RespawnPoint set to ["+toSet.getX()+"|"+toSet.getY()+"|"+toSet.getZ()+"]");
				}
			
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR) {
				if(e.getPlayer().getItemInHand().getType()==Material.FEATHER) {
					int size = 15;
					for(int x = -size; x<size;x++) 
						for(int y = -size; y<size;y++) 
							for(int z = -size; z<size;z++) {
								if(e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().add(x, y, z)).getType()==Material.FIRE)
									e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().add(x, y, z)).setType(Material.GREEN_WOOL);
							}
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
				}
				if(e.getPlayer().getItemInHand().getType()==Material.BLAZE_ROD) {
					int size = 3;
					for(int x = 0; x<3;x++)
						for(int z = 0; z<3; z++) {
							p.getWorld().getBlockAt(p.getLocation().add(x, 0, z).add(-1, -3, -1)).setType(Material.PINK_WOOL);
						}
					
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
				}
			}
			
		}
			
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState)
			if(!p.isOp())
				e.setCancelled(true);
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(e.getPlayer().getItemInHand().getType()==Material.BOW||e.getPlayer().getItemInHand().getType()==Material.ARROW)
				e.setCancelled(true);
		}
			
		
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		
		
		if(TeamManager.getPlayerTeam(p)!=null)
			e.setRespawnLocation(TeamManager.getPlayerTeam(p).getRespawnPoint());
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			FightState.setupPlayer(p);
				if(!FightState.playerLives.containsKey(p.getName())){
					FightState.playerLives.put(p.getName(), FightState.playerHP);
				}
				FightState.playerLives.put(p.getName(), FightState.playerLives.get(p.getName())-1);
				int playerHP = FightState.playerLives.get(p.getName());
				p.sendMessage("Du hast noch "+playerHP+" hp!");
				if(playerHP<=0)
					p.setGameMode(GameMode.SPECTATOR);
				else {
					p.getWorld().getBlockAt(e.getRespawnLocation().clone().add(0, -5, 0)).setType(Material.PINK_WOOL);
				}
				
			}
			
		
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
			if(e.getBlock().getLocation().getBlockZ()<10&&e.getBlock().getLocation().getBlockZ()>-10)
				e.setCancelled(true);
		}
		
		if(GameStateManager.getCurrentGameState().getID()!=GameStateManager.LobbyState)
			if(e.getBlock().getType()==BuildState.toSelfRepairBlockMaterial) {
				BuildState.toSelfRepairBlocks.add(e.getBlock());
			}
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState||GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState)
			if(e.getBlock().getLocation().getBlockY()>BuildState.maxBuildHeight) {
				e.setCancelled(true);
				e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 1);
				p.sendMessage("Hör auf Skybases zu Bauen, du Scheißkind!");
			}
				
		
	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(e.getBlock().getType()==Material.TNT)
				e.getBlock().getLocation().getWorld().createExplosion(e.getBlock().getLocation(), 2, false);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		int flyBorderThickness = 5;
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
			if(p.getLocation().getBlockZ()<flyBorderThickness&&p.getLocation().getBlockZ()>-flyBorderThickness) {
				if(p.getLocation().getBlockZ()<0)
					p.setVelocity(new Vector(0, 0, -1));
				if(p.getLocation().getBlockZ()>0)
					p.setVelocity(new Vector(0, 0, 1));
			}
			
		}
	}
	
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		Entity projectile = e.getEntity();
		
		World world = e.getEntity().getWorld();
		Location loc = projectile.getLocation();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(projectile.getType().equals(EntityType.TRIDENT)) {
				e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 3, true);
				
				for(Player cp : Bukkit.getOnlinePlayers())
					cp.playSound(projectile.getLocation(), Sound.BLOCK_ANVIL_LAND, 15, 1);
				
					double radius = 0.1;
					  int n = 8;
					  for (int i = 0; i < 6; i++) {
					    double angle = 2 * Math.PI * i / n;
					    Location base =
					        loc.clone().add(new Vector(radius * Math.cos(angle), 0, radius * Math.sin(angle)));
					    for (int j = 0; j <= 8; j++) {
					      world.playEffect(base, Effect.SMOKE, j);
					      world.playEffect(base, Effect.MOBSPAWNER_FLAMES, j);
					    }
					  }
			}
				
			if(projectile.getType().equals(EntityType.SNOWBALL))
				projectile.getWorld().strikeLightning(projectile.getLocation());
			
			if(projectile.getType().equals(EntityType.EGG))
				if(e.getHitBlock()!=null)
					e.getHitBlock().setType(Material.FIRE);
			
			if(projectile.getType().equals(EntityType.ARROW)) {
				if(e.getHitBlock()!=null)
					if(new Random().nextInt(5)==0)
						e.getHitBlock().setType(Material.FIRE);
					else if(new Random().nextInt(2)==0)
							e.getHitBlock().setType(Material.AIR);
			}
			e.getEntity().remove();
			
		}
			
		
	}
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(!BuildState.playerBuildStartPoints.containsKey(p))
			BuildState.playerBuildStartPoints.put(p, null);
		if(!BuildState.playerBuildEndPoints.containsKey(p))
			BuildState.playerBuildEndPoints.put(p, null);
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState) {
			//Game currently in LobbyState
			LobbyState.setInventory(p);
			
		}else {
			//Game already started
			p.sendMessage(ChatColor.RED+"Das Spiel hat schon angefangen!");
			p.setGameMode(GameMode.SPECTATOR);
			p.sendMessage(ChatColor.GREEN+"Du kannst dich mit \"/spectate {spielername}\" zu einem anderen Spieler Teleportieren");
		}
		
		
	}
	
	
	
}
