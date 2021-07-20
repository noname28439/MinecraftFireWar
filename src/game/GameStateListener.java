package game;

import java.util.ArrayList;
import java.util.Random;

import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import main.Main;
import net.md_5.bungee.api.ChatColor;
import settings.Settings;
import teams.Team;
import teams.TeamManager;

public class GameStateListener implements Listener {
	
	
	@EventHandler
	public void OnPlayerDamage(EntityDamageByEntityEvent e) {
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState) {
			if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
	            Player whoWasHit = (Player) e.getEntity();
	            Player whoHit = (Player) e.getDamager();
	            whoHit.sendMessage("Du hast "+e.getDamage()+" Damage gemacht!");
	            
	            if(whoWasHit.getPotionEffect(PotionEffectType.LUCK)!=null)
					if(whoWasHit.getPotionEffect(PotionEffectType.LUCK).getAmplifier()==200)
						e.setCancelled(true);
	            
	            if(Main.isHunter(whoWasHit.getName())) {
	            	if(!whoWasHit.isBlocking()) {
//	            		whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 255));
			            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3*20, 200));
			            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 5*20, 200));
			            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3*20, 200));
	            	}
	            	
	            }
	            
	            
	        }
			if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
	            Player whoWasHit = (Player) e.getEntity();
	            Arrow whoHit = (Arrow) e.getDamager();
	            
	            if(whoWasHit.getPotionEffect(PotionEffectType.LUCK)!=null)
					if(whoWasHit.getPotionEffect(PotionEffectType.LUCK).getAmplifier()==200)
						e.setCancelled(true);
	            
	            if(Main.isHunter(whoWasHit.getName())) {
	            	if(!whoWasHit.isBlocking()) {
//	            		whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 255));
			            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3*20, 200));
			            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 5*20, 200));
			            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3*20, 200));
	            	}
	            	
	            }
	            
	            
	        }
			
			
		}
	}
	
	@EventHandler
	public void OnPlayerMove(PlayerMoveEvent e) {
		
		//e.getPlayer().sendMessage(""+e.getPlayer().getWalkSpeed());
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState && e.getPlayer().getWorld().getName()=="world") {
			Player nearensEnemy = null;
			double nearestDist = Double.MAX_VALUE;
			for(Player cp : Bukkit.getOnlinePlayers()) {
				double dist = e.getPlayer().getLocation().distance(cp.getLocation());
				if(dist<nearestDist) {
					nearestDist = dist;
					nearensEnemy = cp;
				}
			}
			if(nearensEnemy!=null)
			e.getPlayer().setCompassTarget(nearensEnemy.getLocation());
			
			Location movedFrom = e.getFrom();
	        Location movedTo = e.getTo();
	        if ((movedFrom.getX() != movedTo.getX() || movedFrom.getY() != movedTo.getY() || movedFrom.getZ() != movedTo.getZ()) && e.getPlayer().isOnGround()) {
					if(e.getPlayer().getPotionEffect(PotionEffectType.FIRE_RESISTANCE)!=null) {
						if(e.getPlayer().getPotionEffect(PotionEffectType.FIRE_RESISTANCE).getAmplifier()==200) {
							//e.setTo(new Location(e.getPlayer().getWorld(),movedFrom.getX() ,movedFrom.getY(), movedFrom.getZ(), e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch()));
							e.getPlayer().setWalkSpeed(0f);
							
						}	
					}else {
						e.getPlayer().setWalkSpeed(0.2f);
					}
					
							
	        }
	        if(Main.isHunter(e.getPlayer().getName())) {
	        	if(new Random().nextInt(1000)==0)
	        		e.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_EYE, 1));
	        }else {
	        	
	        }
	        
	        if(new Random().nextInt(500)==0) {
        		Location dropLoc = e.getPlayer().getLocation();
        		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						if(new Random().nextInt(4)==0)
							e.getPlayer().getWorld().dropItem(dropLoc, new ItemStack(Material.ENDER_PEARL));
						else
							e.getPlayer().getWorld().dropItem(dropLoc, new ItemStack(Material.ARROW));
						
					}
				}, (new Random().nextInt(20)+20)*20);
        	}
	        
		}
		
		
		
	}
	
	@EventHandler
	public void OnPlayerDamageHanging(HangingBreakByEntityEvent  e) {
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState) {
			if(e.getEntity() instanceof Painting)
				e.setCancelled(true);
		
			if (e.getRemover() instanceof Player) {
				Player remover = (Player) e.getRemover();
	            if(remover.getGameMode()==GameMode.CREATIVE)
	            	e.setCancelled(false);
	        }
		}
	}
	
	
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		
		
		
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState) {
			if(e.getClickedBlock()!=null) {
				if(e.getClickedBlock().getType().toString().toLowerCase().contains("warped"))
					e.setCancelled(true);
				if(Main.isHunter(p.getName())|| p.getGameMode()==GameMode.CREATIVE) {
					e.setCancelled(false);
				}
			}
			if(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK)
			if(e.getPlayer().getItemInHand()!=null)
				if(e.getPlayer().getItemInHand().getType()==Material.ENDER_EYE) {
					if(Main.isHunter(e.getPlayer().getName())) {
						e.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
							
							@Override
							public void run() {
								e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 255));
							}
						}, 20*20);
						e.setCancelled(true);
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
					}
				}
					
			
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
			if(inHand.getType()==Material.NETHER_STAR) {
				e.setCancelled(true);
				if(e.getAction()==Action.RIGHT_CLICK_AIR)
					BuildState.playerBuildStartPoints.put(p, p.getLocation());
				else if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
					BuildState.playerBuildStartPoints.put(p, e.getClickedBlock().getLocation());
				
				if(e.getAction()==Action.LEFT_CLICK_AIR)
					BuildState.playerBuildEndPoints.put(p, p.getLocation());
				else if(e.getAction()==Action.LEFT_CLICK_BLOCK)
					BuildState.playerBuildEndPoints.put(p, e.getClickedBlock().getLocation());
			
				if(BuildState.playerBuildEndPoints.get(p)!=null)
					p.getWorld().spawnParticle(Particle.HEART, BuildState.playerBuildEndPoints.get(p), 0);
				if(BuildState.playerBuildStartPoints.get(p)!=null)
					p.getWorld().spawnParticle(Particle.HEART, BuildState.playerBuildStartPoints.get(p), 0);
				
				if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
					if(BuildState.playerBuildStartPoints.get(p)!=null)
						if(BuildState.playerBuildStartPoints.get(p).getY()>BuildState.maxBuildHeight) {
							Location newPos = BuildState.playerBuildStartPoints.get(p);
							newPos.setY(BuildState.maxBuildHeight);
							BuildState.playerBuildStartPoints.put(p, newPos);
						}
					if(BuildState.playerBuildEndPoints.get(p)!=null)
						if(BuildState.playerBuildEndPoints.get(p).getY()>BuildState.maxBuildHeight) {
							Location newPos = BuildState.playerBuildEndPoints.get(p);
							newPos.setY(BuildState.maxBuildHeight);
							BuildState.playerBuildEndPoints.put(p, newPos);
						}
							
				}
				
			}
			
		}	
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
				if(e.getClickedBlock().getType()==Material.TNT) {
					if(!FightState.blockDelays.containsKey(e.getClickedBlock())) {
						
						int choice = new Random().nextInt(5);
						
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
						if(choice==4)
							if(new Random().nextInt(2)==0)
								result = new ItemStack(Material.HEART_OF_THE_SEA, 1);
							else
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
				
				if(e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getType() == Material.CAULDRON) {
					if(e.getPlayer().getItemInHand().getType()==Material.ARROW) {
						if(e.getPlayer().getFoodLevel()>1) {
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 100));
							e.getPlayer().launchProjectile(Arrow.class);
							if(new Random().nextInt(2)==0)
								e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel()-1);
						}
						
					}
				}
				
				if(e.getPlayer().getItemInHand().getType()==Material.HEART_OF_THE_SEA) {
					
					Team targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
					while(targetTeam==TeamManager.getPlayerTeam(p))
						targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
					
					Player targetPlayer = targetTeam.getTeamPlayers().get(new Random().nextInt(targetTeam.getTeamPlayers().size()));
					while(targetPlayer.getGameMode()!=GameMode.SURVIVAL)
						targetPlayer = targetTeam.getTeamPlayers().get(new Random().nextInt(targetTeam.getTeamPlayers().size()));
					
					p.sendMessage("Player: "+targetPlayer);
					
					Location toBomb = targetPlayer.getLocation();
					
					for(int i = 0; i<20;i++) {
						Location in = toBomb.clone().add(new Random().nextInt(10)-5,BuildState.maxBuildHeight+20, new Random().nextInt(10)-5);
						toBomb.getWorld().spawnEntity(in, EntityType.SNOWBALL);
					}
					
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
				}	
				
				if(e.getPlayer().getItemInHand().getType()==Material.FIRE_CHARGE) {
					e.getPlayer().launchProjectile(Fireball.class);
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
				if(!Settings.KeepInventory)
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
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
			if(!Settings.KeepInventory)
				p.getInventory().addItem(new ItemStack(Material.NETHER_STAR));
		}
			
		
	}
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e) {
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			Player dead = e.getEntity();
			if(dead.getKiller()!=null) {
				Player killer = dead.getKiller();
				killer.getInventory().addItem(new ItemStack(Material.TNT));
			}
		}
	}
	
	
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState)
			if(!p.isOp())
				e.setCancelled(true);
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
			if(e.getBlock().getLocation().getBlockZ()<10&&e.getBlock().getLocation().getBlockZ()>-10)
				e.setCancelled(true);
			if(e.getBlock().getLocation().getBlockX()>Settings.maxBuildWidth||e.getBlock().getLocation().getBlockX()<-Settings.maxBuildWidth)
				e.setCancelled(true);
		}
		
		if(GameStateManager.getCurrentGameState().getID()!=GameStateManager.LobbyState)
			if(e.getBlock().getType()==BuildState.toSelfRepairBlockMaterial) {
				BuildState.toSelfRepairBlocks.add(e.getBlock());
			}
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState||GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState)
			if(e.getBlock().getLocation().getBlockY()>BuildState.maxBuildHeight) {
				e.setCancelled(true);
				//e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 1);
				p.sendMessage("Hör auf Skybases zu Bauen, du Scheißkind!");
			}
				
		
	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.LobbyState)
			if(!p.isOp())
				e.setCancelled(true);
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.FightState) {
			if(e.getBlock().getType()==Material.TNT)
				e.getBlock().getLocation().getWorld().createExplosion(e.getBlock().getLocation(), 2, false);
		}
		
		if(e.getBlock().getType()==BuildState.toSelfRepairBlockMaterial)
			if(BuildState.toSelfRepairBlocks.contains(e.getBlock()))
				BuildState.toSelfRepairBlocks.remove(e.getBlock());
		
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
			
			if(projectile.getType().equals(EntityType.FIREBALL))
				if(e.getHitBlock()!=null){
					Blaze blaze = (Blaze)e.getHitBlock().getWorld().spawnEntity(e.getHitBlock().getLocation(), EntityType.BLAZE);
					blaze.setGlowing(true);
					blaze.setHealth(1);
				}
			
			if(projectile.getType().equals(EntityType.ARROW)) {
				if(e.getHitBlock()!=null) {
					if(e.getHitBlock().getType()==Material.CAULDRON) {
						e.getHitBlock().setType(Material.LAVA);
						e.getHitBlock().getWorld().createExplosion(projectile.getLocation(), 10, true);
					}
						
					if(new Random().nextInt(5)==0)
						e.getHitBlock().setType(Material.FIRE);
					else if(new Random().nextInt(2)==0)
						e.getHitBlock().setType(Material.AIR);
				}	
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
