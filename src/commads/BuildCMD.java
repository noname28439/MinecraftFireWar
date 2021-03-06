package commads;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.BuildState;
import game.GameStateManager;
import game.InGameBuildTools;

public class BuildCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		Player p = (Player)sender;
		
		if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState || p.isOp()) {
			
			if(BuildState.playerBuildStartPoints.get(p)==null||BuildState.playerBuildEndPoints.get(p)==null) {
				p.sendMessage(ChatColor.RED+"Du musst erst einen Bereich markieren!");
				return false;
			}
			
			boolean finished = InGameBuildTools.fillRect(p, BuildState.playerBuildStartPoints.get(p), BuildState.playerBuildEndPoints.get(p));
			if(finished) {
				BuildState.playerBuildStartPoints.put(p, null);
				BuildState.playerBuildEndPoints.put(p, null);
				p.sendMessage(ChatColor.GREEN+"Build finished!");
			}
			
		}else {
			p.sendMessage(ChatColor.RED+"Du kannst diesen Befehl nur in der Bauphase einsetzen!");
		}
			

		
		return false;
	}

}
