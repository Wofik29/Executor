package org.wolf.Game.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.wolf.Game.Window;

public class ConnectController {
    private Window window;

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

    @FXML
    private void actonListenerCheckServer(Event event) {
        String result = window.createConnect(serverName.getText());
        if (result.equals("Success")) {
            connectMessage.setTextFill(Color.GREEN);
        } else {
            connectMessage.setTextFill(Color.RED);
        }
        connectMessage.setText(result);
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
