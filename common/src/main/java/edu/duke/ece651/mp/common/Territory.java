package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * construct a Territory class
 * @param: name

 * @param: color: one color represents one player
 * @param: adjacentTerritories
 * @param: unit_list: different type of unit
 * @param: food
 * @param: tech
 * @param: size
 * @param: isCloaked
 */
public class Territory<T> implements IITerritory<T>, Serializable {
  private String name;
  private String color;
  private ArrayList<String> adjacentTerritories;
  private ArrayList<Unit> unit_list;
  private FoodResource food;
  private TechResource tech;
  private int size;
  private boolean isCloaked;
  public int remainedCloakingTimes;
  public Territory(String name, String color, ArrayList<String> adjacentTerritories, int size,int times) {
    this.name = name;
    this.color = color;
    this.adjacentTerritories = new ArrayList<>();
    this.unit_list = new ArrayList<Unit>();
    this.food = new FoodResource(0);
    this.tech = new TechResource(0);
    this.size = size;
    this.isCloaked=false;
    this.remainedCloakingTimes=times;
  }

  /**
   * Copy constructor for deep copy
   */

  public Territory(Territory<T> rhsTerritory) {
    this.name = rhsTerritory.name;
    this.color = rhsTerritory.color;
    this.adjacentTerritories = new ArrayList<>();
    this.adjacentTerritories.addAll(rhsTerritory.adjacentTerritories);
    this.unit_list = new ArrayList<Unit>();
    this.unit_list.addAll(rhsTerritory.unit_list);
    this.food = new FoodResource(rhsTerritory.getFoodNum());
    this.tech = new TechResource(rhsTerritory.getTechNum());
    this.size = rhsTerritory.size;
    this.isCloaked=rhsTerritory.isCloaked;
    this.remainedCloakingTimes= rhsTerritory.remainedCloakingTimes;
  }

  /**
   * Method to get details about the territory in string format
   * 
   * @return String - territory details
   */
  public String getTerritoryDetails() {
    StringBuilder terrDetails = new StringBuilder("");
    terrDetails.append("Territory: " + name + "\n");
    terrDetails.append("--------------------------------\n");

    if (size != 0) {
      terrDetails.append("Size: " + size + "\n");
    }

    if (food != null) {
      terrDetails.append("Food Resource: " + food.getResourceAmount() + "\n");
    }

    if (tech != null) {
      terrDetails.append("Tech Resource: " + tech.getResourceAmount() + "\n");
    }

    if (unit_list != null) {
      terrDetails.append("********Units********" + getUnitsList());
    }

    return terrDetails.toString();
  }

  /**
   *
   */
  private String getUnitsList() {
    StringBuilder unitsDetails = new StringBuilder("");
    for (Unit unit : unit_list) {
      unitsDetails.append("\n" + "Level: " + unit.getUnitType() + "\n");
      unitsDetails.append("Bonus: " + unit.getBonus() + "\n");
      unitsDetails.append("Number: " + unit.getUnitNum());
    }
    return unitsDetails.toString();
  }

  public ArrayList<Unit> getUnitList() {
    return unit_list;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public int getFoodNum() {
    return food.getResourceAmount();
  }

  public int getTechNum() {
    return tech.getResourceAmount();
  }

  /**
   * function to add adjacency to the territory
   */
  public void addAdjacency(String name) {
    adjacentTerritories.add(name);
  }

  /**
   * function to add resources to the territory
   */
  public void updateResource(Resource resource) {
    if (resource instanceof FoodResource) {
      int new_num = food.getResourceAmount() + resource.getResourceAmount();
      setFoodResource(new_num);
    } else if (resource instanceof TechResource) {
      int new_num = tech.getResourceAmount() + resource.getResourceAmount();
      setTechResource(new_num);
    }
  }

  public void setFoodResource(int num) {
    food.setResourceAmount(num);
  }

  public void setTechResource(int num) {
    tech.setResourceAmount(num);
  }

  /**
   * function to add Units to the territory
   */
  public void addUnit(Unit unit) {
    unit_list.add(unit);
  }

  /*
   * function to get the adjacency list
   */
  public ArrayList<String> getAdjacency() {
    return adjacentTerritories;
  }

  /**
   * Write out the Territory object for serialization to the ObjectOutputStream s.
   * readObject depends on this data format.
   * 
   * @serialData Write serializable fields, if any exist. Write each field of the
   *             Territory Class
   */
  private void writeObject(ObjectOutputStream s) throws IOException {
    // Call even if there is no default serializable fields.
    s.defaultWriteObject();

    // save the name
    s.writeObject(name);

    // New added fields go here
  }

  /**
   * Read in the territory object from the ObjectInputStream s. Was written to by
   * writeObject.
   * 
   * @serialData Read serializable fields, if any exist.
   */
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    /*
     * Call even if there is no default serializable fields. Enables default
     * serializable fields to be added in future versions and skipped by this
     * version which has no default serializable fields.
     */
    s.defaultReadObject();

    // restore the name
    name = (String) s.readObject();

    // other fields go here
  }

  /* Update Unit of Territory according to move order */
  /* Also used by upgrade order */
  public void updateUnit(String unit_type, int new_unit) {
    int index = hasUnitType(unit_type);
    if (index >= 0) {
      unit_list.set(index, new Unit(unit_type, new_unit));
    } else {
      unit_list.add(new Unit(unit_type, new_unit));
    }
  }

  // update #units of Territory, using by tempMap
  public void updateUnit(HashMap<String, Integer> unit_list) {
    for (HashMap.Entry<String, Integer> set : unit_list.entrySet()) {
      int old_num = getUnit(set.getKey());
      int new_num = old_num - set.getValue();
      updateUnit(set.getKey(), new_num);
    }
  }

  // change the owner of the territory
  public void updateColor(String new_color) {
    this.color = new_color;
  }

  // check if the UnitType exists in the Territory
  public int hasUnitType(String unit_type) {
    for (int index = 0; index < unit_list.size(); index++) {
      if (unit_list.get(index).getUnitType().equals(unit_type)) {
        return index;
      }
    }
    return -1;
  }

  /*
   * @return the unit_num of specific UnitType
   */
  public int getUnit(String unit_type) {
    for (Unit unit : unit_list) {
      if (unit.getUnitType().equals(unit_type)) {
        return unit.getUnitNum();
      }
    }
    return 0;
  }

  /* get the cose of passing the Territory per unit. */
  public int getSize() {
    return this.size;
  }

  /**
   * Hide the territory owner, resource, size and unit details So it should only
   * have the adjacent territory info
   */
  public void hideDetails() {
    this.color = "Hidden";
    this.unit_list = new ArrayList<Unit>();
    this.food = new FoodResource(0);
    this.tech = new TechResource(0);
    this.size = 0;
  }

  /**
   * Method to hide Spies
   */
  public void hideSpies() {
    int index = 0;
    for (Unit unitType : unit_list) {
      if (unitType.getUnitType().equals("SPY")) {
        unit_list.remove(index);
        break; }
      index++;
    }

  }

  /**
   * function to change the visibility of the territory
   * @param cloaked
   */
  public void setCloaked(boolean cloaked){
    isCloaked=cloaked;
  }

  /**
   * function to get the visibility of the territory
   * @return
   */
  public boolean getCloakedorNot(){
    return isCloaked;
  }

  public void changeRemainedTimes(int times){
    this.remainedCloakingTimes=times;
  }

}
