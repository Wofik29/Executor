package org.wolf.Game;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import org.wolf.Game.view.ConnectController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.wolf.other.Message;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Window extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private ServerHandle serverHandle;

    private String stage = "connect";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Connect Stage");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                serverHandle.stop(0);
            }
        });

        initLayout();
        setCurrentStage("connect");
    }

    private void initLayout() {
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

    public void setCurrentStage(String newStage) {
        switch (newStage) {
            case "connect":
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("view/Connect.fxml"));
                    AnchorPane main = (AnchorPane) loader.load();

                    BorderPane pane = (BorderPane) rootLayout.getChildren().get(0);
                    pane.setCenter(main);

                    ConnectController connectController = loader.getController();
                    connectController.setWindow(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
