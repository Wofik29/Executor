package Game;

import java.awt.Point;

public abstract class ControlLoop extends Queue 
{
	/* 
	 * Без разницы как написано в условии
	 * term1 - сторона кораблика
	 * term2 - вид клетки
	 * перед тем, как отправлять сюда, нужно разобрать.
	 */
	protected int term1 = -1; 
	protected int term2 = -1;
	protected int condition = -1;
	
	// Номер элемента, с которого начинется ветка else. 
	protected int number_else = -1;
		
	public void setElse()
	{
		number_else = commands.size();
	}
		
	
	public void setTerm1(int t1)
	{
		term1 = t1;
	}
	
	public void setTerm2(int t2)
	{
		term2 = t2;
	}
	
	public boolean isAllTerm()
	{
		if (term1 == -1 || term2 == -1 )
			return false;
		return true;
	}
	
	// value_cell - значение клетки, с которой хотим сравнить
	protected int isCondition(GameObject obj)
	{
		Point p = new Point();
		
		switch (term1)
		{
		case 1:
			p = obj.ahead;
			break;
		case 0:
			p = obj.lefty;
			break;
		case 2:
			p = obj.righty;
			break;
		}
		
		int l = World.map.length;
		if (p.x < 0 || p.x>l || p.y<0 || p.y>l )	return -1;
		if (term1 == -1 || term2 == -1 || condition == -1) return -1;
		
		/*
		 * Берем значение по текущим координатам и приводим все полустенки и т.п к одному виду.
		 * Остается только вода, корабль и клад
		 */
		
		int value_cell =  World.map[p.x][p.y];
		value_cell = (3 < value_cell && value_cell< 40) ? Map.BEACH : value_cell; 
		
		System.out.println("ControlLoop: { condition: "+condition+", term1: "+term1+", term2: "+term2+", value_cell: "+value_cell+", map: "+World.map[p.x][p.y]+" }");
		
		switch (condition)
		{
		case 0 : // "="
			return value_cell == term2 ? 0 : 1;
		case 1: // "!=":
			return value_cell != term2 ? 0 : 1;
		}
		
		return -1;
	}
	
	@Override
	public abstract  boolean execute(GameObject obj) throws Exception;

}
