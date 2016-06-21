package server;

import java.awt.Point;
import java.util.HashMap;
import other.*;

public class World {
	//private volatile List<Player> objects = new ArrayList<>();
	private volatile HashMap<String, Player> objects = new HashMap<>();
	private Game game;
	private boolean isAllReady = false;
	public static volatile byte[][] map = null;
	private int count_players = 0; 
	
	public World(Game g) {
		game = g;
	}
	
	private byte[][] copyMap(byte[][] m) {
		byte [][] map;
		map = new byte[m.length][];
		for (int i=0; i< m.length; i++) {
			map[i] = new byte[m[i].length];
			for (int j=0; j<m[i].length; j++) {
				map[i][j] = m[i][j];
			}
		}
		return map;
	}
	
	public void setMap(byte[][] m) {
		map = copyMap(m);
		Point jew = new Point(-1,-1);
		for (int i=0; i < m.length; i++) {
			for (int k=0; k < m[i].length; k++)	{
				if (m[i][k] == 41) {
					jew.x = i;
					jew.y = k;
				}
				map[i][k] = m[i][k];
			}
		}
		/*
		map[jew.x+1][jew.y+1] = Map.JEWEL;
		map[jew.x-1][jew.y+1] = Map.JEWEL;
		map[jew.x+1][jew.y-1] = Map.JEWEL;
		map[jew.x-1][jew.y-1] = Map.JEWEL;
		*/
	}
	
	public Player addPlayer(String name) {
		if (objects.containsKey(name)) return null;
		Player p;
		switch (objects.size())	{
		case 0:
			p = new Player(15,14);
			break;
		case 1:
			p = new Player(17,7);
			p.setRotation(270);
		default:
			p = new Player(17,7);
			p.setRotation(270);
			break;
		}
		count_players++;
		p.setName(name);
		p.setMap();
		objects.put(p.getName(), p);
		
		System.out.println("World: New Player("+name+") added! Count: "+count_players);
		return p;
	}
	
	public void deletePlayer(String n) {
		if (objects.containsKey(n)) {
			objects.get(n).clear();
			objects.remove(n);
			count_players--;
			System.out.println("World: Delete player("+n+". Count: "+count_players);
		}
	}
	
	public void setProgrammToPlayer(String name, String text) throws Exception {
		Player p = objects.get(name);
		Compiller c = new Compiller();
		Queue programm = c.getProgramm(text);
		p.setProgramm(programm);
		p.setReady(true);
		System.out.println("World: programm added!");
		isReady();
	}
	
	public Message getPoints() {
		Message message = new Message();
		message.type = "step";
		for (String name : objects.keySet()) {
			Player player = objects.get(name);
			SPlayer splayer = new SPlayer(
					player.getLocation().x,
					player.getLocation().y,
					player.getRotation(),
					player.getDirection(),
					name);
			message.addPlayer(splayer);
		}
		return message;
	}
	
	/*
	 * Проверка на готовность всех исполнителей
	 */
	private boolean isReady() {
		
		/*
		 * Если все уже были готовы, то проверяем, остался ли кто-нибудь
		 * Иначе проверяем всех.
		 */
		if (isAllReady) {
			int count = 0;
			for (String name : objects.keySet()) {
				Player p = objects.get(name);
				if (!p.isReady()) count++;
			}
			if (count==objects.size()) {
				Message message = new Message("stop");
				game.fromWorld(message);
				isAllReady = false;
				return false;
			}
		}
		else {
			for (String name : objects.keySet()) {
				Player p = objects.get(name);
				if (!p.isReady()) {
					isAllReady = false;
					return false;
				}
			}
			Message message = new Message("message");
			message.text = "Play";
			game.fromWorld(message);
			isAllReady = true;
		}
		return true;
	}
	
	public boolean step() {
		if (map == null) return false;
		if (isAllReady) {
			for (String key : objects.keySet()) {
				Player q = objects.get(key);
				try {
					q.step();
					Point p = q.ahead;
					if (map[p.x][p.y] == Map.JEWEL) {
						setStop();
						Message message = new Message(q.getName(), "winner", "");
						game.fromWorld(message);
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
					game.fromWorld(new Message(key, "error", ex.getMessage()));
					q.stop();
					q.clear();
				}
			}
			isReady();
			return true;
		}
		else
			return false;
	}
	
	public void setStop() {
		for (String key : objects.keySet()) {
			Player q = objects.get(key);
			q.stop();
		}
	}
}
	