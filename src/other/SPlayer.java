package other;

import java.io.Serializable;

public class SPlayer implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public int x,y;
	public int rotation;
	public byte direction;
	public String name;
	
	public SPlayer(int _x, int _y, int r, int d, String n)
	{
		x = _x;
		y = _y;
		rotation = r;
		direction = (byte)d;
		name = n;
	}
}
