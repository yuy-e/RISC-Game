package edu.duke.ece651.mp.common;

import java.util.HashMap;

public class OwnerChecking<T> extends MoveChecking<T> {

  public OwnerChecking(MoveChecking<T> next) {
    super(next);
  };

  /**
   * Method to check rule for all unit types
   */
  public String checkMyRule(Map<T> map, String source, String destination, HashMap<String, Integer> allUnits,String player_color) {
    Territory<T> s = map.getAllTerritories().get(source);
    Territory<T> d = map.getAllTerritories().get(destination);
    //check if allunits contains nothing
    if(allUnits.isEmpty()){
      return null;
    }
    // first check if both units are owned by the player, except the spy unit
    if(!allUnits.containsKey("SPY")) {
      if (!s.getColor().equals(d.getColor())&&!s.getColor().equals(player_color)) {
        return "not same owner";
      } else if (!hasEnoughUnits(s, allUnits)) {
        return "Insuffcient units";
      } else {
        return null;
      }
    }else{
      //check the spy move
      //if the destination is enemy, can only move to adjacent territory
      if(!d.getColor().equals(player_color)&&!s.getAdjacency().contains(destination)){
        return destination + " is not adjacent to "+source;
        //if the source is player's territory, check for spy
      } else if (!hasEnoughUnits(s, allUnits)&&s.getColor().equals(player_color)){//write firs
        return " Insuffcient units";
        //if the source is player's enemy territory,check
      }else if(!hasEnoughSpyUnits(map,source,player_color,allUnits.get("SPY"))&&!s.getColor().equals(player_color)) {
        return " Insuffcient units";
      }else{
        return null;
      }
    }
  }

  /**
   * Method to check if a territpry has enough units for moving
   * @param Territory, HashMap of units
   */
  private boolean hasEnoughUnits(Territory<T> territory, HashMap<String, Integer> allunits) {
    // for each unit type, check if the territory has enough units
    for (String unitType : allunits.keySet()) {
      if (territory.getUnit(unitType) < allunits.get(unitType)) {
        return false;
      }
    }
    return true;
  }

  /**
   * function to check if the enemy territory has enough spy_unit to move
   * @param map
   * @param source
   * @param player_color
   * @param unit
   * @return
   */
  private boolean hasEnoughSpyUnits(Map<T> map,String source,String player_color,int unit){
    int existed_unit=map.getSPY_map(player_color,source);
    if(existed_unit>=unit){
      return true;
    }
    return false;
  }
}
