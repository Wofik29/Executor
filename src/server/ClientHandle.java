package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandle {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread listenerClient;
    private String name;
    private Server server;
    private AtomicBoolean isRead;

    public ClientHandle(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.isRead = new AtomicBoolean(true);

        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
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

    }
}
