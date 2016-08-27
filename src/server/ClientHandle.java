package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import other.Message;

/*
 * Инкапсулирует взаимодействие с клиентом.
 * Создает потоки ввода и вывода.
 * Перенаправляет сообщения серверу. 
 */
public class ClientHandle {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Thread listen_client;
	private String name;
	private Server server;
	private AtomicBoolean isRead;
	
	public ClientHandle(Socket sock, Server serv) {
		socket = sock;
		server = serv;
		isRead = new AtomicBoolean(true);
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception ex) {
			if (Game.isError) ex.printStackTrace();
		}
		
		listen_client = new Thread(new Runnable() {
			@Override
			public void run() {
				listeningClient();
			}
		});
	}
	
	public String getName()	{
		return name;
	}
	
	public void start()	{
		listen_client.start();
	}
	
	/*
	 * Ждет имя клиента и проверяет на дублирования.
	 * Выходит, когда дублирования не будет найдено
	 */
	public void getNameClient()	{
		boolean isSet = true;
		
		Message message = new Message("connected");
		writeToClient(message);
		System.out.println("Соединение установлено. Ожидаем имя...");
		while (isSet) {
			try	{
				System.out.println("");
				message = (Message) in.readObject();
				if (server.isContains(message.name)) {
					out.writeObject(new Message("duplicateName"));
					out.flush();
				} else {
					name = message.name;
					isSet = false;
					server.addClient(this);
					server.sendToGame(message);
				}
			}
			catch (Exception ex) {
				if (Game.isError) ex.printStackTrace();
				stop(-1);
			}
		}
	}
	
	public void stop(int status) {
		isRead.set(false);
		try {
			if (status == 0) {
				out.writeObject(new Message("exit"));
			} else {
				System.out.println("ClientHandle("+name+"): Client has disconnected");
				server.deleteClient(this);
			}
			in.close();
			out.close();
			listen_client.interrupt();
			socket.close();
		}
		catch (Exception ex) {
			if (Game.isError) ex.printStackTrace();
		}
	}
	
	public void writeToClient(Message message) {
		try	{
			//System.out.println("ClientHandle("+name+") - write to client: "+message.type);
			out.writeObject(message);
			out.flush();
		}
		catch (Exception ex) {
			if (Game.isError) ex.printStackTrace();
		}
	}
	
	private void listeningClient() {
		getNameClient();
		while (isRead.get()) {
			Message message = null;
			try {
				Object messageO = in.readObject();
				message = (Message) messageO;
				if ("exit".equals(message.type)) {
					server.deleteClient(this);
					server.sendToGame(message);
					stop(-1);
				}
				else
					server.sendToGame(message);
			}
			catch (Exception ex) {
				if (Game.isError) ex.printStackTrace();
				stop(-1);
			}
		}
	}
}
