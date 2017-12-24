package org.wolf.server;

import org.wolf.other.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandle {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread listenerClient;
    private String name;
    private Server server;
    private AtomicBoolean isRead;

    public ClientHandle(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.isRead = new AtomicBoolean(true);

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            name = in.readUTF();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        listenerClient = new Thread(new Runnable() {
            @Override
            public void run() {
                listeningClient();
            }
        });
    }

    public void start() {
        listeningClient();
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
            listenerClient.interrupt();
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

    public String getName()	{
        return name;
    }
}
