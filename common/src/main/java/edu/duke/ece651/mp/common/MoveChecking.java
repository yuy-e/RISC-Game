package edu.duke.ece651.mp.common;

import java.util.HashMap;

public abstract class MoveChecking<T> {
  private final MoveChecking<T> next;
  public String moveStatus;

  public MoveChecking(MoveChecking<T> next) {
    this.next = next;
  }

  protected abstract String checkMyRule(Map<T> map, String source, String destination, HashMap<String, Integer> allunits,String player_color);

  public String checkMoving(Map<T> map, String source, String destination, HashMap<String, Integer> allunits, String player_color) {
    // update moveStatus
    moveStatus = player_color + ": Move order from " + source + " into " + destination + " was ";
    //Hashmap spyunit is to store the unit number for the spy
    HashMap<String,Integer> spyunit=new HashMap<>();
    HashMap<String,Integer> newallunits=new HashMap<>();
    //deep copy the Hashmap:all units
    newallunits.putAll(allunits);
    //check if the moving contains spy level, if contains, remove it from the Hashmap
    if(allunits.containsKey("SPY")){
      int spy_unit=allunits.get("SPY");
      spyunit.put("SPY",spy_unit);
      newallunits.remove("SPY");
    }
    // First rule checking: owner checking
    if (checkMyRule(map, source, destination, newallunits,player_color) != null||checkMyRule(map, source, destination, spyunit,player_color) != null) {
      return checkMyRule(map, source, destination, newallunits,player_color)==null ? checkMyRule(map, source, destination, spyunit,player_color):checkMyRule(map, source, destination, newallunits,player_color);
    }

    // Second rule checking: path checking
    if (next != null) {
      return next.checkMoving(map, source, destination, allunits, player_color);
    }
    return null;
  }
}
