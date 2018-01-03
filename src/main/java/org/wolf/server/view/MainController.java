package org.wolf.server.view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.wolf.server.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainController implements Observer {

    private Window window;
    private Server server;

    @FXML
    private Canvas canvas;

    @FXML
    private VBox listPlayers;

    private int size = 20;
    private int step = 20;

    private int offsetX = 0;
    private int offsetY = 0;

    private boolean isStepRight = false;
    private boolean isStepLeft = false;
    private boolean isStepUp = false;
    private boolean isStepDown = false;

    public MainController() {
    }

    @FXML
    private void initialize() {
        canvas.setFocusTraversable(true);
        AnimationTimer timer = new MyTimer();
        timer.start();

    }

    @Override
    public void update(Observable o, Object arg) {
        Server server = (Server) o;
        ArrayList<ClientHandle> clients = (ArrayList<ClientHandle>) server.getClients();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listPlayers.getChildren().clear();

                for (ClientHandle client : clients) {
                    Label label = new Label();
                    label.setText(client.getName());
                    listPlayers.getChildren().add(label);
                }
            }
        });

    }

    private void paintCell(int x, int y, int w, int h) {
        canvas.getGraphicsContext2D().fillRect(x, y, w, h);
    }

    @FXML
    private void actionListenerPressedKey(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
            case A:
                isStepLeft = true;
                break;
            case RIGHT:
            case D:
                isStepRight = true;
                break;
            case UP:
            case W:
                isStepUp = true;
                break;
            case DOWN:
            case S:
                isStepDown = true;
                break;
        }
    }

    @FXML
    private void actionListenerReleasedKey(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
            case A:
                isStepLeft = false;
                break;
            case RIGHT:
            case D:
                isStepRight = false;
                break;
            case UP:
            case W:
                isStepUp = false;
                break;
            case DOWN:
            case S:
                isStepDown = false;
                break;
        }
    }

    public void setWindow(Window win) {
        this.window = win;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    private void clear() {
        GraphicsContext gx = canvas.getGraphicsContext2D();
        gx.setFill(Color.WHITE);
        gx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void paint() {
        clear();

        GraphicsContext gx = canvas.getGraphicsContext2D();
        byte[][] map = World.map;
        if (map != null) {
            for (int i=0; i<map.length; i++)
                for (int j=0; j<map[i].length; j++) {
                    switch (map[i][j]) {
                        case Map.SHALLOW:
                            gx.setFill(Color.rgb(0,192,255));
                            break;
                        case Map.SHIP:
                            gx.setFill(Color.BLACK);
                            break;
                        case Map.JEWEL:
                            gx.setFill(Color.RED);
                            break;
                        default:
                            gx.setFill(Color.rgb(255,217,0));
                            break;
                    }
                    paintCell(i*step+offsetX, j*step+offsetY, step, step);
                }
        }

    }

    private void step() {
        if (isStepDown) {
            offsetY++;
        }

        if (isStepUp) {
            offsetY--;
        }

        if (isStepRight) {
            offsetX++;
        }

        if (isStepLeft) {
            offsetX--;
        }
    }

    private void update() {
        paint();
        step();
    }

    private class MyTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
            update();
        }
    }


}
