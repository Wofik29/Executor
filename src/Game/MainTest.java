package Game;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MainTest test = new MainTest();
		test.test_map();
		
		
	}
	
	public void test_map()
	{
		Map map = new Map();
		
		try
		{
			byte[][] m = map.loadMap("Executor.map");
			
			for (int i=0; i<m.length; i++)
			{
				
				for (int j=0; j<m[i].length; j++)
				{
					System.out.print(m[i][j]+" ");
				}
				System.out.println();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}

}
