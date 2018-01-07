package org.wolf.Game.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.wolf.server.Map;

import java.util.HashMap;

public class CanvasPane extends Pane {
    private final Canvas canvas;

    private Image groundTexture;
    private int offsetX;
    private int offsetY;
    private final int widthGround = 64;

    private HashMap<Integer , int[]> coordTexture = new HashMap<Integer, int[]>(70);

    public CanvasPane(double width, double height) {
        canvas = new Canvas(width, height);
        getChildren().add(canvas);
        groundTexture = new Image("file:D:/Java/Executor/res/Spritesheet.png");

        offsetX = 300;
        offsetY = 0;

        setOther();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private void setOther()	{
        coordTexture.put(0, new int[]{5,0}); // grass
        coordTexture.put(1, new int[]{0,0}); // deep
        coordTexture.put(2, new int[]{1,4}); // beach
        coordTexture.put(3, new int[]{2,3}); // shallow
        coordTexture.put(4, new int[]{0,3}); // grass-beach-in-1
        coordTexture.put(5, new int[]{4,3}); // grass-beach-in-2
        coordTexture.put(6, new int[]{1,3}); // grass-beach-in-3
        coordTexture.put(7, new int[]{5,2}); // grass-beach-in-4
        coordTexture.put(8, new int[]{5,4}); // grass-beach-out-1
        coordTexture.put(9, new int[]{5,3}); // grass-beach-out-2
        coordTexture.put(10, new int[]{7,2}); // grass-beach-out-3
        coordTexture.put(11, new int[]{3,4}); // grass-beach-out-4
        coordTexture.put(12, new int[]{6,2}); // grass-beach-1
        coordTexture.put(13, new int[]{6,3}); // grass-beach-2
        coordTexture.put(14, new int[]{7,4}); // grass-beach-3
        coordTexture.put(15, new int[]{3,3}); // grass-beach-4
        coordTexture.put(16, new int[]{2,0}); // beach-shallow-in-1
        coordTexture.put(17, new int[]{7,0}); // beach-shallow-in-2
        coordTexture.put(18, new int[]{6,0}); // beach-shallow-in-3
        coordTexture.put(19, new int[]{2,4}); // beach-shallow-in-4
        coordTexture.put(20, new int[]{0,4}); // beach-shallow-out-1
        coordTexture.put(21, new int[]{0,1}); // beach-shallow-out-2
        coordTexture.put(22, new int[]{6,4}); // beach-shallow-out-3
        coordTexture.put(23, new int[]{4,0}); // beach-shallow-out-4
        coordTexture.put(24, new int[]{1,0}); // shallow-deep-in-1
        coordTexture.put(25, new int[]{0,2}); // shallow-deep-in-2
        coordTexture.put(26, new int[]{2,1}); // shallow-deep-in-3
        coordTexture.put(27, new int[]{1,1}); // shallow-deep-in-4
        coordTexture.put(28, new int[]{7,1}); // shallow-deep-out-1
        coordTexture.put(29, new int[]{1,2}); // shallow-deep-out-2
        coordTexture.put(30, new int[]{2,2}); // shallow-deep-out-3
        coordTexture.put(31, new int[]{6,1}); // shallow-deep-out-4
        coordTexture.put(32, new int[]{4,1}); // shallow-deep-1
        coordTexture.put(33, new int[]{5,1}); // shallow-deep-2
        coordTexture.put(34, new int[]{3,1}); // shallow-deep-3
        coordTexture.put(35, new int[]{3,0}); // shallow-deep-4
        coordTexture.put(36, new int[]{7,3}); // beach-shallow-1
        coordTexture.put(37, new int[]{3,2}); // beach-shallow-2
        coordTexture.put(38, new int[]{4,2}); // beach-shallow-3
        coordTexture.put(39, new int[]{4,4}); // beach-shallow-4
        coordTexture.put(40, new int[]{2,3}); // ship который shallow
        coordTexture.put(41, new int[]{0,5}); // Jewel

/*
        coord_tex_ship.put(0,new int[]{1,1});
        coord_tex_ship.put(1,new int[]{0,0});
        coord_tex_ship.put(2,new int[]{0,1});
        coord_tex_ship.put(3,new int[]{1,0});
*/
    }

    public void drawMap() {
        byte map[][] = Map.getMap();
        int[] currentCell;

        int width = 64;
        int height = 32;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                currentCell = coordTexture.get((int) map[x][y]);

                float sx = x*width/2;
                float sy = y*height;
                float _sx = sx - sy + offsetX;
                float _sy = (sx+sy)/2 + offsetY;

                canvas.getGraphicsContext2D().drawImage(groundTexture, currentCell[0] * widthGround, currentCell[1] * widthGround, widthGround, widthGround, _sx, _sy, widthGround, widthGround);
            }
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();

        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("W: " + w);
        System.out.println("H: " + h);

        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);

        clear();
        drawMap();
    }

    public void clear() {
        GraphicsContext gx = canvas.getGraphicsContext2D();
        gx.setFill(Color.WHITE);
        gx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}

