package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import other.Message;

/*
 * Указывает на клиента, принимает и отсылает ему сообщения. 
 */
public class ClientHandle 
{
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Thread listen_client;
	private String name;
	private Server server;
	private AtomicBoolean isRead;
	
	public ClientHandle(Socket sock, Server serv) 
	{
		socket = sock;
		server = serv;
		isRead = new AtomicBoolean(true);
		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			name = in.readUTF();
			System.out.println("ClientHandle("+name+"): New client connected - "+name);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		listen_client = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				listeningClient();
			}
		});
	}
	
	public String getName()
	{
		return name;
	}
	
	public void start()
	{
		listen_client.start();
	}
	
	public void stop(int status)
	{
		isRead.set(false);
		try
		{
			if (status == 0) 
			{
				out.writeObject(new Message("exit"));
			}
			else 
			{
				server.deleteClient(this);
				System.out.println("ClientHandle("+name+"): Client has disconnected");
			}
			in.close();
			out.close();
			listen_client.interrupt();
			socket.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void writeToClient(Message message)
	{
		try
		{
			System.out.println("ClientHandle("+name+") - write to client: "+message.type);
			out.writeObject(message);
			out.flush();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void listeningClient()
	{
		while (isRead.get())
		{
			Message message = null;
			try
			{
				Object messageO = in.readObject();
				message = (Message) messageO;
				if ("exit".equals(message.type))
				{
					server.deleteClient(this);
					stop(-1);
				}
				else
					server.sendToGame(message);
			}
			catch (Exception ex)
			{
				stop(-1);
				ex.printStackTrace();
			}
		}
	}
}
