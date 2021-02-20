package game;

public class GameStateManager {

	static GameState currentGameState = new LobbyState();
	
	public static GameState getCurrentGameState() {
		return currentGameState;
	}
	
	public static void setGameState(GameState newGameState) {
		currentGameState.stop();
		currentGameState = newGameState;
		currentGameState.start();
		
	}
	
	
}
