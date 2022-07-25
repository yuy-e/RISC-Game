package edu.duke.ece651.mp.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class startpage extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        URL xmlResource = getClass().getResource("/ui/firstpage.fxml");
        AnchorPane root = FXMLLoader.load(xmlResource);
        Scene scene = new Scene(root, 694, 453);
        stage.setScene(scene);
        stage.show();

    }
}
