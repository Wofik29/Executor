package org.wolf.Game;

import java.net.SocketException;
import java.net.UnknownHostException;

import org.wolf.other.Message;

public class Game {
	private Window window;
	private ServerHandle serverHandle;
	
	public static boolean isDebug = false;
	public static boolean isError = true;

	public Game(ServerHandle serverHandle) {
		this.serverHandle = serverHandle;
	}

	public void start()	{
		//window_connect = new WindowConnect(this);
		Compiller.setCommands("Executer.ini");
	}
	
	public void fromServer(Message message)	{
		switch (message.type) {
		case "connected":
			//window_connect.connectSet();
			break;
		case "dublicateName":
			//window_connect.setLabelText("Имя уже использовано");
			break;
		case "map":
/*			if (message.map != null && message.map.length > 0 )	{
				window = new Window(this);
				window.start();
				window_connect.setShow(false);
				window.map = message.map.clone();
				Player p = new Player(message.player);
				window.addPlayer(p);
				serverHandle.setName(p.getName());
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
			}*/
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
				serverHandle.writeToServer(message);
				//window.setMsg("Wait other players...");
			}
			catch (Exception ex) {
				if (isError) ex.printStackTrace();
				//window.setMsg(ex.getMessage());
			}
		}
		else {
			serverHandle.writeToServer(message);
		}
		
	}
	
	public String connect(String address) {
		return "";
	}
	
	public void sendName(String name) {
		serverHandle.registerName(name);
	}
	
	public void startServerH() {
		serverHandle.start();
	}
	
	public void stop() {
		if (serverHandle != null) serverHandle.stop(0);
	}
}
