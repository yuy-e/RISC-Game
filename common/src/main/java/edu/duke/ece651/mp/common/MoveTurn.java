package edu.duke.ece651.mp.common;

public class MoveTurn extends Turn {
  public MoveTurn(String fromTerritory, String toTerritory, int num_unit, String player_color) {
    super("Move", fromTerritory, toTerritory, num_unit, player_color);
  }
}
