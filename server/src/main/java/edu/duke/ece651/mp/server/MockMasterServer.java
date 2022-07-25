package edu.duke.ece651.mp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.mp.common.Turn;
import edu.duke.ece651.mp.common.TurnList;

public class MockMasterServer extends MasterServer{
  public MockMasterServer(int port, int num_players) throws IOException {
    super(port, num_players);
  }

  @Override
  public int getPort() {
    return this.port;
  }

  @Override
  public void acceptPlayers() throws IOException {
    int connectedPlayers = 0;
    System.out.println("Server is waiting...");
    while (connectedPlayers < num_players) {
      player_socket = null;
      player_socket_list.add(player_socket);
      connectedPlayers++;
    }
    System.out.println("Server is connected to ALL the players.");

  }

  /**
   * Method to send any object over the socket
   * 
   * @param Object to send
   */
  @Override
  public void sendToAll(Object obj) {
    // empty as we don't wanna send anything while mocking
  }

  /* Close Server Socket. */
  @Override
  public void close() throws IOException {

  }

  /**
   * Method to send Player Identity(Green/Yellow) over the socket
   * 
   * @param Object to send
   */
  @Override
  public void sendPlayerIdentityToAll(List<String> players_identity) {
    System.out.println("Sending color to player: Green");
    System.out.println("Sending color to player: Blue");
  }

  @Override
  public void receiveTurnListFromAllPlayers() throws IOException, ClassNotFoundException, InterruptedException {
    int connectedPlayers = 0;
    ArrayList<String> players_colors = new ArrayList<String>();
    players_colors.add("Green");
    players_colors.add("Blue");

    while (connectedPlayers < num_players) {
      TurnList turn_list = new TurnList(players_colors.get(connectedPlayers));
      this.all_order_list.add(turn_list);
      connectedPlayers++;
    }
    System.out.println("Server received lists of orders from all players.");
  }



}
