package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import other.Message;

/*
 * Прослушивает порт на подключение клиентов.
 * Пересылает сообщение выше в Game
 */
public class Server 
{
	private ServerSocket server;
	private Thread listen_port;
	private Game game;
	private List<ClientHandle> clients;
	private AtomicBoolean isRead;
	
	public Server(Game g)
	{
		game = g;
		isRead = new AtomicBoolean(true);
		clients = new ArrayList<>();
		try
		{
			server = new ServerSocket(7498);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		listen_port = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				listeningPort();
			}
		});
	}
	
	public void addClient(ClientHandle ch)
	{
		ch.start();
		
		clients.add(ch);
	}
	
	public void deleteClient(ClientHandle ch)
	{
		if (clients.contains(ch))
		{
			clients.remove(ch);
			System.out.println("Server: Client - "+ch.getName()+" - is delete");
		}
	}
	
	public void start()
	{
		listen_port.start();
		try
		{
			System.out.println("System: Server start! Your ip - "+server.getLocalSocketAddress());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void stop()
	{
		isRead.set(false);
		for (ClientHandle ch : clients)
		{
			ch.stop(0);
		}
		listen_port.interrupt();
		try
		{
			server.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void sendToGame(Message message)
	{
		game.fromServer(message);
	}
	
	public void writeToClient(Message message)
	{
		switch (message.type)
		{
		case "map":
		case "error":
			for (ClientHandle ch : clients)
			{
				if (message.name.equals(ch.getName()))
				{
					ch.writeToClient(message);
					break;
				}
			}
			break;
		case "deletePlayer":
		case "addPlayer":
		case "step":
			for (ClientHandle ch : clients)
			{
				ch.writeToClient(message);
			}
			break;
		case "message":
			break;
		default:
			System.out.println("Server: Unknown type message - "+message.type);
		}
	}
	
	private void listeningPort()
	{
		System.out.println("Server: Wait clients...");
		while (isRead.get())
		{
			try
			{
				Socket client = server.accept();
				ClientHandle ch = new ClientHandle(client, this);
				addClient(ch);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
