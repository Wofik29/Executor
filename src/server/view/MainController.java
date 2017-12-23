package server.view;

import server.Window;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MainController {

    private Window window;

    @FXML
    private Canvas canvas;
    private int size = 20;

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

    private void paintCell(int x, int y, Paint p) {
        canvas.getGraphicsContext2D().setFill(p);
        canvas.getGraphicsContext2D().fillRect(x, y, size, size);

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

    private void clear() {
        GraphicsContext gx = canvas.getGraphicsContext2D();
        gx.setFill(Color.WHITE);
        gx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void paint() {
        clear();
        paintCell(offsetX, offsetY, Color.RED);
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
