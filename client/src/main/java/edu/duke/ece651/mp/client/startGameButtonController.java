package edu.duke.ece651.mp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class startGameButtonController {

  @FXML
  private Button startGame;

  @FXML
  private TextArea connection_status;

  @FXML
  void onStartGameButton(MouseEvent event) {

    try {
      connection_status.clear();
      // build Connection with Server
      System.out.println("Welcome to our game!");
      int port = 8080;
      String servername = "127.0.0.1";
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      Client theClient = new Client(servername, port, input, System.out);
      connection_status.appendText("Connected to server! Waiting for my identity...");

      theClient.theTextPlayer.initiateGame();
      System.out.println("Successfully connect to Server!");

      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/gamepage.fxml"));
        String player_color = theClient.theTextPlayer.identity;
        System.out.println("The player's color is: " + player_color);
        // primaryStage.setTitle("RISC GAME")
        AnchorPane root = (AnchorPane) loader.load();
        // Scene scene = new Scene(root);
        // stage.setScene(scene);

        GameController gameController = loader.getController();
        // String player_info = theClient.theTextPlayer.identity;
        // gameController.setName(player_info);

        gameController.setPlayer(theClient.theTextPlayer);
        gameController.initGame();

        startGame.getScene().setRoot(root);

      } catch (Exception e) {
        e.printStackTrace();
      }

    } catch (Exception e) {
      connection_status.setText("Server is not running. Please try later...");
    }
  }

}
