package Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import other.Message;

public class ServerHandle {
	private Socket socket;
	private String name;
	private Thread listen_thread;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Game game;
	private AtomicBoolean isRead;
	
	public ServerHandle(Game g, String addres) throws UnknownHostException, SocketException	{
		try	{
			socket = new Socket(addres, 7498);
			isRead = new AtomicBoolean(true);
			game = g;
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (UnknownHostException ex)	{
			throw ex;
		}
		catch (SocketException ex) {
			throw ex;
		}
		catch (IOException ex)	{
			if (Game.isError) ex.printStackTrace();
		}
		
		listen_thread = new Thread( new Runnable() {
			@Override
			public void run() {
				listeningServer();
			}
		});
	}
	
	public void start() {
		listen_thread.start();
	}
	
	public void stop(int status) {
		isRead.set(false);
		try	{
			if (status == 0) {
				Message message = new Message(name,"exit","");
				writeToServer(message);
			}
			in.close();
			out.close();
			listen_thread.interrupt();
			socket.close();
		}
		catch (Exception ex) {
			if (Game.isError) ex.printStackTrace();
		}
	}
	
	private void listeningServer() {
		while (isRead.get()) {
			Message message = null;
			try	{
				message = (Message) in.readObject();
				System.out.println("Sending message - "+message.type);
				if ("exit".equals(message.type)) {
					stop(1);
				}
				game.fromServer(message);
			}
			catch (Exception ex) {
				if (Game.isError) ex.printStackTrace();
			}
		}
	}
	
	public void registerName(String name) {
		Message message = new Message(name, "register", "");
		writeToServer(message);
	}
	
	public void writeToServer(Message message) {
		try	{
			out.writeObject(message);
			out.flush();
		}
		catch (Exception ex) {
			if (Game.isError) ex.printStackTrace();
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
