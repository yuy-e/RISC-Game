package edu.duke.ece651.mp.common;

public class AttackTurn extends Turn {
  public AttackTurn(String fromTerritory, String toTerritory, int num_unit, String player_color) {
    super("Attack", fromTerritory, toTerritory, num_unit, player_color);
  }

}
