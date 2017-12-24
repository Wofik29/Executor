package org.wolf.server;
import org.wolf.other.Message;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private ServerSocket server;
    private Thread listenPort;
    private Game game;
    private AtomicBoolean isRead;
    private List<ClientHandle> clients;

    public Server(Game game) {
        this.game = game;
        isRead = new AtomicBoolean(true);
        clients = new ArrayList<ClientHandle>();

        try {
            server = new ServerSocket(7498);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        listenPort = new Thread(new Runnable() {
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
            System.out.println("Server: Client "+ch.getName()+" is deleted");
        }
    }

    public void start()	{
        listenPort.start();
        try	{
            System.out.println("System: Server start! Your ip - "+server.getLocalSocketAddress());
        }
        catch (Exception ex) {
            if (Game.isError) ex.printStackTrace();
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
        listenPort.interrupt();
        try	{
            server.close();
        }
        catch (Exception ex) {
            if (Game.isError) ex.printStackTrace();
        }
    }

    public void sendToGame(Message message) {
        game.fromServer(message);
    }

    private void listeningPort() {
        while (isRead.get()) {
            try {
                Socket client = server.accept();
                ClientHandle clientHandle = new ClientHandle(client, this);
                clientHandle.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void writeToClient(Message message) {
        switch (message.type) {
            case "error":
                Message mess = new Message("error");
                mess.text = "Player "+message.name+" make mistake";
                mess.name = message.name;
                for (ClientHandle ch : clients) {
                    if (message.name.equals(ch.getName())) {
                        ch.writeToClient(message);
                    }
                    else {
                        ch.writeToClient(mess);
                        System.out.println("write to client "+mess.text);
                    }
                }
            case "map":
                for (ClientHandle ch : clients)	{
                    if (message.name.equals(""))
                        ch.writeToClient(message);
                    else if (message.name.equals(ch.getName())) {
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

}
