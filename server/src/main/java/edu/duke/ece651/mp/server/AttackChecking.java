package edu.duke.ece651.mp.server;

import java.util.ArrayList;

import edu.duke.ece651.mp.common.Map;
import edu.duke.ece651.mp.common.Territory;
import edu.duke.ece651.mp.common.Turn;
import edu.duke.ece651.mp.common.TurnList;

public class AttackChecking<T> {
  String attackStatus;


  public ArrayList<Turn> checkMyRule(Map<T> map, ArrayList<Turn> attackOrder) {
    ArrayList<Turn> ans = new ArrayList<Turn>();
    for (Turn t : attackOrder) {
      boolean b = checkMyRule(map, t);
      if (b) {
        ans.add(t);
      }
    }
    return ans;
  }

  public boolean checkMyRule(Map<T> map, Turn attackOrder) {
    int attackingunits = attackOrder.getNumber();
    String source = attackOrder.getSource();
    String destination = attackOrder.getDestination();
    String player_color = attackOrder.getPlayerColor();

    Territory<T> attacker = map.getAllTerritories().get(source);
    Territory<T> defender = map.getAllTerritories().get(destination);

    attackStatus = player_color + ": Attack order from "
        + source + " into " + destination + " with "
        + attackingunits + " units was ";

    // check if the source belongs to the attacker
    if (!attacker.getColor().equals(player_color)) {
      attackStatus += "invalid as the source is not owned by the player";
      return false;
    }

    // check if the attacker and defender are adjacent
    if (!attacker.getAdjacency().contains(destination) || attacker.getColor().equals(defender.getColor())) {
      attackStatus += "invalid as there is no valid path";
      return false;
    }

    // check if the attacker and defender belong to different players
    if (!attacker.getAdjacency().contains(destination) || attacker.getColor().equals(defender.getColor())) {
      attackStatus += "invalid as both territories are owned by same player";
      return false;
    }

    // check if the attcker has enough units
    if (attacker.getUnit() < attackingunits) {
      attackStatus += "invalid as attacker doesn't have enough units";
      return false;
    }

    // passed all rules!
    attackStatus += "valid - ";
    return true;
  }
}
