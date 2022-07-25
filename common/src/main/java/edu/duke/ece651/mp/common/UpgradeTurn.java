package edu.duke.ece651.mp.common;

public class UpgradeTurn extends Turn {
    private String fromTerritory;
    private String old_unit_type;
    private String new_unit_type;

    public UpgradeTurn(String fromTerritory, String old_unit_type, String new_unit_type, int num_unit,
            String player_color) {
        super("Upgrade", num_unit, player_color);
        this.fromTerritory = fromTerritory;
        this.old_unit_type = old_unit_type;
        this.new_unit_type = new_unit_type;
    }

    public String getFromTerritory() {
        return this.fromTerritory;
    }

    public String getOldUnitType() {
        return this.old_unit_type;
    }

    public String getNewUnitType() {
        return this.new_unit_type;
    }

    @Override
    public void printTurn() {
        System.out.println("Turn: ");
        System.out.println(this.type);
        System.out.println(this.fromTerritory);
        System.out.println(this.old_unit_type);
        System.out.println(this.new_unit_type);
        System.out.println(this.num_unit);
        System.out.println(this.player_color);
    }

}
