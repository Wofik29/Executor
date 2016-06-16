package server;

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
	
	public void setCondition(int c)
	{
		if (c < 2)
		{
			condition = c;
		}
	}
	
	public boolean isAllTerm()
	{
		if (term1 == -1 || term2 == -1 )
			return false;
		return true;
	}
	
	/*
	 * Возвращает
	 * 0 - Если выражение истинно
	 * 1 - Если ложно
	 * -1 - Если ошибка знака
	 */
	protected int isCondition(Player obj)
	{
		Point p = new Point();
		
		// узнаем с какой стороны и что с этой стороны находится.
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
		
		// value_cell - значение клетки, с которой хотим сравнить
		int value_cell =  World.map[p.x][p.y];
		
		// Тут мы сглаживаем все виды земель в одну - песок. Иначе это вода, или сам кораблик
		value_cell = (3 < value_cell && value_cell< 40) ? Map.BEACH : value_cell; 
		
		int l_x = World.map.length;
		int l_y = World.map[0].length;
		if (p.x < 0 || p.x>l_x || p.y<0 || p.y>l_y )	return -1;
		if (term1 == -1 || term2 == -1 || condition == -1) return -1;
		
		// просто проверка со вторым оператором
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
	public abstract  boolean execute(Player obj) throws Exception;

}
