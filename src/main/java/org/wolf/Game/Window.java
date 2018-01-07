package org.wolf.Game;

import javafx.event.EventHandler;
import javafx.scene.control.SplitPane;
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

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                if (serverHandle != null) {
                    serverHandle.stop(0);
                }
            }
        });

        initLayout();
        setCurrentStage("game");
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
                    this.primaryStage.setTitle("Connect Stage");
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
            case "game":
                try {
                    this.primaryStage.setTitle("Game");
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("view/Played.fxml"));
                    SplitPane main = (SplitPane) loader.load();

                    primaryStage.setX(10);
                    primaryStage.setY(10);
                    primaryStage.setHeight(1000);
                    primaryStage.setWidth(1240);
                    main.setPrefWidth(1240);
                    main.setPrefHeight(900);

                    BorderPane pane = (BorderPane) rootLayout.getChildren().get(0);
                    pane.setCenter(main);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }
}
