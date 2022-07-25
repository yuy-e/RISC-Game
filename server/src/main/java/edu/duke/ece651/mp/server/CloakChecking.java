package edu.duke.ece651.mp.server;

import edu.duke.ece651.mp.common.CloakingTurn;
import edu.duke.ece651.mp.common.Map;
import edu.duke.ece651.mp.common.TechResourceList;
import edu.duke.ece651.mp.common.Territory;

import java.util.ArrayList;

public class CloakChecking<T> {
    String CloakingStatus="";
    public int cloaking_cost=20;
    public boolean checkMyRule(Map<T> map, CloakingTurn cloakingTurn, TechResourceList techResourceList){
        String player_color=cloakingTurn.getPlayerColor();
        String cloakedTerritory=cloakingTurn.getFromTerritory();
        Territory<T> cloakedTerr=map.getAllTerritories().get(cloakedTerritory);
        ArrayList<String> terrgroup=map.getPlayerTerritories(player_color);
        int available_num = techResourceList.resource_list.get(player_color).getResourceAmount();
        CloakingStatus+=player_color+": Cloak order in "+cloakedTerritory;
        if(!terrgroup.contains(cloakedTerritory)){
            CloakingStatus+=" is invalid as "+cloakedTerritory+
                    " is not owned by the player";
            return false;
        }
        if(cloaking_cost>available_num){
            CloakingStatus+=" invalid as the available tech resource's amount is not enough";
            return false;
        }
        CloakingStatus += " valid ";
        return true;
    }
}
