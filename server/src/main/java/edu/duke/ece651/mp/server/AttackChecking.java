package edu.duke.ece651.mp.server;

import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.mp.common.*;

public class AttackChecking<T> {
  String attackStatus;

  public ArrayList<AttackTurn> checkMyRule(Map<T> map, ArrayList<AttackTurn> attackOrder,FoodResourceList foodResourceList) {
    ArrayList<AttackTurn> ans = new ArrayList<AttackTurn>();
    for (AttackTurn t : attackOrder) {
      boolean b = checkMyRule(map, t,foodResourceList);
      if (b) {
        ans.add(t);
      }
    }
    return ans;
  }

  public boolean checkMyRule(Map<T> map, AttackTurn attackOrder, FoodResourceList foodResourceList) {
    String source = attackOrder.getSource();
    String destination = attackOrder.getDestination();
    String player_color = attackOrder.getPlayerColor();
    HashMap<String, Integer> num_units = attackOrder.getUnitList();


    Territory<T> attacker = map.getAllTerritories().get(source);
    int size= attacker.getSize();
    Territory<T> defender = map.getAllTerritories().get(destination);

    String attack_units_info = "";
    for (HashMap.Entry<String, Integer> set : num_units.entrySet()) {
      attack_units_info += set.getValue() + set.getKey() + " ";
    }

    attackStatus = player_color + ": Attack order from "
        + source + " into " + destination + " with ( " + attack_units_info + ") units was ";

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

    // check if the attacker has enough units
    for (HashMap.Entry<String, Integer> set : num_units.entrySet()) {
      if (attacker.getUnit(set.getKey()) < set.getValue()) {
        attackStatus += "invalid as attacker doesn't have enough " + set.getKey() + " units";
        return false;
      }
    }

    //  check if the attacker has enough food resource
    int unitSum=0;
    for(HashMap.Entry<String, Integer> set : num_units.entrySet()){
      unitSum+=set.getValue();
    }
    if(unitSum*size>foodResourceList.resource_list.get(player_color).getResourceAmount()){
      attackStatus += "invalid as available food resource is not enough";
      return false;
    }

    // passed all rules!
    attackStatus += "valid - ";
    return true;
  }
}
