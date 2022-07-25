package edu.duke.ece651.mp.common;

import java.util.HashMap;

public abstract class MoveChecking<T> {
  private final MoveChecking<T> next;
  public String moveStatus;

  public MoveChecking(MoveChecking<T> next) {
    this.next = next;
  }

  protected abstract String checkMyRule(Map<T> map, String source, String destination, HashMap<String, Integer> allunits);

  public String checkMoving(Map<T> map, String source, String destination, HashMap<String, Integer> allunits, String player_color) {
    // update moveStatus
    moveStatus = player_color + ": Move order from " + source + " into " + destination + " was ";

    // First rule checking: owner checking
    if (checkMyRule(map, source, destination, allunits) != null) {
      return checkMyRule(map, source, destination, allunits);
    }

    // Second rule checking: path checking
    if (next != null) {
      return next.checkMoving(map, source, destination, allunits, player_color);
    }
    return null;
  }
}
