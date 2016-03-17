package Game;
import java.awt.EventQueue;
import java.util.Stack;

import org.lwjgl.input.Keyboard;

public class Controller  
{
	Window window;
	World world;
	
	Thread thread_world;
	Thread thread_window;
	
	final int WIDTH = 1024;
	final int HEIGHT = 780;
	
	public void init()
	{
		int step = 10;
		world = new World(WIDTH, HEIGHT, step);
		window = new Window(this);
		window.setMap(world.getMap());
		//window.setObjects(world.getObjects());
		
		GameObject p =  world.getObjects().get(0);
		window.setPlayer(p);
		
		thread_world = new Thread(world);
		//thread_window = new Thread(window);
	}
	
	public void setProgramm(String text)
	{
		
		//StringBuilder sb = new StringBuilder(text);
		/*
		 *  0 - обычное
		 *  1 - обработка условия для цикла
		 */
		int state = 0;
		boolean error = false;
		String error_text = "";
		
		
		Stack<Queue> stack = new Stack<>();
		
		Queue programm = new MainLoop();
		Queue current = programm;
		
		String[] lines = text.split("\t");
		
		parse: for (String line: lines)
		{
			String[] strs = line.split("\\s+");
			
			/* 
			 * Проверяем строку и на основании нее выбираем вид состояния
			 */
			for (String str : strs )
			{
				// TODO разбивать на буквы и по буквенно проверять. например ahead=lefty будет как одна строка, надо вычленить каждый term
				
				if (str.charAt(0) == '(')
					str = str.substring(1);
				if (str.charAt(str.length()-1) == ')')
					str = str.substring(0,str.length()-1);
				System.out.println(str);
				
				
				switch (state)
				{
				// Обычное, добавляем команды
				case 0:
					switch (str.toLowerCase())
					{
					case "forward":
						current.add(new Forward());
						break;
					case "left":
						current.add(new Left());
						break;
					case "right":
						current.add(new Right());
						break;
					case "if":
						state = 1;
						stack.push(current);
						Queue temp = new ifTerm(); 
						current.add(temp);
						current = temp;
						temp = null;
						break;
					case "end":
						current = stack.pop();
						break;
					case "else":
						ifTerm if_temp = (ifTerm) current;
						if_temp.setElse();
						if_temp = null;
						break;
					default: 
						error = true;
						error_text = "Ожидался оператор, но встречен "+str;
						break parse;
					}
					break;
				case 1:
					ifTerm if_temp = (ifTerm) current;
					switch (str.toLowerCase())
					{
					case "ahead":
						if_temp.setTerm1(1);
						break;
					case "lefty":
						if_temp.setTerm1(0);
						break;
					case "righty":
						if_temp.setTerm1(2);
						break;
					case "water":
						if_temp.setTerm2(Map.SHALLOW);
						break;
					case "beach":
						if_temp.setTerm2(Map.BEACH);
						break;
					case "then": 
						if (if_temp.isAllTerm()) state = 0;
						else
						{
							error = true;
							error_text = "Условие не полное!";
							break parse;
						}
						// TODO Проверка, полностью ли написано условие.
						
						break;
					default:
						error = true;
						error_text = "Ожидалось условие, но встречен "+str;
						// TODO вывод ошибки "Ожидалось условие, но нашел str
						break parse;
					}
				case 2:
					switch (str.toLowerCase())
					{
					
					}
					break;
				case 3:
					break;
				}
			}
		}
		
		if (error)
		{
			System.out.println(error_text);
			// TODO скорее всего будет throw exeption, и написание ошибки.
		}
		
		world.getObjects().get(0).setProgramm((MainLoop)current);
	}
	
	public void pressedKey(int key, char c)
	{
		GameObject p = world.getObjects().get(0);
		switch (key)
        {
        case Keyboard.KEY_A: System.out.println("pressed A "); 
        	p.addCommand(new Left());
        	//window.x ++;
        	break;
        case Keyboard.KEY_D: System.out.println("pressed D"); 
    		p.addCommand(new Right());
    		//window.x --;
        	break;
        case Keyboard.KEY_W: System.out.println("pressed W"); 
    		p.addCommand(new Forward());
        	//window.y ++;
        	break;        
        case Keyboard.KEY_S: System.out.println("pressed S"); 
        	//window.y --;
        	break;
        }
	}

	public void relessedKey(int key, char c)
	{
		switch (key)
        {
        case Keyboard.KEY_A: System.out.println("relessed A"); 
        	//window.x_speed = 0;
        	break;
        case Keyboard.KEY_D: System.out.println("relessed D"); 
        	//window.x_speed = 0;
        	break;
        case Keyboard.KEY_W: System.out.println("relessed W"); 
        	//window.y_speed = 0;
        	break;
        case Keyboard.KEY_S: System.out.println("relessed S"); 
        	//window.y_speed = 0;
        	break;	
        }
	}
	
	public void stop()
	{
		world.stop();
	}
	
	public void start()
	{
		window.start();
		//thread_window.start();
		//EventQueue.invokeLater(window);
		thread_world.start();
		try
		{
			//thread_window.join();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		//world.stop();
	}
	
	
}
