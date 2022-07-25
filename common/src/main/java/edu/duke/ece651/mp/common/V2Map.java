package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class V2Map<T> implements edu.duke.ece651.mp.common.Map<T>, Serializable {
  public HashMap<String, Territory<T>> myTerritories; // key=name, value=object itself
  public ArrayList<String> players_colors;

  public ArrayList<String> AllUnitTypes;

  public HashMap<String, HashMap<String, Integer>> spy_map;// key:name value: HashMap<Key:Territory,value:spy_unit>

  public ArrayList<Territory<T>> cloakedTerritory ;
  /**
   * method to initialize all unit types and their bonus
   */
  private void initializeUnitTypes() {
    AllUnitTypes = new ArrayList<>();
    AllUnitTypes.add("Guards");
    AllUnitTypes.add("Infantry");
    AllUnitTypes.add("SPY");
    AllUnitTypes.add("Archer");
    AllUnitTypes.add("Cavalry");
    AllUnitTypes.add("Dwarves");
    AllUnitTypes.add("Orcs");
    AllUnitTypes.add("Elves");
  }

  /**
   * Constructor construct a V2Map specified with Territoris in it
   * 
   * @param players_colors myTerritories, the key is the territory itself and the
   *                       value is the list of adjancent territories
   */
  public V2Map(ArrayList<String> players_colors) {
    this.myTerritories = new HashMap<>();
    this.players_colors = players_colors;
    initializeUnitTypes();
    setMap();
    addAdjacency();
    addResources();
    addUnits();
    setSpy_map();
    this.cloakedTerritory=new ArrayList<>();
  }

  public V2Map() {
    this.myTerritories = new HashMap<>();
    ArrayList<String> players_colors = new ArrayList<String>();
    initializeUnitTypes();
    players_colors.add("Green");
    players_colors.add("Blue");
    this.players_colors = players_colors;
    setMap();
    addAdjacency();
    addResources();
    addUnits();
    setSpy_map();
    this.cloakedTerritory=new ArrayList<>();
  }

  /**
   * Copy constructor for Deep Copy
   */
  public V2Map(V2Map<T> rhsMap) {
    this.myTerritories = new HashMap<>();

    for (String terrName : rhsMap.myTerritories.keySet()) {
      Territory<T> copiedTerr = new Territory<>(rhsMap.myTerritories.get(terrName));
      myTerritories.put(terrName, copiedTerr);
    }

    this.players_colors = new ArrayList<>();
    this.players_colors.addAll(rhsMap.players_colors);

    this.AllUnitTypes = new ArrayList<>();
    this.AllUnitTypes.addAll(rhsMap.AllUnitTypes);

    this.spy_map = new HashMap<>();
    this.spy_map.putAll(rhsMap.spy_map);

    this.cloakedTerritory=new ArrayList<>();
    for(Territory<T> terr: rhsMap.cloakedTerritory ) {
      Territory<T> copiedTerr=new Territory<>(terr);
      this.cloakedTerritory.add(copiedTerr);
    }
  }

  public HashMap<String, Territory<T>> getAllTerritories() {
    return myTerritories;
  }

  public ArrayList<String> getPlayerColors() {
    return players_colors;
  }

  /**
   * function used in constructor to initialize the hashmap
   * 
   **/
  protected void setMap() {
    // PLAYER 1
    String player_color = players_colors.get(0);
    ArrayList<Unit> unit_list = new ArrayList<Unit>();
    myTerritories.put("Narnia", new Territory<T>("Narnia", player_color, new ArrayList<String>(), 8,  3));
    myTerritories.put("Midemio", new Territory<T>("Midemio", player_color, new ArrayList<String>(), 3, 3));
    myTerritories.put("Oz", new Territory<T>("Oz", player_color, new ArrayList<String>(), 12,3));

    // PLAYER 2
    player_color = players_colors.get(1);
    myTerritories.put("Elantris", new Territory<T>("Elantris", player_color, new ArrayList<String>(), 7,3));
    myTerritories.put("Scadnal", new Territory<T>("Scadnal", player_color, new ArrayList<String>(), 10,3));
    myTerritories.put("Roshar", new Territory<T>("Roshar", player_color, new ArrayList<String>(), 6,3));
  }

  /**
   * function used to initialize the adjacency information in the map
   **/
  protected void addAdjacency() {
    myTerritories.get("Narnia").addAdjacency("Midemio");
    myTerritories.get("Narnia").addAdjacency("Oz");
    myTerritories.get("Midemio").addAdjacency("Narnia");
    myTerritories.get("Midemio").addAdjacency("Oz");
    myTerritories.get("Midemio").addAdjacency("Elantris");
    myTerritories.get("Midemio").addAdjacency("Scadnal");
    myTerritories.get("Oz").addAdjacency("Midemio");
    myTerritories.get("Oz").addAdjacency("Narnia");
    myTerritories.get("Oz").addAdjacency("Scadnal");
    myTerritories.get("Elantris").addAdjacency("Scadnal");
    myTerritories.get("Elantris").addAdjacency("Roshar");
    myTerritories.get("Elantris").addAdjacency("Midemio");
    myTerritories.get("Scadnal").addAdjacency("Roshar");
    myTerritories.get("Scadnal").addAdjacency("Elantris");
    myTerritories.get("Scadnal").addAdjacency("Oz");
    myTerritories.get("Scadnal").addAdjacency("Midemio");
    myTerritories.get("Roshar").addAdjacency("Elantris");
    myTerritories.get("Roshar").addAdjacency("Scadnal");
  }

  /**
   * function used to initialize the Resource information in the map
   **/
  protected void addResources() {
    myTerritories.get("Narnia").updateResource(new FoodResource(10));
    myTerritories.get("Narnia").updateResource(new TechResource(20));
    myTerritories.get("Midemio").updateResource(new FoodResource(20));
    myTerritories.get("Midemio").updateResource(new TechResource(10));
    myTerritories.get("Oz").updateResource(new FoodResource(20));
    myTerritories.get("Oz").updateResource(new TechResource(20));

    myTerritories.get("Elantris").updateResource(new FoodResource(20));
    myTerritories.get("Elantris").updateResource(new TechResource(15));
    myTerritories.get("Scadnal").updateResource(new FoodResource(15));
    myTerritories.get("Scadnal").updateResource(new TechResource(15));
    myTerritories.get("Roshar").updateResource(new FoodResource(15));
    myTerritories.get("Roshar").updateResource(new TechResource(20));
  }

  /**
   * function used to initialize the unit information in the map
   **/
  protected void addUnits() {
    String basicUnitType = AllUnitTypes.get(0);

    for (String terrName : myTerritories.keySet()) {
      myTerritories.get(terrName).addUnit(new Unit(basicUnitType, 20));
    }
  }

  /**
   * function to initialize the spy map in the map
   */
  public void setSpy_map() {
    spy_map = new HashMap<>();
    String player_color1 = players_colors.get(0);
    spy_map.put(player_color1, new HashMap<>());
    String player_color2 = players_colors.get(1);
    spy_map.put(player_color2, new HashMap<>());
  }

  /**
   * function to get a player's spy_unit in the enemy territory
   * 
   * @param player_color
   * @param territory_name return 0 if there is no spy in this territory
   */
  public int getSPY_map(String player_color, String territory_name) {
    HashMap<String, Integer> this_spy_map = spy_map.get(player_color);
    if (this_spy_map.containsKey(territory_name)) {
      return this_spy_map.get(territory_name);
    }
    return 0;
  }

  /**
   * funtion to edit the spy_unit int the spy_map, delete or add
   * 
   * @param player_color
   * @param territory_name
   * @param spy_unit
   */
  public void editSPY_map(String player_color, String territory_name, int spy_unit) {
    HashMap<String, Integer> this_spy_map = spy_map.get(player_color);
    if (this_spy_map.containsKey(territory_name)) {
      int newunit = this_spy_map.get(territory_name) + spy_unit;
      this_spy_map.put(territory_name, newunit);
    } else {
      this_spy_map.put(territory_name, spy_unit);
    }
  }

  /**
   * Write out the V1Map object for serialization to the ObjectOutputStream s.
   * readObject depends on this data format.
   */
  private void writeObject(ObjectOutputStream s) throws IOException {
    // Call even if there is no default serializable fields.
    s.defaultWriteObject();

    // Hashmap is serializable
    s.writeObject(myTerritories);

    // otehr fields go here
    s.writeObject(players_colors);
  }

  /**
   * Read in the V1Map object from the ObjectInputStream s. Was written to by
   * writeObject.
   * 
   * @serialData Read serializable fields, if any exist.
   */
  @SuppressWarnings({ "unchecked" })
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    /*
     * Call even if there is no default serializable fields. Enables default
     * serializable fields to be added in future versions and skipped by this
     * version which has no default serializable fields.
     */
    s.defaultReadObject();

    // restore the HashMap
    myTerritories = (HashMap<String, Territory<T>>) s.readObject();
    // other fields go here
  }

  /**
   * method to get territories grouped by owner
   * 
   * @return hashmap with key as the owner/player color and value as an ArrayList
   *         of territories owned by the player
   */
  public HashMap<String, ArrayList<String>> getOwnersTerritoryGroups() {
    HashMap<String, ArrayList<String>> territoryGroups = new HashMap<>();
    territoryGroups.put("Green", new ArrayList<>());
    territoryGroups.put("Blue", new ArrayList<>());
    territoryGroups.put("Hidden", new ArrayList<>());

    for (Map.Entry<String, Territory<T>> entry : myTerritories.entrySet()) {
      Territory<T> terr = entry.getValue();
      String terrOwner = terr.getColor();
      territoryGroups.get(terrOwner).add(terr.getName());
    }
    return territoryGroups;
  }

  /**
   * method to get produced food resources grouped by owner
   *
   * @return hashmap with key as the owner/player color and value as
   *         FoodResources' amount of player-owned territories
   */
  public HashMap<String, Integer> getOwnersTerritoryFoodGroups() {
    HashMap<String, Integer> territoryFoodGroups = new HashMap<>();
    territoryFoodGroups.put("Green", 0);
    territoryFoodGroups.put("Blue", 0);

    for (Map.Entry<String, Territory<T>> entry : myTerritories.entrySet()) {
      Territory<T> terr = entry.getValue();
      int terr_food = terr.getFoodNum();
      String terrOwner = terr.getColor();
      int old_food = territoryFoodGroups.get(terrOwner);
      territoryFoodGroups.put(terrOwner, (old_food + terr_food));
    }
    return territoryFoodGroups;
  }

  /**
   * method to get produced tech resources grouped by owner
   *
   * @return hashmap with key as the owner/player color and value as
   *         TechResources' amount of player-owned territories
   */
  public HashMap<String, Integer> getOwnersTerritoryTechGroups() {
    HashMap<String, Integer> territoryTechGroups = new HashMap<>();
    territoryTechGroups.put("Green", 0);
    territoryTechGroups.put("Blue", 0);

    for (Map.Entry<String, Territory<T>> entry : myTerritories.entrySet()) {
      Territory<T> terr = entry.getValue();
      int terr_tech = terr.getTechNum();
      String terrOwner = terr.getColor();
      int old_tech = territoryTechGroups.get(terrOwner);
      territoryTechGroups.put(terrOwner, (old_tech + terr_tech));
    }
    return territoryTechGroups;
  }

  /*
   * method to get units' types of specific territory
   * 
   * @input String: the name of territory
   * 
   * @return ArrayList<String> consist of units' types
   */
  public ArrayList<String> getTerritoryUnitType(String currTerritory) {
    ArrayList<String> unit_type_list = new ArrayList<String>();
    for (Map.Entry<String, Territory<T>> entry : myTerritories.entrySet()) {
      Territory<T> terr = entry.getValue();
      if (terr.getName().equals(currTerritory)) {
        ArrayList<Unit> unit_list = terr.getUnitList();
        for (Unit unit : unit_list) {
          if (unit.getUnitNum() > 0) {
            unit_type_list.add(unit.getUnitType());
            // System.out.println("The unit type is :" + unit.getUnitType());
          }
        }
      }
    }
    return unit_type_list;
  }

  /*
   * Update Map according to move order
   * 
   * @input dep: start of Moving Action des: destination of Moving Action
   * unit_type: moving units' type n1: #unit in dep n2: #unit in des
   */
  public void updateMap(String dep, String des, String unit_type, int n1, int n2) {
    Territory<T> t1 = myTerritories.get(dep);
    t1.updateUnit(unit_type, n1);
    myTerritories.put(dep, t1);

    Territory<T> t2 = myTerritories.get(des);
    t2.updateUnit(unit_type, n2);
    myTerritories.put(des, t2);
  }

  // Upgrade Map according to upgrade order
  public void updateMapForUpgrade(String dep, String old_type, String new_type, int unit_change) {
    // update the #unit of old type
    Territory<T> terr = myTerritories.get(dep);
    int o_currUnits = terr.getUnit(old_type);
    int o_newUnits = o_currUnits - unit_change;
    terr.updateUnit(old_type, o_newUnits);
    // update the #unit of the new type
    int n_currUnits = terr.getUnit(new_type);
    int n_newUnits = n_currUnits + unit_change;
    terr.updateUnit(new_type, n_newUnits);
  }

  /* Update TempMap for moving all units before attacking */
  // public void updateTempMap(String dep, String unit_type, int n) {
  // Territory<T> t = myTerritories.get(dep);
  // t.updateUnit(unit_type, n);
  // }

  /* Update TempMap for moving all units before attacking. */
  public void updateTempMap(String dep, AttackTurn attackTurn) {
    Territory<T> t = myTerritories.get(dep);
    t.updateUnit(attackTurn.getUnitList());
  }

  /* Update Territory in the Map, including changing the owner of the Territory */

  public void updateTerritoryInMap(String territoryName, String unitType, int unitChange, String newOwnerColor) {
    Territory<T> terr = myTerritories.get(territoryName);
    int currUnits = terr.getUnit(unitType);
    int newUnits = currUnits + unitChange;
    terr.updateUnit(unitType, newUnits);
    if (newOwnerColor != "Unchanged") {
      terr.updateColor(newOwnerColor);
    }
    myTerritories.put(territoryName, terr);
  }

  /* Update Territory in the Map, including changing the owner of the Territory */
  // For the Use of updating combate result.
  public void updateTerritoryInMap(String territoryName, HashMap<String, Integer> unit_change, String newOwnerColor) {
    Territory<T> terr = myTerritories.get(territoryName);
    for (Map.Entry<String, Integer> set : unit_change.entrySet()) {
      int unitChange = set.getValue();
      int currUnits = terr.getUnit(set.getKey());
      int newUnits = currUnits + unitChange;
      terr.updateUnit(set.getKey(), newUnits);
    }
    if (newOwnerColor != "Unchanged") {
      terr.updateColor(newOwnerColor);
    }
    myTerritories.put(territoryName, terr);
  }

  // update Territory in map for combining old and new map
  public void updateTerritoryInMap(String territoryName, Territory terr){
    myTerritories.put(territoryName, terr);
  }


  public void updateTerritoryInMap(String territoryName, String unitType, int unitChange) {
    updateTerritoryInMap(territoryName, unitType, unitChange, "Unchanged");
  }

  // Increase #Units after fighting - add one basic unit - Guards
  public void updateMapbyOneUnit() {
    HashMap<String, Territory<T>> myT = myTerritories;
    int basic_unit_num = 0;
    for (Map.Entry<String, Territory<T>> set : myT.entrySet()) {
      Territory<T> temp = set.getValue();
      // just add One basic Unit to Territory
      for (Unit unit : temp.getUnitList()) {
        if (unit.getUnitType().equals("Guards")) {
          basic_unit_num = unit.getUnitNum();
        }
      }
      temp.updateUnit("Guards", basic_unit_num + 1);
      myTerritories.put(temp.getName(), temp);
    }
    System.out.println("End of turn: Added one basic unit to each territory.");
  }

  /**
   * function to get a player's territory group
   * @param player_color
   * @return terrList
   */
  public ArrayList<String> getPlayerTerritories(String player_color) {
    HashMap<String, ArrayList<String>> terrGroups = getOwnersTerritoryGroups();
    ArrayList<String> terrList = terrGroups.get(player_color);
    return terrList;
  }

  public void printMap() {
    HashMap<String, ArrayList<String>> terrGroupByOwner = this.getOwnersTerritoryGroups();
    for (String color : terrGroupByOwner.keySet()) {
      System.out.println(color + ": " + terrGroupByOwner.get(color));
    }
  }

  /**
   * Method to check if a territory is adjacent to enemy territory
   * 
   * @param String territory name
   * @return True/False
   */
  public boolean isAdjacentToEnemy(String terrName) {
    Territory<T> terr = this.myTerritories.get(terrName);

    // go through the adjacent territories
    for (String adjTerr : terr.getAdjacency()) {
      Territory<T> adjTerritory = this.myTerritories.get(adjTerr);
      // check if the owners are different
      if (!adjTerritory.getColor().equals(terr.getColor())) {
        return true;
      }
    }
    return false;
  }

  /**
   * function to get all cloaked territories
   * @return
   */
  public ArrayList<Territory<T>> getCloakedTerritory(){
    return cloakedTerritory;
  }

  /**
   * function to edit the remained turns for cloaking
   */
  public void editCloakedTerritory(){
    ArrayList<Territory<T>> cloakedTerr=new ArrayList<>();
    cloakedTerr.addAll(cloakedTerritory);
    for(Territory<T> terr:cloakedTerr){
       terr.remainedCloakingTimes--;
       if(terr.remainedCloakingTimes<0){
         terr.setCloaked(false);
         cloakedTerritory.remove(terr);
       }
    }
  }

  /**
   * function to add the territory to the list: CloakedTerritory
   * @param terr
   */
  public void addCloakedTerritory(Territory<T> terr){
    if(!cloakedTerritory.contains(terr)){
      cloakedTerritory.add(terr);
    }
    terr.changeRemainedTimes(3);
    terr.setCloaked(true);
  }
}
