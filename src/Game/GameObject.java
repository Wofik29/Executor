package Game;


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
	int speed;
	
	int[][] map;
	int step = 20;
		
	// пока будут цифры направления 0 - вверх, 1 - вправо и т.д.
	int direction;
	
	int[] programm;
	
	int current;

	GameObject(int x, int y, int[] p, int[][] m)
	{
		this.x = x;
		this.y = y;
		
		x_p = x*step;
		y_p = y*step;
		
		width = height = step;
		
		map = m;
		
		programm = p;
		
		direction = 0;
		rotation = 0;
		current = -1;
	}
	
	public void step()
	{
		if (x_p > x*step) x_p --;
		if (x_p < x*step) x_p ++;
		if (y_p > y*step) y_p --;
		if (y_p < y*step) y_p ++;
				
		//System.out.println("x : "+x+", y: "+y+", x_p : "+x_p+", y_p : "+y_p+", next_x : "+next_x+", next_y: "+next_y);
	}
	
	void setCommand(int speed)
	{
		if (current == programm.length-1) current = 0;
		else current++;
	}
	
	void forward()
	{
		
	}
	
	
	/*
	 * Как то сделать плавный поворот.
	 */
	void left()
	{
		
	}
	
	void right()
	{
		
	}
	
	void stepA()
	{
		switch (programm[current])
		{
		case 0:
			forward();
			break;
		case 1:
			left();
			break;
		case 2:
			right();
			break;
		}
	}
	
}
