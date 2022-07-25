package edu.duke.ece651.mp.common;

public class CloakingTurn extends Turn{
    String fromTerritory;
    public CloakingTurn(String fromTerritory, String player_color) {
        super("Cloak", player_color);
        this.fromTerritory=fromTerritory;
    }
    public String getFromTerritory(){
        return fromTerritory;
    }
}
