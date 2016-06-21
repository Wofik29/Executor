package Game;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import other.Message;

public class Game {
	private Window window;
	private WindowConnect window_connect;
	private ServerHandle server_h;
	
	public void start()	{
		window_connect = new WindowConnect(this);
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
		server_h.writeToServer(message);
	}
	
	public void connect(String address) {
		try	{
			server_h = new ServerHandle(this, address);
		}
		catch (UnknownHostException ex)	{
			//ex.printStackTrace();
			window_connect.setLabelText("Unknown host");
		}
		catch (SocketException ex) {
			window_connect.setLabelText("Incorrect host");
		}
		catch (Exception ex) {
			ex.printStackTrace();
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
		server_h.stop(0);
		System.out.println("Close");
	}
}
