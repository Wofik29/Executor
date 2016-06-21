package server;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import other.Message;

/*
 * Главный цикл программы.
 * Запускает Server и World и дает им общение между собой 
 */
public class Game {
	private Server server;
	private World world;
	private Window window;
	private Thread main_loop;
	private AtomicBoolean isPlay; 
	
	public Game() {
		window = new Window();
		server = new Server(this);
		world = new World(this);
		isPlay = new AtomicBoolean(true);
		
		/*
		 * В этом потоке крутиться steping мира.
		 */
		main_loop = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isPlay.get()) {
					//System.out.println("step");
					if (world.step()) {
						server.writeToClient(world.getPoints());
						window.draw();
					}
					try {
						Thread.sleep(200);
					}
					catch (Exception ex) {
						
					}
				}
			}
		});
	}
	
	public void stop() {
		server.stop();
		isPlay.set(false);
		window.dispose();
		main_loop.interrupt();
	}
	
	public void start()	{
		server.start();
		try {
			Map.loadMap("simple2.map");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		world.setMap(Map.texture_map);
		window.draw();
		mainLoop();
	}
	
	/*
	 * Обработка сообщений от сервера
	 */
	public void fromServer(Message message)	{
		switch (message.type) {
		case "register":
			Player p = world.addPlayer(message.name);
			if (p == null) {
				break;
			}
			message = new Message("map");
			message.player = p.toSPlayer();
			message.map = World.map;
			message.name = p.getName();
			server.writeToClient(message);
			
			message = new Message("addPlayer");
			message.name = p.getName();
			message.text = "New player connected - "+p.getName();
			message.player = p.toSPlayer();
			server.writeToClient(message);
			break;
		case "exit":
			world.deletePlayer(message.name);
			message = new Message(message.name, "deletePlayer", "Player "+message.name+" has disconnect");
			server.writeToClient(message);
			break;
		case "programm":
			try {
				world.setProgrammToPlayer(message.name, message.text);
			}
			catch (Exception ex) {
				// Отправляем на клиенту ошибку
				server.writeToClient(new Message(message.name, "error", ex.getMessage()));
			}
			break;
		}
		window.draw();
	}
	
	public void addPlayer(String n){}
	
	public void fromWorld(Message message) {
		server.writeToClient(message);
	}
	
	private void mainLoop() {
		main_loop.start();
		Scanner sc = new Scanner(System.in);
		while (isPlay.get()) {
			System.out.println("Слушаем текст");
			if (sc.hasNext()) {
				String str = sc.nextLine();
				if ("exit".equals(str))	{
					stop();
				}
				else if ("map".equals(str))	{
					window.setVisible(true);
				}
			}
		}
		sc.close();
	}
}
