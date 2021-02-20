package game;

public class GameStateManager {

	static GameState currentGameState;
	
	public static final int
	LobbyState = 0, 
	BuildState = 1,
	FightState = 2;
	
	public static GameState getCurrentGameState() {
		return currentGameState;
	}
	
	
	public static void setGameState(GameState newGameState) {
		
		if(currentGameState!=null)
			currentGameState.stop();
		currentGameState = newGameState;
		if(currentGameState!=null)
			currentGameState.start();
		
	}
	
	
}
