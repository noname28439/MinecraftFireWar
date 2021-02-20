package game;

public abstract class GameState {
	
	abstract int getID();
	
	public abstract void start();
	public abstract void stop();
}
