package org.wolf.Game.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.wolf.server.Server;

public class ConnectController {
    private Server server;

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

    public void setServer(Server server) {
        this.server = server;
    }

    @FXML
    private void actonListenerCheckServer(Event event) {
        System.out.println("Pressed check server");
    }

    @FXML
    private void actonListenerCheckName(Event event) {
        System.out.println("Pressed check name");
    }

    @FXML
    private void actionListenerConnect(Event event) {
        System.out.println("Pressed connect");
    }
}
