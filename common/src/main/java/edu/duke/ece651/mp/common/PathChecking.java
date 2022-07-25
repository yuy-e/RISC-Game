package edu.duke.ece651.mp.common;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
public class PathChecking<T> extends MoveChecking<T> {

  public PathChecking(MoveChecking<T> next) {
    super(next);
  }

  ;

  /**
   * Method to check if there is a valid path from the source to destination (the path must be through the player's oen territories)
   */
  public String checkMyRule(Map<T> map, String source, String destination, HashMap<String, Integer> allunits, String player_color) {
    // Algorithm used: Depth First Search
    Deque<Territory<T>> stack = new ArrayDeque<>();

    HashSet<Territory<T>> visited = new HashSet<>();
    Territory<T> start = map.getAllTerritories().get(source);
    stack.push(start);
    String colorID = start.getColor();
    if (allunits.isEmpty()) {
      return null;
    }
    //for units that are not "SPY", it can only move in its own territory
    if (!allunits.containsKey("SPY")) {
      while (!stack.isEmpty()) {
        Territory<T> currTerritory = stack.pop();
        String currname = currTerritory.getName();
        if (currname.equals(destination)) {
          return null;
        }
        if (!visited.contains(currTerritory)) {
          visited.add(currTerritory);
          for (String s : currTerritory.getAdjacency()) {
            Territory<T> thisTerritory = map.getAllTerritories().get(s);
            if (thisTerritory.getColor().equals(colorID)) stack.push(thisTerritory);
          }
        }
      }
      return "no valid path exists";
    } else {
      //for units that are "SPY", it can move in its own and enemy territory as long as it's adjacent
      while (!stack.isEmpty()) {
        Territory<T> currTerritory = stack.pop();
        String currname = currTerritory.getName();
        if (currname.equals(destination)) {
          return null;
        }
        if (!visited.contains(currTerritory)) {
          visited.add(currTerritory);
          for (String s : currTerritory.getAdjacency()) {
            Territory<T> thisTerritory = map.getAllTerritories().get(s);
            stack.push(thisTerritory);
          }
        }
      }
      return "no valid path exist";
    }
  }
}
