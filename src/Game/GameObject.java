package Game;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * Есть возможность двигаться прямо и поворачивать 
 */
public class GameObject 
{
	// Координаты в мапе
	//int x;
	//int y;
	
	Point location = new Point();
	
	// Пиксельные координаты
	int x_p;
	int y_p;
	
	int width;
	int height;
	
	int rotation;
	int current_rotation;
	
	byte[][] map;
	int step;
	
	boolean isGo = true;
	boolean isControl = true;
		
	/* 
	 * direction: 
	 * 0 - вниз-влево 
	 * 1 - вниз-вправо
	 * 2 - вверх-вправо
	 * 3 - вверх-влево
	 * side:
	 * 0 - front
	 * 1 - left
	 * 2 - right 
	 */
	int direction;
	
	Point ahead,lefty,righty;
	int current;
	
	static Point[][] mix = new Point[4][3];
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
	
	MainLoop programm;
	ConcurrentLinkedQueue<Command> commands = new ConcurrentLinkedQueue<Command>();
	HashMap<Integer, Integer> direct = new HashMap<Integer, Integer>();

	GameObject(int x, int y, int s)
	{
		//this.x = x;
		//this.y = y;
		location.x = x;
		location.y = y;
		step = s;
		
		x_p = x*step;
		y_p = y*step;
		
		width = height = step;
		
		programm = new MainLoop();
		
		direction = 0;
		current_rotation = rotation = 0;
		current = -1;
		
		direct.put(0, 0);
		direct.put(90, 3);
		direct.put(180, 2);
		direct.put(270, 1);
		direct.put(-90, 1);
		direct.put(-180, 2);
		direct.put(-270, 3);
		direct.put(-360, 0);
		direct.put(360, 0);
		
		ahead = new Point();
		lefty = new Point();
		righty= new Point();
		
		checkDirection();
	}
	
	public void setProgramm(MainLoop q)
	{
		programm = q;
	}
	
	public void setMap(byte[][] m)
	{
		map = m;
	}
	
	public void step()
	{
		
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
		}
		
		if (current_rotation > rotation) current_rotation -= 2;
		if (current_rotation < rotation) current_rotation += 2;
		
		if (isGo && x_p == location.x*step && y_p == location.y*step && current_rotation == rotation)
		{
			checkDirection();
			checkRotation();
			next();
			//isGo = false;
		}
		
		//System.out.println("x : "+x+", y: "+y+", x_p : "+x_p+", y_p : "+y_p+", direction : "+direction+", rotation: "+rotation+", current_rotation: "+current_rotation);
	}
	
	public void checkDirection()
	{
		if (map!=null)
		{
			Point p = mix[direction][1];
			ahead.setLocation(location.x+p.x, location.y+p.y);
			
			p = mix[direction][0];
			lefty.setLocation(location.x+p.x, location.y+p.y);
		
			p = mix[direction][2];
			righty.setLocation(location.x+p.x, location.y+p.y);
		}
		
		//System.out.println("");
		//System.out.println("aheadx: "+ahead.x+", aheady: "+ahead.y+", direction: "+direction);
		//System.out.println("x: "+location.x+", y: "+location.y+", direction: "+direction);
		
	}
	
	public void checkRotation()
	{
		if (current_rotation == 360 || current_rotation == -360)
		{
			current_rotation = 0;
			rotation = 0;
		}
	}
	
	public void addCommand(Command c)
	{
		commands.add(c);
	}
	
	public void next()
	{
		if (isControl && !commands.isEmpty())
		{
			commands.poll().execute(this);
		}
		else
		if (programm != null) {
			//System.out.println("execute");
			programm.execute(this);
		}
	}
	
}
