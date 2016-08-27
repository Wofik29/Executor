package server;

public class Main {
	static boolean isDebug = false;
	public static void main (String[] args)	{
		Game game = new Game(args);
		game.start();
	}
}