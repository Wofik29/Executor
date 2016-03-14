package Game;

public class ifTerm extends Queue 
{
	/* 
	 * Без разницы как написано в условии
	 * term1 - сторона кораблика
	 * term2 - вид клетки
	 * перед тем, как отправлять сюда, нужно разобрать.
	 */
	private int term1; 
	private int term2;

	// Номер элемента, с которого начинется ветка else. 
	private int number_else;
	
	
	public void setTerm1(int t1)
	{
		term1 = t1;
	}
	
	public void setTerm2(int t2)
	{
		term2 = t2;
	}
	
	@Override
	public boolean execute(GameObject obj)
	{
		if (current_command != null)
		{
			if (current_number == 0)
			{
				// TODO делаем проверку, и решаем в какую ветку идти. Или вообще на выход.
			}
			{
				
			}
		}
		
		return true;
	}
}
