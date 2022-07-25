package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/*
 * construct a Territory class
 * @param: name

 * @param: color: one color represents one player
 * @param: adjacentTerritories
 * @param: unit: number
 */
public class Territory<T> implements ITerritory<T>, Serializable {
  private String name;
  private String color;
  private ArrayList<String> adjacentTerritories;
  private int unit;
  // public Territory() {
  // this.name = null;
  // }

  public Territory(String name, String color, ArrayList<String> adjacentTerritories, int unit) {
    this.name = name;
    this.color = color;
    this.adjacentTerritories = new ArrayList<>();
    this.unit = unit;
  }

  public int getUnit() {
    return unit;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  /**
   * function to add adjacency to the territory
   */
  public void addAdjacency(String name) {
    adjacentTerritories.add(name);
  }

  /*
   * function to get the adjacency list
   */
  public ArrayList<String> getAdjacency() {
    return adjacentTerritories;
  }

  // public void addAdjacentTo

  /**
   * Write out the Territory object for serialization
   * to the ObjectOutputStream s.
   * readObject depends on this data format.
   * 
   * @serialData Write serializable fields, if any exist.
   *             Write each field of the Territory Class
   */
  private void writeObject(ObjectOutputStream s) throws IOException {
    // Call even if there is no default serializable fields.
    s.defaultWriteObject();

    // save the name
    s.writeObject(name);

    // New added fields go here
  }

  /**
   * Read in the territory object from the ObjectInputStream s.
   * Was written to by writeObject.
   * 
   * @serialData Read serializable fields, if any exist.
   */
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    /*
     * Call even if there is no default serializable fields.
     * Enables default serializable fields to be added in future versions
     * and skipped by this version which has no default serializable fields.
     */
    s.defaultReadObject();

    // restore the name
    name = (String) s.readObject();

    // other fields go here
  }

  public void updateUnit(int new_unit){
    this.unit = new_unit;
  }

  public void updateColor(String new_color){
    this.color = new_color;
  }

}
