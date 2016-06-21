package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import other.Message;

/*
 * Инкапсулиреут действия с сервером и прослушкой порта.
 * При подключении создает Handle на клиента и запускает его.
 * Пересылает сообщение выше в Game
 */
public class Server {
	private ServerSocket server;
	private Thread listen_port;
	private Game game;
	private List<ClientHandle> clients;
	private AtomicBoolean isRead;
	
	public Server(Game g) {
		game = g;
		isRead = new AtomicBoolean(true);
		clients = new ArrayList<>();
		try {
			server = new ServerSocket(7498);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		listen_port = new Thread(new Runnable()	{
			@Override
			public void run() {
				listeningPort();
			}
		});
	}
	
	public void addClient(ClientHandle ch)	{
		clients.add(ch);
	}
	
	public void deleteClient(ClientHandle ch) {
		if (clients.contains(ch)) {
			clients.remove(ch);
			System.out.println("Server: Client - "+ch.getName()+" - is delete");
		}
	}
	
	public void start()	{
		listen_port.start();
		try	{
			System.out.println("System: Server start! Your ip - "+server.getLocalSocketAddress());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean isContains(String name) {
		for (ClientHandle ch: clients) {
			if (ch.getName().equals(name)) return true;
		}
		return false;
	}
	
	public void stop() {
		isRead.set(false);
		for (ClientHandle ch : clients)	{
			ch.stop(0);
		}
		listen_port.interrupt();
		try	{
			server.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void sendToGame(Message message) {
		game.fromServer(message);
	}
	
	public void writeToClient(Message message) {
		switch (message.type) {
		case "error":
			Message mess = new Message("message");
			mess.text = "Player "+message.name+" make mistake";
			for (ClientHandle ch : clients) {
				if (message.name.equals(ch.getName())) {
					ch.writeToClient(message);
					break;
				}
				else {
					ch.writeToClient(mess);
				}
			}
		case "map":
			for (ClientHandle ch : clients)	{
				if (message.name.equals(ch.getName())) {
					ch.writeToClient(message);
					break;
				}
			}
			break;
		case "stop":
		case "winner":
		case "deletePlayer":
		case "message":
		case "step":
			for (ClientHandle ch : clients) {
				ch.writeToClient(message);
			}
			break;
		case "addPlayer":
			for (ClientHandle ch : clients) {
				if (!message.name.equals(ch.getName())) {
					ch.writeToClient(message);
					break;
				}
			}
			break;
		default:
			System.out.println("Server: Unknown type message - "+message.type);
			for (ClientHandle ch : clients) {
				ch.writeToClient(message);
			}
		}
	}
	
	private void listeningPort() {
		System.out.println("Server: Wait clients...");
		while (isRead.get()) {
			try	{
				Socket client = server.accept();
				ClientHandle ch = new ClientHandle(client, this);
				ch.start();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
