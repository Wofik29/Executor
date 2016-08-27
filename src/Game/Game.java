package Game;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import other.Message;

public class Game {
	private Window window;
	private WindowConnect window_connect;
	private ServerHandle server_h;
	
	public static boolean isDebug = false;
	public static boolean isError = true;
	
	public void start()	{
		window_connect = new WindowConnect(this);
		Compiller.setCommands("Executer.ini");
	}
	
	public void fromServer(Message message)	{
		switch (message.type) {
		case "connected":
			window_connect.connectSet();
			break;
		case "dublicateName":
			window_connect.setLabelText("Имя уже использовано");
			break;
		case "map":
			if (message.map != null && message.map.length > 0 )	{
				window = new Window(this);
				window.start();
				window_connect.setShow(false);
				window.map = message.map.clone();
				Player p = new Player(message.player);
				window.addPlayer(p);
				server_h.setName(p.getName());
			}
			break;
		case "addPlayer":
			Player p = new Player(message.player);
			window.addPlayer(p);
			window.setMsg(message.text);
			break;
		case "deletePlayer":
			window.deletePlayer(message.player);
			window.setMsg(message.text);
			break;
		case "exit":
			window.closeServer();
			break;
		case "error":
			window.setMsg(message.text);
			window.setRender(message.name, false);
			break;
		case "stop":
			window.setAllVisible();
			window.setMsg("Stop");
			break;
		case "step":
			if (message.players != null)
				window.updatePlayers(Arrays.asList(message.players), message.size);
			break;
		case "message":
			window.setMsg(message.text);
			break;
		case "winner":
			if (window.getName().equals(message.name)) {
				window.setMsg("Поздравляем! Вы дошли до клада!");
			}
			else {
				window.setMsg("Конец игры. Победил "+message.name);
			}
		}
	}
	
	public void fromPlayer(Message message) {
		System.out.println("From player: "+message.type +", text: "+message.text);
		if (message.type.equals("programm")) {
			Compiller c = new Compiller();
			try {
				System.out.println(c.getProgramm(message.text));
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
			
			try {
				byte[][] algorithm = c.getProgramm(message.text);
				message.algorithm = algorithm;
				message.text = null;
				server_h.writeToServer(message);
				window.setMsg("Wait other players...");
			}
			catch (Exception ex) {
				if (isError) ex.printStackTrace();
				window.setMsg(ex.getMessage());
			}
		}
		else {
			server_h.writeToServer(message);		
		}
		
	}
	
	public void connect(String address) {
		try	{
			server_h = new ServerHandle(this, address);
		}
		catch (UnknownHostException ex)	{
			if (isError) ex.printStackTrace();
			window_connect.setLabelText("Unknown host");
		}
		catch (SocketException ex) {
			if (isError) ex.printStackTrace();
			window_connect.setLabelText("Incorrect host");
		}
		catch (Exception ex) {
			if (isError) ex.printStackTrace();
			window_connect.setLabelText("Unknown error");
		}
		server_h.start();
	}
	
	public void sendName(String name) {
		server_h.registerName(name);
	}
	
	public void startServerH() {
		server_h.start();
	}
	
	public void stop() {
		if (server_h != null) server_h.stop(0);
	}
}
