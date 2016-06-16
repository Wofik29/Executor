package Game;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import other.Message;

public class Game 
{
	private Window window;
	private ServerHandle server_h;
	
	public Game()
	{
		window = new Window(this);
	}
	
	public void start()
	{
		window.start();
	}
	
	public void fromServer(Message message)
	{
		switch (message.type)
		{
		case "map":
			if (message.map != null && message.map.length > 0 )
			{
				window.map = message.map.clone();
				Player p = new Player(message.player);
				window.addPlayer(p);
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
			break;
		case "step":
			window.updatePlayers(Arrays.asList(message.players), message.size);
			break;
		case "message":
			break;
		}
	}
	
	public void fromPlayer(Message message)
	{
		server_h.writeToServer(message);
	}
	
	public String connect(String address, String name)
	{
		try
		{
			server_h = new ServerHandle(this, address, name);
		}
		catch (UnknownHostException ex)
		{
			//ex.printStackTrace();
			return "Unknown host";
		}
		catch (SocketException ex)
		{
			return "Incorrect host";
		}
		catch (Exception ex) 
		{
			//ex.printStackTrace();
			return "Unknown error";
		}
		return "OK";
	}
	
	public void startServerH()
	{
		server_h.start();
	}
	
	public void stop()
	{
		server_h.stop(0);
		System.out.println("Close");
	}
}
