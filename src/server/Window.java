package server;

import javafx.scene.canvas.Canvas;
import server.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Window extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private MainController mainController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Server Executor");

        Game game = new Game(this);

        Thread gameLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                game.start();
            }
        });


        initRootLayout();
        showFirst();
        gameLoop.start();
    }

    public void draw() {

    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/Layout.fxml"));
            rootLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFirst() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Window.class.getResource("view/Main.fxml"));
            AnchorPane main = (AnchorPane) loader.load();

            BorderPane pane = (BorderPane) rootLayout.getChildren().get(0);
            pane.setCenter(main);

            MainController mainController = loader.getController();
            this.mainController = mainController;
            mainController.setWindow(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
