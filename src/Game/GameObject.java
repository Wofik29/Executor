package Game;

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
	int x;
	int y;
	
	// Пиксельные координаты
	int x_p;
	int y_p;
	
	int width;
	int height;
	
	int rotation;
	int current_rotation;
	int current_front;
	
	byte[][] map;
	int step;
	
	boolean isGo = true;
	boolean isControl = false;
		
	/* 
	 * пока будут цифры направления 
	 * 0 - вниз-влево 
	 * 1 - вниз-вправо
	 * 2 - вверх-вправо
	 * 3 - вверх-влево 
	 */
	int direction;
	int current;
	
	
	
	MainLoop programm;
	ConcurrentLinkedQueue<Command> commands = new ConcurrentLinkedQueue<Command>();
	HashMap<Integer, Integer> direct = new HashMap<Integer, Integer>();

	GameObject(int x, int y, int s, byte[][] m)
	{
		this.x = x;
		this.y = y;
		step = s;
		
		x_p = x*step;
		y_p = y*step;
		
		width = height = step;
		
		map = m;
		
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
		
		checkDirection();
		
	}
	
	void setProgramm(MainLoop q)
	{
		programm = q;
	}
	
	public void step()
	{
		
		if (x_p >= x*step) x_p --;
		if (x_p < x*step) x_p ++;
		if (y_p >= y*step) y_p --;
		if (y_p < y*step) y_p ++;
		
		//if (current_rotation == 270 && rotation == 0) current_rotation = -90;
		//if (current_rotation == -270 && rotation == 0) current_rotation = 90;
		System.out.println("rot: "+rotation);
		System.out.println("cur_rot: "+current_rotation);
		
		if (current_rotation > rotation) current_rotation -= 2;
		if (current_rotation < rotation) current_rotation += 2;
		
		if (isGo && x_p == x*step && y_p == y*step && current_rotation == rotation)
		{
			checkDirection();
			checkRotation();
			next();
			//isGo = false;
			//System.out.println("next");
		}
		
		
		//System.out.println("x : "+x+", y: "+y+", x_p : "+x_p+", y_p : "+y_p+", direction : "+direction+", rotation: "+rotation+", current_rotation: "+current_rotation);
	}
	
	public void checkDirection()
	{
		switch (direction)
		{
		case 0: //По экран вниз-влево
			current_front = map[x+1][y];
			break;
		case 1: // вниз-вправо
			current_front = map[x][y+1];
			break;
		case 2: // верх-вправо
			current_front = map[x-1][y];
			break;
		case 3: // верх-влево
			current_front = map[x][y-1];
			break;
		}
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
		if (isControl)
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
