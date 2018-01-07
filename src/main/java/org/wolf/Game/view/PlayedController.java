package org.wolf.Game.view;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class PlayedController {

    Canvas canvas;

    @FXML
    AnchorPane anchorCanvas;

    @FXML
    SplitPane splitPane;

    @FXML
    private void initialize() {
        System.out.println("INIT");
        CanvasPane canvasPane = new CanvasPane(500, 500);
        canvas = canvasPane.getCanvas();
        splitPane.getItems().add(canvasPane);

        Image image = new Image("file:D:/Java/Executor/res/ship1.png");
        Image image1 = new Image("file:D:/Java/Executor/res/Spritesheet.png");

        new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                canvasPane.drawMap();
/*
                canvasPane.clear();

                int x1 = 0 % 2;
                int y1 = 0 / 2;

                int x2 = 0 % 8;
                int y2 = 0 % 8;

                canvas.getGraphicsContext2D().drawImage(image1, x2 * 64, y2 * 64, 64, 64, 0, 0, 64, 64);
                canvas.getGraphicsContext2D().drawImage(image1, x2 * 64, y2 * 64, 64, 64, 64, 0, 64, 64);
                canvas.getGraphicsContext2D().drawImage(image1, x2 * 64, y2 * 64, 64, 64, 32, 16, 64, 64);
                canvas.getGraphicsContext2D().drawImage(image, x1 * 128, y1 * 128, 128, 128, 32, 16, 64, 64);
*/
            }
        }.start();

    }
}
