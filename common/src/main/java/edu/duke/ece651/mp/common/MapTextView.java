package edu.duke.ece651.mp.common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles textual display of a Map (i.e. converting a Map class to a
 * string to show to the user).
 */

public class MapTextView {
  /**
   * The Map to display
   */
  private V2Map<Character> toDisplay;

  /**
   * Constructs a MapView, given the map it will display.
   * 
   * @param toMap is the Map to display
   */
  public MapTextView(V2Map<Character> toDisplay) {
    this.toDisplay = toDisplay;
  }

  public void updateTextView(V2Map<Character> newMap) {
    this.toDisplay = newMap;
  }

  /**
   * Display the map
   * 
   * Example format:
   * 
   * Green player: ------------ 10 units in Narnia (next to: Elantris, Midkemia)
   * 12 units in Midkemia (next to: Narnia, Elantris, Scadrial, Oz) 8 units in Oz
   * (next to: Midkemia, Scadrial, Mordor, Gondor)
   * 
   * Blue player: ----------- 6 units in Elantris (next to: Roshar, Scadrial,
   * Midkemia, Narnia) 3 units in Roshar (next to: Hogwarts, Scadrial, Elantris) 5
   * units in Scadrial (next to: Elantris, Roshar, Hogwats, Mordor Oz, Midkemia,
   * Elantris)
   */
  public String displayMap() {
    HashMap<String, ArrayList<String>> terrGroups = toDisplay.getOwnersTerritoryGroups();

    StringBuilder ans = new StringBuilder(""); // empty at first
    String separator = "";

    for (String player_color : toDisplay.getPlayerColors()) {
      ans.append(separator);
      ans.append(displayPlayerTerritories(player_color, terrGroups));
      separator = "\n"; // empty line between two players' info
    }
    return ans.toString();
  }

  /**
   * This method displays the map/territory info for each player
   * 
   * @param player's color, HashMap with player's color as key and list of
   *                 player's territories as value
   * @return the display info as string
   */
  private String displayPlayerTerritories(String playerColor, HashMap<String, ArrayList<String>> terrGroups) {
    ArrayList<String> terrList = terrGroups.get(playerColor);
    StringBuilder playerInfo = new StringBuilder("");
    playerInfo.append(makePlayerHeader(playerColor));
    for (String terrName : terrList) {
      playerInfo.append(toDisplay.getAllTerritories().get(terrName).getUnitList());
      playerInfo.append(" units in ");
      playerInfo.append(terrName);
      playerInfo.append(" (next to: ");
      // neighbours go here
      ArrayList<String> neighborList = toDisplay.getAllTerritories().get(terrName).getAdjacency();
      String sep = "";
      for (String neighborName : neighborList) {
        playerInfo.append(sep);
        playerInfo.append(neighborName);
        sep = ", ";
      }

      playerInfo.append(")");
      playerInfo.append("\n");
    }
    return playerInfo.toString();

  }

  /**
   * This method makes the header for showing each player's territory information
   */
  private String makePlayerHeader(String playerName) {
    StringBuilder header = new StringBuilder(""); // empty at first
    header.append(playerName);
    header.append(" player:");
    header.append("\n");
    header.append("-----------");
    header.append("\n");

    return header.toString();
  }

  // Receive a Map and display it.
  public String displayMap(Map<Character> map) {
    displayMap();
    return null;
  }

  /**
   * method to list player's own territory as a list
   */
  public String displayTerritoriesAsList(ArrayList<String> terrList) {
    StringBuilder options = new StringBuilder("");
    int i = 1;
    for (String terrName : terrList) {
      options.append("(" + i + ") ");
      options.append(terrName);
      options.append("\n");
      i++;
    }

    return options.toString();

  }

}
