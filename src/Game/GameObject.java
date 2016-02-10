package Game;


/*
 * Есть возможность двигаться прямо и поворачивать 
 */
public class GameObject 
{
	int x;
	int y;
	int rotation;
	int speed;
	
	// пока будут цифры направления 0 - вверх, 1 - вправо и т.д.
	int direction;
	
	int next_x;
	int next_y;
	
	int[] programm;
	
	int current;

	GameObject(int x, int y, int[] p)
	{
		this.x = x;
		this.y = y;
		
		programm = p;
		
		next_x = x;
		next_y = y;
		
		direction = 0;
		rotation = 0;
		current = -1;
	}
	
	void setCommand(int speed)
	{
		if (current == programm.length-1) current = 0;
		else current++;
	}
	
	void forward()
	{
		switch (direction)
		{
		case 0: // up
			if (y != next_y) y += speed;
			break;
		case 1: // rigth
			if (x != next_x) x += speed;
			break;
		case 2: //down
			if (y != next_y) y -= speed;
			break;
		case 3: // left
			if (x != next_x) x -= speed;
		}
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
	
	void step()
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
