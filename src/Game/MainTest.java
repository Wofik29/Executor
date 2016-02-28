package Game;

import java.util.HashMap;
import java.util.Random;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random rnd = new Random();
		
		for (int i =0; i<6; i++)
		{
			float round = rnd.nextFloat();
			
			if (round<0.33)
				System.out.println("Сереброника");
			else if ( round<0.66)
				System.out.println("585");
			else System.out.println("Золото");
			
		}
		
	}

}
