package Game;

import java.util.List;

public class World 
{
	int width;
	int heigth;
	
	List<Quad> quads;
	
	World(int w, int h, List<Quad> q)
	{
		width = w;
		heigth = h;
		quads = q;
	}
	
	void step()
	{
		for (Quad q : quads)
		{
			//q.step();
		}
	}
	

}
