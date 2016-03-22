package Game;

import java.util.HashMap;
import java.util.Random;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Compiller c = new Compiller();
		c.setFile("Executor.ini");
		c.read();
		Queue q = c.getProgramm1();
		
		System.out.println(q.toString());
		
	}

}
