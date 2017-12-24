package org.wolf.Game;

import Game.view.ConnectController;
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

    private String stage = "connect";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Connect Stage");

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

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
