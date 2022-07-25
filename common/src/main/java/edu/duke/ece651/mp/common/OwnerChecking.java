package edu.duke.ece651.mp.common;

import java.util.HashMap;

public class OwnerChecking<T> extends MoveChecking<T> {

  public OwnerChecking(MoveChecking<T> next) {
    super(next);
  };

  /**
   * Method to check rule for all unit types
   */
  public String checkMyRule(Map<T> map, String source, String destination, HashMap<String, Integer> allUnits) {
    Territory<T> s = map.getAllTerritories().get(source);
    Territory<T> d = map.getAllTerritories().get(destination);

    // first check if both units are owned by the player
    if (!s.getColor().equals(d.getColor())) {
      return "not same owner";
    }

    // check if the territory has enough territory
    else if (!hasEnoughUnits(s, allUnits)) {
      return "Insuffcient units";
    } else {
      return null;
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
}
