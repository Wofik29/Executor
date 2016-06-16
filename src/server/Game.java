package server;

import java.util.Scanner;
import other.Message;

/*
 * Главный цикл программы.
 * Запускает Server и World и дает им общение между собой 
 */
public class Game 
{
	private Server server;
	private World world;
	private Thread listen_command;
	private boolean isPlay = true; 
	
	public Game()
	{
		server = new Server(this);
		world = new World(this);
		listen_command = new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner sc = new Scanner(System.in);
				while (isPlay)
					if (sc.hasNext())
					{
						String str = sc.nextLine();
						if ("exit".equals(str))
						{
							stop();
						}
					}
				sc.close();
			}
		});
	}
	
	public void stop()
	{
		server.stop();
		isPlay = false;
		listen_command.interrupt();
	}
	
	public void start()
	{
		server.start();
		try 
		{
			Map.loadMap("simple2.map");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		world.setMap(Map.texture_map);
		isPlay = true;
		mainLoop();
	}
	
	/*
	 * Обработка сообщений от сервера
	 */
	public void fromServer(Message message)
	{
		switch (message.type)
		{
		case "register":
			Player p = world.addPlayer(message.name);
			message.player = p.toSPlayer();
			message.map = World.map;
			message.type = "map";
			server.writeToClient(message);
			break;
		case "exit":
			world.deletePlayer(message.name);
			message = new Message(message.name, "deletePlayer", "Player "+message.name+" has disconnect");
			server.writeToClient(message);
			break;
		case "programm":
			try
			{
				world.setProgrammToPlayer(message.name, message.text);
			}
			catch (Exception ex)
			{
				// Отправляем на клиенту ошибку
				server.writeToClient(new Message(message.name, "error", ex.getMessage()));
			}
			break;
		}
	}
	
	public void addPlayer(String n)
	{
		
	}
	
	public void fromWorld(Message message)
	{
		server.writeToClient(message);
	}
	
	private void mainLoop()
	{
		listen_command.start();
		while (isPlay)
		{
			//System.out.println("step");
			if (world.step())
				server.writeToClient(world.getPoints());
			try
			{
				Thread.sleep(400);
			}
			catch (Exception ex)
			{
				
			}
		}
	}
}
