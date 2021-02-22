package commads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.BuildState;
import game.GameStateManager;

public class TestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		Player p = (Player)sender;
		
		p.sendMessage("Test geklappt, du Schluri!");
		
		
		
		
		if(p.isOp())
		if(args.length==1) {
			if(args[0].equalsIgnoreCase("skip"))
				if(GameStateManager.getCurrentGameState().getID()==GameStateManager.BuildState) {
					BuildState state = (BuildState) GameStateManager.getCurrentGameState();
					state.seconds = BuildState.BuildTimeSec-10;
				}
			if(args[0].equalsIgnoreCase("start"))
				GameStateManager.setGameState(new BuildState());
		}
			
					
		
		return false;
	}

}
