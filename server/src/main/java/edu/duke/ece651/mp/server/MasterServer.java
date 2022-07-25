package edu.duke.ece651.mp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.net.Socket;
import java.util.ArrayList;
import edu.duke.ece651.mp.common.V1Map;
import edu.duke.ece651.mp.common.Map;
import edu.duke.ece651.mp.common.Territory;
import edu.duke.ece651.mp.common.Turn;
import edu.duke.ece651.mp.common.TurnList;

public class MasterServer {
  public int port;
  public ServerSocket server_socket;
  public int num_players;
  public ArrayList<Socket> player_socket_list;
  public Socket player_socket;
  public ArrayList<TurnList> all_order_list;

  public MasterServer(int port, int num_players) throws IOException {
    this.port = port;

    if (port != 0) {
      try {
        // create socket and bind it to port
        this.server_socket = new ServerSocket(port);
      } catch (Exception e) {
        // print exception message about Throwable object
        e.printStackTrace();
      }
    } else {
      this.server_socket = null;
    }

    this.num_players = num_players;
    this.player_socket_list = new ArrayList<Socket>();
    this.all_order_list = new ArrayList<TurnList>();
  }

  public int getPort() {
    return this.port;
  }

  public void acceptPlayers() throws IOException, InterruptedException {
    int connectedPlayers = 0;
    System.out.println("Server is waiting...");
    while (connectedPlayers < num_players) {
      player_socket = server_socket.accept();
      player_socket_list.add(player_socket);
      connectedPlayers++;
      PlayerThread pth = new PlayerThread(player_socket);
      Thread t = new Thread(pth);

      t.start();
      t.join();
      // String rev = (String)pth.obj;
      // System.out.print(rev);
    }
    System.out.println("Server is connected to ALL the players.");
  }

  /**
   * Method to send any object over the socket
   * 
   * @param Object to send
   * @throws IOException
   */
  public void sendToAll(Object obj) throws IOException {
    // iterate through the player socket list
    for (Socket soc : player_socket_list) {
      sendToPlayer(obj, soc);
    }
  }

  /**
   * Method to send object to a specific player
   * 
   * @param object to send and player's socket
   * @throws IOException
   */
  public void sendToPlayer(Object obj, Socket soc) {
    try {
      OutputStream o = soc.getOutputStream();
      ObjectOutputStream s = new ObjectOutputStream(o);

      s.writeObject(obj);
      s.flush();
      // s.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("Error during serialization");
      e.printStackTrace();
    }
  }

  /* Close Server Socket. */
  public void close() throws IOException {
    server_socket.close();
    this.close_clients();
  }

  /* Close All Client Sockets. */
  public void close_clients() throws IOException {
    for (int i = 0; i < player_socket_list.size(); ++i) {
      player_socket_list.get(i).close();
    }
  }

  /**
   * Method to send Player Identity(Green/Yellow) over the socket
   * 
   * @param Object to send
   * @throws IOException
   */
  public void sendPlayerIdentityToAll(List<String> players_identity) {
    for (int i = 0; i < player_socket_list.size(); ++i) {
      String player_color = players_identity.get(i);
      System.out.println("Sending color to player: " + player_color);
      sendToPlayer(player_color, player_socket_list.get(i));
    }
  }

  /**
   * Method to receive Object over the socket
   * 
   * @return Object received from player
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Object receiveObjectFromPlayer(Socket player_socket) throws IOException, ClassNotFoundException {
    InputStream o = player_socket.getInputStream();
    ObjectInputStream s = new ObjectInputStream(o);
    Object obj = s.readObject();
    return obj;
  }

  /**
   * Method to receive Object over the socket from all players
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws InterruptedException
   */
  @SuppressWarnings("unchecked")
  public void receiveTurnListFromAllPlayers() throws IOException, ClassNotFoundException, InterruptedException {
    // ArrayList<Turn<Character>> order_list = new ArrayList<Turn<Character>>();
    int connectedPlayers = 0;

    while (connectedPlayers < num_players) {
      PlayerThread<Character> th = new PlayerThread<Character>(player_socket_list.get(connectedPlayers));
      Thread t = new Thread(th);
      t.start();
      t.join();
      TurnList turn_list = (TurnList) th.obj;
      this.all_order_list.add(turn_list);
      connectedPlayers++;
      // System.out.println("receive TurnList");
      // printTurnList();

    }
    System.out.println("Server received lists of orders from all players.");
  }

  /**
   * 
   * method to detect which player has won return the winner color 
   * @returns player's color if they won OR null
   * 
   */

  public String detectresult(Map<Character> theMap){
    String color=null;
    int flag=0;//one winner has lost
    HashMap<String, Territory<Character>> myTerritories=theMap.getAllTerritories();
    for(String s:myTerritories.keySet()){
      Territory<Character> terr=myTerritories.get(s);
      if(terr.getUnit()==0){continue;}
      if(!terr.getColor().equals(color)&&color!=null){
        flag=1;
        color=null;
        break;
      }else if(color==null&&flag==0){
        color=terr.getColor();
      }
    }
    return color;

  }

  public void printTurnList() {
    for (TurnList tl : all_order_list) {
      tl.printTurnList();
    }
  }


}
