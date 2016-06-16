package Game;

import java.awt.Point;

import other.SPlayer;

public class Player 
{
	private Point position;
	private int rotation;
	private int direction;
	private String name;
	
	public String getName()
	{
		return name;
	}
	
	public Point getPosition() 
	{
		return position;
	}

	public void setPosition(int _x, int _y) 
	{
		position.setLocation(_x, _y);
	}

	public int getRotation() 
	{
		return rotation;
	}

	public void setRotation(int rotation) 
	{
		this.rotation = rotation;
	}

	public int getDirection() 
	{
		return direction;
	}

	public void setDirection(int direction) 
	{
		this.direction = direction;
	}

	public Player(int _x, int _y, int r, int d)
	{
		position = new Point(_x, _y);
		rotation = r;
		direction = d;
	}
	
	public Player(SPlayer p)
	{
		position = new Point(p.x, p.y);
		rotation = p.rotation;
		direction = p.direction;
		name = p.name;
	}
}
