package edu.duke.ece651.mp.server;

import java.util.ArrayList;

import edu.duke.ece651.mp.common.Map;
import edu.duke.ece651.mp.common.Territory;
import edu.duke.ece651.mp.common.UpgradeTurn;
import edu.duke.ece651.mp.common.TechResourceList;

public class UpgradeChecking<T> {
    String upgradeStatus;
    public int upgrade_cost;

    public boolean checkMyRule(Map<T> map, UpgradeTurn upgradeOrder, TechResourceList tech_list) {
        String source = upgradeOrder.getFromTerritory();
        String old_unit_type = upgradeOrder.getOldUnitType();
        String new_unit_type = upgradeOrder.getNewUnitType();
        int upgradeunits = upgradeOrder.getNumber();
        String player_color = upgradeOrder.getPlayerColor();
        int available_num = tech_list.resource_list.get(player_color).getResourceAmount();
        Territory<T> source_terr = map.getAllTerritories().get(source);

        upgradeStatus = player_color + ": Upgrade order in "
                + source + " from " + old_unit_type + " to " + new_unit_type + "(num:"
                + upgradeunits + ") units was ";

        // check if the source belongs to the attacker
        if (!source_terr.getColor().equals(player_color)) {
            upgradeStatus += "invalid as the source is not owned by the player";
            return false;
        }

        // check if the source has enough old unit type
        if (source_terr.getUnit(old_unit_type) < upgradeunits) {
            upgradeStatus += "invalid as the source don't have enough amount of " + old_unit_type;
            return false;
        }

        int base = 0;
        int top = 0;
        switch (old_unit_type) {
            case "ALEVEL":
                base = 0;
                break;
            case "BLEVEL":
                base = 3;
                break;
            case "CLEVEL":
                base = 11;
                break;
            case "DLEVEL":
                base = 30;
                break;
            case "ELEVEL":
                base = 55;
                break;
            case "FLEVEL":
                base = 90;
                break;
            case "GLEVEL":
                base = 140;
                break;
        }
        switch (new_unit_type) {
            case "ALEVEL":
                top = 0;
                break;
            case "BLEVEL":
                top = 3;
                break;
            case "CLEVEL":
                top = 11;
                break;
            case "DLEVEL":
                top = 30;
                break;
            case "ELEVEL":
                top = 55;
                break;
            case "FLEVEL":
                top = 90;
                break;
            case "GLEVEL":
                top = 140;
                break;
        }
        int upgrade_price = top - base;
        // check if the new unit level is higher than the old one
        if (upgrade_price <= 0) {
            upgradeStatus += "invalid as the " + new_unit_type + " is lower than " + old_unit_type;
            return false;
        }

        // check if the Player has enough tech resources to upgrade
        int need_amount = upgrade_price * upgradeunits;
        if (need_amount > available_num) {
            upgradeStatus += "invalid as the available tech resource's amount is not enough";
            return false;
        }

        // passed all rules!
        upgradeStatus += "valid ";
        this.upgrade_cost = need_amount;
        return true;
    }

}
