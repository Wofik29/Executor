package Game;

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
	
	protected boolean isCondition(int value_cell)
	{
		value_cell = (value_cell != Map.SHALLOW || value_cell != Map.DEEP) ? 1 : 0;
		if (term1 == -1 || term2 == -1 || condition == -1) return false;
		
		switch (condition)
		{
		case 0 : // "="
			return value_cell == term2;
		case 1: // "!=":
			return value_cell != term2;
		}
		
		return false;
	}
	
	@Override
	public abstract  boolean execute(GameObject obj);

}
