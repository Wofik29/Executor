package Game;

import java.util.HashMap;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap< Integer, String> map = new HashMap<>();
		
		map.put(0, "Zero");
		map.put(1, "One");
		map.put(2, "Two");
		map.put(3, "Three");
		map.put(4, "Four");
		
		for (int i=0; i<5; i++)
			System.out.println(map.get(i));
		
	}

}
