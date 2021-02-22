package game;

import java.util.Random;

import javax.swing.border.EtchedBorder;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.IHopper;
import teams.TeamManager;

public class GameStateListener implements Listener {

	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState)
			e.setCancelled(true);
		
		if(p.getInventory().getItemInMainHand()!=null) {
			ItemStack inHand = p.getInventory().getItemInMainHand();
			
			if(inHand.equals(LobbyState.TeamSelector())) {
				if(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK)
				p.openInventory(TeamManager.getSelectionInventory());
				
			}
			
		}	
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
				if(e.getClickedBlock().getType()==Material.TNT) {
					if(!FightState.blockDelays.containsKey(e.getClickedBlock())) {
						
						int choice = new Random().nextInt(3);
						
						if(choice==0)
							e.getPlayer().getInventory().addItem(new ItemStack(Material.TRIDENT, 1));
						if(choice==1)
							e.getPlayer().getInventory().addItem(new ItemStack(Material.EGG, 1));
						if(choice==2)
							e.getPlayer().getInventory().addItem(new ItemStack(Material.FEATHER, 1));
						
						FightState.blockDelays.put(e.getClickedBlock(), 10);
					}
						
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
			}
		}
			
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState)
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
					FightState.playerLives.put(p.getName(), 3);
				}
				FightState.playerLives.put(p.getName(), FightState.playerLives.get(p.getName())-1);
				int playerHP = FightState.playerLives.get(p.getName());
				p.sendMessage("Du hast noch "+playerHP+" hp!");
				if(playerHP<=0)
					p.setGameMode(GameMode.SPECTATOR);
				else {
					p.getWorld().getBlockAt(e.getRespawnLocation().add(0, -5, 0)).setType(Material.PINK_WOOL);
				}
				
			}
			
		
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState)
			if(e.getBlock().getLocation().getBlockZ()<10&&e.getBlock().getLocation().getBlockZ()>-10)
				e.setCancelled(true);
		
	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState)
			if(e.getBlock().getType()==Material.TNT)
				e.getBlock().getLocation().getWorld().createExplosion(e.getBlock().getLocation(), 2, false);
		
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		Entity projectile = e.getEntity();
		
		
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(projectile.getType().equals(EntityType.TRIDENT))
				e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 3, true);
			
			if(projectile.getType().equals(EntityType.EGG))
				if(e.getHitBlock()!=null)
					e.getHitBlock().setType(Material.FIRE);
			
			if(projectile.getType().equals(EntityType.ARROW)) {
				e.getEntity().remove();
				if(e.getHitBlock()!=null)
					if(new Random().nextInt(10)==0)
						e.getHitBlock().setType(Material.FIRE);
					else
						e.getHitBlock().setType(Material.AIR);
			}
			
		}
			
		
	}
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState) {
			//Game currently in LobbyState
			LobbyState.setInventory(p);
			
		}else {
			//Game already started
			p.sendMessage(ChatColor.RED+"Das Spiel hat schon angefangen!");
		}
		
		
	}
	
	
	
}
