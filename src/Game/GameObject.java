package Game;

import java.util.HashMap;
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
	
	int[][] map;
	int step = 10;
		
	// пока будут цифры направления 0 - вверх, 1 - вправо и т.д.
	int direction;
	int current;
	
	Queue programm;
	HashMap<Integer, Integer> direct = new HashMap<Integer, Integer>();

	GameObject(int x, int y, int[][] m)
	{
		this.x = x;
		this.y = y;
		
		x_p = x*step;
		y_p = y*step;
		
		width = height = step;
		
		map = m;
		
		programm = new Queue();
		
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
		
	}
	
	void setProgramm(Queue q)
	{
		programm = q;
	}
	
	public void step()
	{
		if (x_p >= x*step) x_p --;
		if (x_p < x*step) x_p ++;
		if (y_p >= y*step) y_p --;
		if (y_p < y*step) y_p ++;
		
		if (current_rotation == 270 && rotation == 0) current_rotation = -90;
		if (current_rotation == -270 && rotation == 0) current_rotation = 90;
		
		if (current_rotation > rotation) current_rotation -= 2;
		if (current_rotation < rotation) current_rotation += 2;
		
		if (x_p == x*step && y_p == y*step && current_rotation == rotation)
		{
			next();
			System.out.println("next");
		}
		
		System.out.println("x : "+x+", y: "+y+", x_p : "+x_p+", y_p : "+y_p+", direction : "+direction+", rotation: "+rotation+", current_rotation: "+current_rotation);
	}
	
	
	public void next()
	{
		programm.execute(this);
	}
	
}
