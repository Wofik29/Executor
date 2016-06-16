package Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import other.Message;
import other.SPlayer;

public class ServerHandle 
{
	private Socket socket;
	private String name;
	private Thread listen_thread;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Game game;
	private AtomicBoolean isRead;
	
	public ServerHandle(Game g, String addres, String n) throws UnknownHostException, SocketException
	{
		try
		{
			socket = new Socket(addres, 7498);
			isRead = new AtomicBoolean(true);
			name = n;
			game = g;
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeUTF(name);
		}
		catch (UnknownHostException ex)
		{
			throw ex;
		}
		catch (SocketException ex)
		{
			throw ex;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		
		listen_thread = new Thread( new Runnable() 
		{
			@Override
			public void run() 
			{
				listeningServer();
			}
		});
	}
	
	public void start()
	{
		listen_thread.start();
		Message message = new Message("register");
		message.name = name;
		writeToServer(message);
	}
	
	public void stop(int status)
	{
		isRead.set(false);
		try
		{
			if (status == 0)
			{
				Message message = new Message(name,"exit","");
				writeToServer(message);
			}
			in.close();
			out.close();
			listen_thread.interrupt();
			socket.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void listeningServer()
	{
		while (isRead.get())
		{
			Message message = null;
			try
			{
				message = (Message) in.readObject();
				if ("exit".equals(message.type))
				{
					stop(1);
				}
				game.fromServer(message);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void writeToServer(Message message)
	{
		try
		{
			out.writeObject(message);
			out.flush();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
