package org.wolf.server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.wolf.other.*;

public class World {
	private volatile HashMap<String, Player> objects = new HashMap<String, Player>();
	private Game game;
	private boolean isAllReady = false;
	private int countPlayers = 0;
	private List<Point> spawns;
	public static volatile byte[][] map = null;
	
	public World(Game g) {
		game = g;
		spawns = new ArrayList<Point>();
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
		spawns.clear();
		map = copyMap(m);
		for (int i=0; i < m.length; i++) {
			for (int k=0; k < m[i].length; k++)	{
				if (m[i][k] == 40) {
					spawns.add(new Point(i,k));
					System.out.println("x: "+i+", y: "+k);
					map[i][k] = Map.SHALLOW;
				}
			}
		}
	}
	
	public Player addPlayer(String name) throws Exception {
		if (objects.containsKey(name)) return null;
		if (countPlayers > spawns.size()) throw new Exception("Нет места!");
		Point spawn = spawns.get(countPlayers++);
		Player p = new Player(spawn.x, spawn.y);
		p.setRotation(90);
		p.setName(name);
		p.setMap();
		objects.put(p.getName(), p);
		System.out.println("World: New Player("+name+") added! Count: "+ countPlayers);
		return p;
	}
	
	public void deletePlayer(String n) {
		if (objects.containsKey(n)) {
			objects.get(n).clear();
			objects.remove(n);
			countPlayers--;
			System.out.println("World: Delete player("+n+"). Count: "+ countPlayers);
		}
	}
	
	public void reset() {
		for (String name : objects.keySet()) {
			objects.get(name).reset();
		}
	}
	
	public void setProgrammToPlayer(Message message) throws Exception {
		Player p = objects.get(message.name);
		Compiller c = new Compiller();
		Queue program = c.getProgramm(message.algorithm);
		p.setProgramm(program);
		p.setReady(true);
		System.out.println("World: program added!");
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
					if (Game.isError) ex.printStackTrace();
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
	