package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Turn implements Serializable {
  public String type;
  public String fromTerritory;
  public String toTerritory;
  public int num_unit;
  // public TextPlayer player;
  public String player_color;

  public Turn(String type, String fromTerritory, String toTerritory, int num_unit, String player_color) {
    this.type = type;
    this.fromTerritory = fromTerritory;
    this.toTerritory = toTerritory;
    this.num_unit = num_unit;
    this.player_color = player_color;
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
  }

  public String getSource() {
    return this.fromTerritory;
  }

  public String getDestination() {
    return this.toTerritory;
  }

  public int getNumber() {
    return this.num_unit;
  }

  public String getPlayerColor() {
    return this.player_color;
  }

  public String getTurnType(){
    return this.type;
  }

  public void printTurn(){
    System.out.println("Turn: ");
    System.out.println(this.type);
    System.out.println(this.fromTerritory);
    System.out.println(this.toTerritory);
    System.out.println(this.num_unit);
    System.out.println(this.player_color);
  }
}
