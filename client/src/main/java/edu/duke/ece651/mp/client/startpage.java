package edu.duke.ece651.mp.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class startpage extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        URL xmlResource = getClass().getResource("/ui/firstpage.fxml");
        StackPane root = FXMLLoader.load(xmlResource);
        Scene scene = new Scene(root, 800, 640);
        stage.setScene(scene);
        stage.show();

    }
}
