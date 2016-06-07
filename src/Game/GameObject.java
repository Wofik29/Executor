package Game;

import java.awt.Point;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * Есть возможность двигаться прямо и поворачивать 
 */
public class GameObject 
{
	// Координаты в мапе
	private Point location = new Point();
	
	// Пиксельные координаты
	public int x_p;
	public int y_p;
	
	public int rotation;
	private int current_rotation;
	
	//public static volatile byte[][] map;
	private byte current_cell;
	private int step;
	
	private boolean isGo = true;
	private boolean isControl = false;
		
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
	public int direction;
	public Point ahead,lefty,righty;
	
	
	private static Point[][] mix = new Point[4][3];
	static 
	{	
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

	GameObject(int x, int y, int s)
	{
		location.x = x;
		location.y = y;
		step = s;
		
		x_p = x*step;
		y_p = y*step;
		
		programm = new MainLoop();
		
		direction = 0;
		
		directs.put(90, 0);
		directs.put(0, 3);
		directs.put(180, 1);
		directs.put(270, 2);
		
		directs.put(-90, 2);
		directs.put(-180, 1);
		directs.put(-270, 0);
		directs.put(-360, 3);
		directs.put(360, 3);
		current_rotation = rotation = 90;
		
		ahead = new Point();
		lefty = new Point();
		righty= new Point();
		
		checkDirection();
	}
	
	public void setProgramm(MainLoop q)
	{
		programm = q;
	}
	
	public void setMap()
	{
		current_cell = World.map[location.x][location.y];
		World.map[location.x][location.y] = Map.SHIP;
	}
	
	public void setGo()
	{
		isGo = true;
	}
	
	public void stop()
	{
		isGo = false;
	}
	
	public void step() throws Exception
	{
		if (World.map == null) return;
		
		if (x_p >= location.x*step) x_p --;
		if (x_p < location.x*step) x_p ++;
		if (y_p >= location.y*step) y_p --;
		if (y_p < location.y*step) y_p ++;
		
		//if (current_rotation == 270 && rotation == 0) current_rotation = -90;
		//if (current_rotation == -270 && rotation == 0) current_rotation = 90;
		if (Main.isDebug)
		{
			System.out.println("rot: "+rotation);
			System.out.println("cur_rot: "+current_rotation);
			//System.out.println("aheadx: "+ahead.x+", aheady: "+ahead.y+", direction: "+direction);
			//System.out.println("x: "+location.x+", y: "+location.y+", direction: "+direction);
			//System.out.println("x: "+ahead.x+", y: "+ahead.y+", map: "+World.map[ahead.x][ahead.y]);
		}
		
		if (current_rotation > rotation) current_rotation -= 2;
		if (current_rotation < rotation) current_rotation += 2;
		
		if (isGo && x_p == location.x*step && y_p == location.y*step && current_rotation == rotation)
		{
			
			next();
			checkDirection();
			checkRotation();
			//isGo = false;
		}

	}
	
	public void setLocation(Point p)
	{
		World.map[location.x][location.y] = current_cell;
		location.x = p.x;
		location.y = p.y;
		current_cell = World.map[location.x][location.y];
		World.map[location.x][location.y] = Map.SHIP;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public void checkDirection()
	{
		if (World.map!=null)
		{
			Point p = mix[direction][1]; // left
			ahead.setLocation(location.x+p.x, location.y+p.y);
			
			p = mix[direction][0]; // front
			lefty.setLocation(location.x+p.x, location.y+p.y);
		
			p = mix[direction][2]; // right
			righty.setLocation(location.x+p.x, location.y+p.y);
		}	
	}
	
	public void checkRotation()
	{
		if (rotation == 360)
		{
			current_rotation = -90;
			rotation = 0;
		}
		if (rotation == -360)
		{
			current_rotation = 90;
			rotation = 0;
		}
	}
	
	public void addCommand(Command c)
	{
		commands.add(c);
	}
	
	private void next() throws Exception
	{
		if (isControl && !commands.isEmpty())
		{
			commands.poll().execute(this);
		}
		else
		if (programm != null && programm.getSize() > 0) 
		{
			if (programm.execute(this)) isGo = false;
		}
		
		
	}
	
}
