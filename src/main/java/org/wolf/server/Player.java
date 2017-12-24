package org.wolf.server;

import java.awt.Point;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.wolf.other.SPlayer;

/*
 * Есть возможность двигаться прямо и поворачивать 
 */
public class Player {
	// Координаты в мапе
	private Point spawn;
	private Point location = new Point();
	private int rotation;
	private byte current_cell;
	private boolean isGo = false;
	private boolean isControl = false;
	private String name;
	
	/* 
	 * direction: 
	 * 0 - вниз-вправо
	 * 1 - вниз-влево
	 * 2 - вверх-влево
	 * 3 - вверх-вправо
	 * side:
	 * 0 - front
	 * 1 - left
	 * 2 - right 
	 */
	private int direction;
	public Point ahead,lefty,righty;
	private static Point[][] mix = new Point[4][3];
	static {	
		mix[0][0] = new Point(0,1);
		mix[0][1] = new Point(1,0);
		mix[0][2] = new Point(0,-1);
		mix[1][0] = new Point(-1,0);
		mix[1][1] = mix[0][0];
		mix[1][2] = mix[0][1];
		mix[2][0] = mix[0][2];
		mix[2][1] = mix[1][0];
		mix[2][2] = mix[0][0];
		mix[3][0] = mix[0][1];
		mix[3][1] = mix[0][2];
		mix[3][2] = mix[1][0];
	}
	
	private MainLoop programm;
	private ConcurrentLinkedQueue<Command> commands = new ConcurrentLinkedQueue<Command>();
	public HashMap<Integer, Integer> directs = new HashMap<Integer, Integer>();

	Player(int x, int y) {
		System.out.println("Player set: x: "+x+", y: "+y);
		location.x = x;
		location.y = y;
		spawn = new Point(x,y);
		programm = new MainLoop();
		rotation = 90;
		directs.put(90, 0);
		directs.put(0, 3);
		directs.put(180, 1);
		directs.put(270, 2);
		directs.put(-90, 2);
		directs.put(-180, 1);
		directs.put(-270, 0);
		directs.put(-360, 3);
		directs.put(360, 3);
		ahead = new Point();
		lefty = new Point();
		righty= new Point();
		
		checkDirection();
	}
	
	public void reset() {
		location =(Point) spawn.clone();
		isGo = false;
	}
	
	public void setProgramm(Queue q) {
		programm = (MainLoop) q;
	}
	
	public void setMap() {
		current_cell = World.map[location.x][location.y];
		World.map[location.x][location.y] = Map.SHIP;
	}
	
	public void clear() {
		World.map[location.x][location.y] = current_cell;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setGo() {
		isGo = true;
	}
	
	public void stop() {
		isGo = false;
	}
	
	public boolean isReady() {
		if (programm.getSize() == 0)
			return false;
		return isGo;
	}
	
	public void setReady(Boolean r) {
		isGo = r;
	}
	
	public void step() throws Exception {
		if (World.map == null) return;
		
		if (isGo) {
			next();
			checkDirection();
			checkRotation();
			//System.out.println("Player("+name+")"+": do step.");
		}
	}
	
	public void setLocation(Point p) {
		// Стираем свое присутствие на карте
		World.map[location.x][location.y] = current_cell;
		
		// Берем новые значения
		location.x = p.x;
		location.y = p.y;
		// запоминаем значение нового положения
		current_cell = World.map[location.x][location.y];
		// Ставим кораблик на новое место 
		World.map[location.x][location.y] = Map.SHIP;
	}
	
	public Point getLocation() {
		return (Point) location.clone();
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public void setRotation(int r) {
		rotation = r;
		checkDirection();
	}
	
	public void checkDirection() {
		direction = directs.get(rotation);
		if (World.map!=null) {
			Point p = mix[direction][1]; // left
			ahead.setLocation(location.x+p.x, location.y+p.y);
			
			p = mix[direction][0]; // front
			lefty.setLocation(location.x+p.x, location.y+p.y);
		
			p = mix[direction][2]; // right
			righty.setLocation(location.x+p.x, location.y+p.y);
		}
	}
	
	public void checkRotation() {
		if (rotation == 360) rotation = 0;
		if (rotation == -360) rotation = 0;
	}
	
	public void addCommand(Command c) {
		commands.add(c);
	}
	
	private void next() throws Exception {
		if (isControl && !commands.isEmpty()) {
			commands.poll().execute(this);
		}
		else
			if (programm != null && programm.getSize() > 0) {
				if (programm.execute(this)) isGo = false;
			}
	}
	
	public SPlayer toSPlayer() {
		SPlayer p = new SPlayer(location.x, location.y, rotation, direction, name);
		return p;
	}
}
