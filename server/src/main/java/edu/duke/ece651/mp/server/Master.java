package edu.duke.ece651.mp.server;

import edu.duke.ece651.mp.common.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Master {
  final MasterServer theMasterServer;
  public V2Map<Character> theMap;
  public ArrayList<String> players_identity;
  public ArrayList<TurnList> all_order_list;
  HandleOrder<Character> theHandleOrder;

  private FoodResourceList food_list;
  private TechResourceList tech_list;

  /**
   * Constructor
   * 
   * @throws IOException
   */
  public Master(int port, int num_players) throws IOException {
    if (port != 0) {
      this.theMasterServer = new MasterServer(port, num_players);
    } else {
      this.theMasterServer = new MockMasterServer(port, num_players);
    }
    this.players_identity = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    this.theMap = new V2Map<Character>(this.players_identity);
    this.all_order_list = new ArrayList<TurnList>();
    PathChecking<Character> pcheck = new PathChecking<>(null);
    OwnerChecking<Character> ocheck = new OwnerChecking<>(pcheck);

    // initialize the Resources at the begining of the game
    this.food_list = new FoodResourceList(players_identity);
    this.tech_list = new TechResourceList(players_identity);
    for (String playerColor : players_identity) {
      food_list.addResource(playerColor, new FoodResource(50));
      tech_list.addResource(playerColor, new TechResource(50));

    }
    this.theHandleOrder = new HandleOrder<Character>(this.all_order_list, theMap, ocheck, players_identity, food_list,
        tech_list);
  }

  /**
   * @throws IOException
   * @throws InterruptedException
   *
   */
  public void acceptPlayers() throws IOException, InterruptedException {
    this.theMasterServer.acceptPlayers();
  }

  /**
   * Method to send current version map to ALL the players
   * 
   * @throws IOException
   */
  public void sendMapToAll() throws IOException {
    // Eval-3: Fog of War Feature
    // We cannot send the actual map to all the players
    // We need hide the non-adjacent enemy territory details first before sending
    for (String playerColor : players_identity) {
      Socket playerSocket = theMasterServer.player_socket_Map.get(playerColor);

      // Get the version of the map with hidden non-adjacent enemy territory details
      // and hidden spy_map for enemy and hidden spies for enemies
      System.out.println("Sending map to player " + playerColor);
      theMasterServer.sendToPlayer((Object) getFogOfWarMap(playerColor), playerSocket);
    }
  }

  /**
   * Method to send current resources list to ALL the players
   *
   * @throws IOException
   */
  public void sendResourceToAll() throws IOException {
    theMasterServer.sendToAll(food_list);
    theMasterServer.sendToAll(tech_list);
  }

  public void close() throws IOException {
    this.theMasterServer.close();
  }

  /**
   * Method to send Player Color to ALL the players
   * 
   * @throws IOException
   */
  public void sendPlayerIdentityToAll() throws IOException {
    theMasterServer.sendPlayerIdentityToAll(players_identity);
  }

  /**
   * Method to send Player Color to ALL the players
   * 
   * @throws IOException
   */
  public void sendTurnStatusToAll(ArrayList<String> turnStatus) throws IOException {
    // Eval-3: Fog of War Feature
    // We cannot send the actual turn status to all the players as it may
    // information have spy upgrade and move that we don't want the enemy to know
    // We need hide these spy related information before sending to the enemy
    for (String playerColor : players_identity) {
      Socket playerSocket = theMasterServer.player_socket_Map.get(playerColor);

      // Get the version of the turn status with enemies spy related info hidden
      System.out.println("Sending turn status to player " + playerColor);
      theMasterServer.sendToPlayer((Object) getFogOfWarTurnStatus(playerColor, turnStatus), playerSocket);
    }
  }

  /**
   * int Method to receive and update orders from ALL players
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws InterruptedException
   */
  private void receiveTurnListFromAllPlayers() throws IOException, ClassNotFoundException, InterruptedException {
    theMasterServer.receiveTurnListFromAllPlayers();
    this.all_order_list = theMasterServer.all_order_list;
  }

  /**
   * Display in the server console the status of each order in the turn
   */
  private void displayTurnStatus(ArrayList<String> status_list) {
    System.out.println("--------------\nTurn Status:\n--------------\n");
    for (String turn_status : status_list) {
      System.out.println(turn_status);
    }
    System.out.println("\n");
  }

  /**
   * Method to handle orders
   * 
   * @return list of turn result
   */
  private ArrayList<String> handleOrders() {
    V2Map<Character> updatedMap = (V2Map<Character>) theHandleOrder.handleOrders(all_order_list, theMap);
    theMap = updatedMap;
    // update the resources
    food_list = theHandleOrder.getFoodList();
    tech_list = theHandleOrder.getTechList();

    theMasterServer.all_order_list.clear(); // reset the turn list

    ArrayList<String> status_list = new ArrayList<String>(theHandleOrder.turnStatus);
    theHandleOrder.turnStatus.clear(); // reset the list
    return status_list;
  }

  /**
   * Method to update players' resources at the end of each turn
   *
   * @return list of turn result
   */
  private void updatePlayerResource() {
    HashMap<String, Integer> food_l = theMap.getOwnersTerritoryFoodGroups();
    HashMap<String, Integer> tech_l = theMap.getOwnersTerritoryTechGroups();

    for (Map.Entry<String, Integer> s : food_l.entrySet()) {
      this.food_list.addResource(s.getKey(), new FoodResource(s.getValue()));
    }

    for (Map.Entry<String, Integer> s : tech_l.entrySet()) {
      this.tech_list.addResource(s.getKey(), new TechResource(s.getValue()));
    }
  }

  /**
   * cl Method to start a game by accepting players sending the players their
   * colors
   * 
   * @throws IOException, InterruptedException
   */
  public void initiateGame() throws IOException, InterruptedException {
    // Step-1:
    acceptPlayers();

    // Step-2:
    sendPlayerIdentityToAll();
  }

  /**
   * Method to play the game
   * 
   * @throws IOException, ClassNotFoundException, InterruptedException
   */
  public void playGame() throws IOException, ClassNotFoundException, InterruptedException {
    String gameStatus = "Ready for accepting turn!";
    while (true) { // main playing loop
      // Step-1: send map and resources to player
      sendMapToAll();
      sendResourceToAll();

      // Step-2:
      // Send game status to all players
      // Options: "Ready for accepting turn"
      // OR a player lost
      theMasterServer.sendToAll(gameStatus);
      System.out.println(gameStatus);

      if (gameStatus == "Ready for accepting turn!") {
        // Step-3:
        receiveTurnListFromAllPlayers();

        // Step-4:

        ArrayList<String> turnResult = handleOrders();
        sendTurnStatusToAll(turnResult);
        displayTurnStatus(turnResult);

        // Step-5:
        // check victory and defeat
        // update gameStatus if needed

        String winning_color = this.theMasterServer.detectresult(theMap);
        if (winning_color != null) {
          gameStatus = winning_color + " player has won!";
        } else {
          // add one unit to each territory
          theMap.updateMapbyOneUnit();
          // updated the territories' produced resources to theTextPlayr
          updatePlayerResource();
        }

      } else {
        break;
      }
    }
  }

  /**
   * Method to create the version of the map with non-adjacent enemy territory
   * details and enemy's spy map hidden and hidden enemy spies
   * 
   * @param String player color
   * @return Map
   */
  public V2Map<Character> getFogOfWarMap(String playerColor) {
    // first copy the current map
    V2Map<Character> fogOfWarMap = new V2Map<>(this.theMap);

    // Now get the territories grouped by the player
    HashMap<String, ArrayList<String>> terrGroupByOwner = fogOfWarMap.getOwnersTerritoryGroups();

    // Also need to remove the spy details about the enemy
    // so get the current spy map
    // HashMap<String, HashMap<String, Integer>> spyHashMap = fogOfWarMap.spy_map;

    for (String color : terrGroupByOwner.keySet()) {
      if (color != playerColor) { // if this is not the player
        // go through the list of territories and hide them in the map
        for (String terrName : terrGroupByOwner.get(color)) {
          Territory<Character> currTerritory = fogOfWarMap.myTerritories.get(terrName);

          // remove the territory (it includes all the details)
          fogOfWarMap.myTerritories.remove(terrName);

          // first check if the territory is adjacent to enemy territory
          // if it is adjacent, then no need to hide
          // if there is a spy, then no need to hide
          if (!theMap.isAdjacentToEnemy(terrName) && theMap.getSPY_map(playerColor, terrName) == 0) {

            // now hide the details
            currTerritory.hideDetails();
          } // end of - if not adjacent
            // if currTerritory is cloaked and there is no spy in currTerritory, hide
            // currTerritory
          else if (currTerritory.getCloakedorNot() && theMap.getSPY_map(playerColor, terrName) == 0) {
            currTerritory.hideDetails();
          } 
          else {
            // For all other territories, we just need to hide the spies
            currTerritory.hideSpies();
          }

          // add the updated territory with hidden info

          fogOfWarMap.myTerritories.put(terrName, currTerritory);

        } // end of - for each territory

        // now hide this enemy's spy info
        if (fogOfWarMap.spy_map.containsKey(color)) {
          fogOfWarMap.spy_map.remove(color);
        }

      } // end of - if not this player_color
    } // end of - for each color

    /*
     * // print fog of war spy map System.out.println("**********SPY MAP********");
     * for (String color : fogOfWarMap.spy_map.keySet()) {
     * System.out.println("Player: " + color); for (String terrString :
     * fogOfWarMap.spy_map.get(color).keySet()) { System.out.println("Spy in " +
     * terrString); } }
     */
    return fogOfWarMap;
  }

  /**
   * Method to hide enemies spy related info from turn status
   * 
   * @param player's color in String format, original full turn status
   * @return fog of war turn status
   */
  private ArrayList<String> getFogOfWarTurnStatus(String player_color, ArrayList<String> allTurnStatus) {
    ArrayList<String> fogOfWarTurnStatus = new ArrayList<String>();
    fogOfWarTurnStatus.addAll(allTurnStatus);
    
    ArrayList<String> toRemove = new ArrayList<String>();
    for (String turn : fogOfWarTurnStatus) {
      if (!turn.startsWith(player_color)) { // if this turn status for the enemy
        if (turn.indexOf("Move") != -1 || turn.indexOf("Upgrade") != -1) { // Move and Upgrade may have SPY related
                                                                           // info, so hide them from enemy
          toRemove.add(turn);
        }
      }
    }
    fogOfWarTurnStatus.removeAll(toRemove);
    return fogOfWarTurnStatus;
  }

}
