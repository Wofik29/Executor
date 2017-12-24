package org.wolf.server;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import org.wolf.server.view.MainController;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Server Executor");

        final Game game = new Game(this);

        Thread gameLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                game.start();
            }
        });
        gameLoop.start();
        initRootLayout();
        showFirst();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("CLOSED");
                game.stop();
                try {
                    gameLoop.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
            mainController.setWindow(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
