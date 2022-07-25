package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public abstract class Turn implements Serializable {
  String type;
  public String player_color;
  int num_unit; // Used only when there is one unit type (Eval-1)
  HashMap<String, Integer> num_units; // Eval-2 change for different unit types

  public Turn(String type, int num_unit, String player_color) {
    this.type = type;
    this.player_color = player_color;
    this.num_unit = num_unit;
    this.num_units = null;
  }

  public Turn(String type, HashMap<String, Integer> num_units, String player_color) {
    this.type = type;
    this.player_color = player_color;
    this.num_units = num_units;
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
  }

  public HashMap<String, Integer> getUnitList() {
    return num_units;
  }

  // used Only by Upgrade Turn
  public int getNumber() {
    return this.num_unit;
  }

  public int getNumber(String unitType) {
    return this.num_units.get(unitType);
  }

  public String getPlayerColor() {
    return this.player_color;
  }

  public String getTurnType() {
    return this.type;
  }

  public void printTurn() {
    System.out.println("Turn: ");
    System.out.println(this.type);
    System.out.println(this.player_color);

    printUnits();
  }

  public void printUnits() {
    if (num_units != null) {
      for (String unitType : num_units.keySet()) {
        System.out.println(unitType + "units: " + num_units.get(unitType));
      }
    } else {
      System.out.println(this.num_unit);
    }
  }

}
