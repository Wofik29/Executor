package server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class Window extends JFrame
{
	//private JFrame main_frame;
	private int step = 20;
	private int offset = 40;
	
	public Window()
	{
		this.setTitle("Map");
		this.setSize(400, 400);
		this.setVisible(true);
	}
	
	public void draw()
	{
		update(this.getGraphics());
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.drawRect(0, 0, this.getWidth(), this.getHeight());
		byte[][] map = World.map;
		
		if (map != null)
		{
			for (int i=0; i<map.length; i++)
				for (int j=0; j<map[i].length; j++)
				{
					switch (map[i][j])
					{
					case Map.SHALLOW:
						g2.setColor(new Color(0,192,255));
						break;
					case Map.SHIP:
						g2.setColor(Color.BLACK);
						break;
					case Map.JEWEL:
						g2.setColor(Color.red);
						break;
					default:	
						g2.setColor(new Color(255,217,00));
						break;
					}
					
					g2.fillRect(i*step+offset, j*step+offset, step, step);
				}
		}
	}
	
}