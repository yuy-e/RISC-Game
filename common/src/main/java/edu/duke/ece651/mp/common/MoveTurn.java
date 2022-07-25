package edu.duke.ece651.mp.common;

import java.util.HashMap;

public class MoveTurn extends Turn {

  private String fromTerritory;
  private String toTerritory;

  public MoveTurn(String fromTerritory, String toTerritory, int num_unit, String player_color) {
    super("Move", num_unit, player_color);
    this.fromTerritory = fromTerritory;
    this.toTerritory = toTerritory;
  }

  public MoveTurn(String fromTerritory, String toTerritory, HashMap<String, Integer> units, String player_color) {
    super("Move", units, player_color);
    this.fromTerritory = fromTerritory;
    this.toTerritory = toTerritory;
  }

  public String getSource() {
    return this.fromTerritory;
  }

  public String getDestination() {
    return this.toTerritory;
  }

  @Override
  public void printTurn() {
    System.out.println("Turn: ");
    System.out.println(this.type);
    System.out.println(this.fromTerritory);
    System.out.println(this.toTerritory);
    System.out.println(this.player_color);
    printUnits();
  }
}
