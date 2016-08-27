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
	public static boolean isError = false;
	
	public Game(String[] args) {
		if (args.length > 0)
			for (String str : args) {
				if ("-error".equals(str)) {
					isError = true;
				} else {
					System.out.println("Неизвестный аргумент");
				}
			}
		
		server = new Server(this);
		world = new World(this);
		isPlay = new AtomicBoolean(true);
		window = new Window(this);
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
						if (isError) ex.printStackTrace();
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
		world.setMap(Map.getMap());
		window.draw();
		mainLoop();
	}
	
	/*
	 * Обработка сообщений от сервера
	 */
	public void fromServer(Message message)	{
		switch (message.type) {
		case "register":
			try {
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
			}
			catch (Exception ex) {
				ex.printStackTrace();
				message.type = "error";
				message.text = ex.getMessage();
				server.writeToClient(message);
			}
			break;
		case "exit":
			world.deletePlayer(message.name);
			message = new Message(message.name, "deletePlayer", "Player "+message.name+" has disconnect");
			server.writeToClient(message);
			break;
		case "programm":
			try {
				world.setProgrammToPlayer(message);
			}
			catch (Exception ex) {
				// Отправляем на клиенту ошибку
				if (isError) ex.printStackTrace();
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
	
	public void loadMap(String path) {
		try {
			Map.loadMap(path);
		}
		catch (Exception ex) {
			if (isError) ex.printStackTrace();
		}
		
		world.setMap(Map.getMap());
		Message message = new Message("", "map", "");
		message.map = Map.getMap();
		server.writeToClient(message);
	}
	
	private void mainLoop() {
		main_loop.start();
		Scanner sc = new Scanner(System.in);
		System.out.println("Возможные команды: \n"
				+ "map - показать миникарту\n"
				+ "reset - вернуть всех на свои места\n"
				+ "exit - выход\n"
				
		);
		while (isPlay.get()) {
			if (sc.hasNext()) {
				String str = sc.nextLine();
				if ("exit".equals(str))	{
					stop();
				} else if ("map".equals(str)) {
					window.setVisible(true);
				} else if ("reset".equals(str)) {
					world.reset();
					server.writeToClient(world.getPoints());
					server.writeToClient(new Message("", "message", "Произведен рестарт"));
				} else {
					System.out.println("Неизвестная команда");
				}
			}
		}
		sc.close();
	}
}
