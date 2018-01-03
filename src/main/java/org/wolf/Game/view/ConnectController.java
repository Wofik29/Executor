package org.wolf.Game.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.wolf.Game.ServerHandle;
import org.wolf.Game.Window;
import org.wolf.other.Message;
import org.wolf.server.Server;

import java.net.SocketException;
import java.net.UnknownHostException;

public class ConnectController {
    private Window window;
    private ServerHandle server;

    @FXML
    private TextField nickname;

    @FXML
    private TextField serverName;

    @FXML
    private Label connectMessage;

    @FXML
    private Label nameMessage;

    private void initialize() {
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public ServerHandle getServer() {
        return server;
    }

    @FXML
    private void actonListenerCheckServer(Event event) {
        String result = "Success";

        try	{
            server = new ServerHandle(serverName.getText());
        }
        catch (UnknownHostException ex)	{
            result = "Unknown host";
        }
        catch (SocketException ex) {
            result = "Incorrect host";
        }
        catch (Exception ex) {
            result = "Unknown error";
        }

        if (result.equals("Success")) {
            connectMessage.setTextFill(Color.GREEN);
        } else {
            connectMessage.setTextFill(Color.RED);
        }
        connectMessage.setText(result);
    }

    @FXML
    private void actonListenerCheckName(Event event) {
        String result = "Success";
        String name = nickname.getText();
        Message message = server.registerName(name);

        if (message.type.equals("duplicateName")) {
            result = "That name already exist";
        } else if (message.type.equals("SuccessRegister")) {
            server.setName(name);
            result = "Success";
        }

        if (result.equals("Success")) {
            nameMessage.setTextFill(Color.GREEN);
        } else {
            nameMessage.setTextFill(Color.RED);
        }
        nameMessage.setText(result);
    }

    @FXML
    private void actionListenerConnect(Event event) {
        System.out.println("Pressed connect");
    }
}
