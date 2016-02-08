package Game;

public class Quad {

	int width;
	int height;
	
	int x, y;
	
	Quad(int x, int y,int w, int h)
	{
		width = w;
		height = h;
		this.x = x;
		this.y = y;
	}
	
	public void print()
	{
		System.out.println("width : "+width);
	}
	
	public void setW(int w)
	{
		width = w;
	}

}