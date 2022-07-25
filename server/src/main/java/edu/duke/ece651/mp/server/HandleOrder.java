package edu.duke.ece651.mp.server;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Random;

import edu.duke.ece651.mp.common.*;

public class HandleOrder<T> {
  public ArrayList<TurnList> all_order_list;
  Map<T> theMap;
  private final MoveChecking<T> moveChecker;
  ArrayList<String> turnStatus;
  private FoodResourceList food_list;
  private TechResourceList tech_list;
  public ArrayList<String> players_identity;

  HandleOrder() {
    this.all_order_list = new ArrayList<TurnList>();
    this.theMap = new V2Map<>();
    this.moveChecker = null;
    this.turnStatus = new ArrayList<>();
    this.players_identity = new ArrayList<String>();
    this.food_list = new FoodResourceList(players_identity);
    this.tech_list = new TechResourceList(players_identity);
  }

  HandleOrder(ArrayList<TurnList> all_order_list, Map<T> theMap, MoveChecking<T> moveChecker,
      ArrayList<String> players_identity, FoodResourceList food_list, TechResourceList tech_list) {
    this.all_order_list = all_order_list;
    this.theMap = theMap;
    MapTextView test = new MapTextView((V2Map) theMap);
    test.displayMap();
    this.moveChecker = moveChecker;
    this.turnStatus = new ArrayList<>();
    this.players_identity = players_identity;
    this.food_list = food_list;
    this.tech_list = tech_list;
  }

  /**
   * Method to handle All Move Orders
   * 
   */
  public void handleAllMoveOrder() {
    for (int i = 0; i < all_order_list.size(); i++) {
      TurnList curr = all_order_list.get(i);
      System.out.println("The length of the TurnList is:" + curr.getListLength());
      for (int j = 0; j < curr.getListLength(); j++) {
        Turn curr_turn = curr.order_list.get(j);
        if (curr_turn.getTurnType().equals("Move")) {
          handleSingleMoveOrder((MoveTurn) curr_turn);
        }
      }
    }
  }

  /**
   * Method to handle All Attack Orders
   * 
   */

  public void handleAllAttackOrder() {
    // create a temporary map with correct attacker and defender unit number
    // (requirement 5d)

    Map<T> tempMap = theMap; // validationMap is used for validate AttackTurn

    ArrayList<TurnList> valid_attack_order_list = new ArrayList<TurnList>();

    AttackChecking<T> ruleChecker = new AttackChecking<>();

    // filter out valid attack orders from all player and updated the tempMap used
    // by HandleSingleAttackOrder
    for (int i = 0; i < all_order_list.size(); i++) {
      TurnList curr = all_order_list.get(i);
      TurnList curr_valid = new TurnList(curr.player_info);
      for (int j = 0; j < curr.getListLength(); j++) {
        Turn curr_turn = curr.order_list.get(j);
        if (curr_turn.getTurnType().equals("Attack")) {
          if (ruleChecker.checkMyRule(tempMap, (AttackTurn) curr_turn,food_list)) {
            curr_valid.order_list.add(curr_turn);
            // update the ValidationMap
            // int move_unit = curr_turn.getNumber(curr_turn.getTurnType());
            // int new_unit = tempMap.getAllTerritories().get(((AttackTurn)
            // curr_turn).getSource())
            // .getUnit(curr_turn.getTurnType())
            // - move_unit;
            tempMap.updateTempMap(((AttackTurn) curr_turn).getSource(), (AttackTurn) curr_turn);
          }
          turnStatus.add(ruleChecker.attackStatus);
        }
      }
      valid_attack_order_list.add(curr_valid);
    }

    ArrayList<HashMap<String, ArrayList<Turn>>> res = new ArrayList<HashMap<String, ArrayList<Turn>>>();
    // sort attack turns for the same player.
    for (int i = 0; i < valid_attack_order_list.size(); i++) {
      // System.out.println("sort attack orders: ");
      TurnList curr = valid_attack_order_list.get(i);
      HashMap<String, ArrayList<Turn>> temp = new HashMap<String, ArrayList<Turn>>();
      for (int j = 0; j < curr.getListLength(); j++) {
        AttackTurn curr_turn = (AttackTurn) curr.order_list.get(j);

        // Group the AttackTurns which attack the same Territory together
        String des = curr_turn.getDestination();
        if (temp.get(des) != null) {
          temp.get(des).add(curr_turn);
        } else {
          ArrayList<Turn> newTurnList = new ArrayList<Turn>();
          newTurnList.add(curr_turn);
          temp.put(des, newTurnList);
        }
      }
      res.add(temp);
    }

    for (HashMap<String, ArrayList<Turn>> hm : res) {
      // Same Player & Same Territory
      for (ArrayList<Turn> t : hm.values()) {
        handleSingleAttackOrder(t, tempMap);
      }
    }

  }

  /**
   * Method to handle Move Order
   * 
   */
  public void handleSingleMoveOrder(MoveTurn moveOrder) {
    String dep = moveOrder.getSource();
    Territory<T> depT=theMap.getAllTerritories().get(dep);
    String des = moveOrder.getDestination();
    Territory<T> desT=theMap.getAllTerritories().get(des);
    String player_color = moveOrder.getPlayerColor();
    HashMap<String, Integer> moveUnits = moveOrder.getUnitList();

    // Check Rules - Owner and Path
    String moveProblem = moveChecker.checkMoving(theMap, dep, des, moveUnits, player_color);
    int spy_unit=0;
    HashMap<String,Integer> spy_level_unit=new HashMap<>();
    //remove the spy unit from the unit_list
    if(moveUnits.containsKey("SPY")){
      spy_unit=moveUnits.get("SPY");
      moveUnits.remove("SPY");
      spy_level_unit.put("SPY",spy_unit);
    }
    // Check Rule - Food Resource
    // Find the minimum cost path from source to destination
    // the cost of each move is (total size of territories moved through) * (number
    // of units moved).
    int minimumCost = calculateMinimumCostToMove(dep, des, moveUnits)+calculateMinimumCostToMove(dep,des,spy_level_unit);

    // Check if the player has enough food resources to make the move
    int playerFoodResource = food_list.resource_list.get(player_color).getResourceAmount();
    if (minimumCost > playerFoodResource) {
      moveProblem = "Not enough food resource - at least " + minimumCost + " required";
    } else if(moveProblem == null){ // deduct the player's resource
      food_list.addResource(player_color, -minimumCost);
    }
    //reput the spy level and its unit in the hashmap allunits
    moveUnits.put("SPY",spy_unit);
    //create the secret map that can only be seen by the player.
    String moveResult;

    if (moveProblem == null) {
      // update Territory & Map

      for (String unit_type : moveUnits.keySet()) {

        if(unit_type.equals("SPY")){
          //edit the spy_unit in the source
          if(depT.getColor().equals(player_color)){
            theMap.updateTerritoryInMap(dep, unit_type, spy_unit * (-1));
          }else {
            theMap.editSPY_map(player_color,dep,spy_unit * (-1));
          }

          //edit the spy_unit in the destination
          if(desT.getColor().equals(player_color)){
            theMap.updateTerritoryInMap(des, unit_type, spy_unit * (1));
          }else {
            theMap.editSPY_map(player_color,des,spy_unit * (1));
          }
          continue;
        }
        int unitsToMove = moveUnits.get(unit_type);
        // update source territory
        theMap.updateTerritoryInMap(dep, unit_type, unitsToMove * (-1)); // -1 for taking units out
        // update destination territory
        theMap.updateTerritoryInMap(des, unit_type, unitsToMove); // for adding

      }
      moveResult = "successful";
    } else {
      moveResult = "invalid (Reason: " + moveProblem + ")";
    }
    turnStatus.add(moveChecker.moveStatus + moveResult);
  }

  public void handleSingleAttackOrder(ArrayList<Turn> attackOrder, Map<T> tempMap) {
    String attackResult = "";
    ArrayList<Turn> validturn = new ArrayList<>();
    for (Turn temp : attackOrder) {
      AttackTurn t = (AttackTurn) temp;
      int total_attackingunits = 0;
      for (String s : t.getUnitList().keySet()) {
        total_attackingunits += t.getUnitList().get(s).intValue();
      }
      String attackerTerritory = t.getSource();
      Territory<T> attacker = theMap.getAllTerritories().get(attackerTerritory);

      // player color
      String color = attacker.getColor();
      if (total_attackingunits > food_list.resource_list.get(color).getResourceAmount()) {
        attackResult += attackerTerritory + " failed because of no enough food resource from " + attackerTerritory
            + "\n";
        // restore the units in the attacker
        for (String s : t.getUnitList().keySet()) {
          attacker.updateUnit(s, t.getUnitList().get(s));
        }
      } else {
        validturn.add(temp);
      }
    }
    if (validturn.size() != 0 || !validturn.isEmpty()) {
      attackResult += resolveCombat(validturn, tempMap);
    }
    turnStatus.add(attackResult);
  }

  /**
   *
   * @param attackOrder
   * @param tempMap
   * @return
   */
  private String resolveCombat(ArrayList<Turn> attackOrder, Map<T> tempMap) {

    String combatResult;
    int attacking_units = 0;
    String attackerTerritory = "";
    String defenderTerritory = "";
    String player_color = "";
    HashMap<String, Integer> attacking_map = new HashMap<>();// key:type value: unit number

    // loop through all the attackorder in the arraylist
    for (Turn temp : attackOrder) {
      AttackTurn t = (AttackTurn) temp;
      // attacking_units += t.getNumber(); // add up units
      int total_attackingunits = 0;
      for (String s : t.getUnitList().keySet()) {
        if (!attacking_map.containsKey(s)) {
          attacking_map.put(s, t.getUnitList().get(s));
        } else {
          attacking_map.replace(s, attacking_map.get(s), attacking_map.get(s) + t.getUnitList().get(s));
        }
        total_attackingunits += t.getUnitList().get(s).intValue();
      }
      attackerTerritory = t.getSource(); // any Source
      defenderTerritory = t.getDestination(); // same Destination
      // player_color = t.getPlayerColor();
      // reduce food resources in all attackerTerritory
      Territory<T> attacker = theMap.getAllTerritories().get(attackerTerritory);
      String color = attacker.getColor();
      food_list.addResource(color, new FoodResource(-total_attackingunits));
      // attacker.updateResource(new FoodResource(-total_attackingunits));

    }

    // update the attacking_list for attacking from the attacking map
    ArrayList<Unit> attacking_list = new ArrayList<>();
    for (String s : attacking_map.keySet()) {
      if (attacking_map.get(s).intValue() != 0) {
        attacking_list.add(new Unit(s, attacking_map.get(s).intValue()));
      }
    }
    Collections.sort(attacking_list, (o1, o2) -> o1.getBonus() - o2.getBonus());

    Territory<T> attacker = tempMap.getAllTerritories().get(attackerTerritory);
    Territory<T> defender = tempMap.getAllTerritories().get(defenderTerritory);
    // int defending_units = defender.getUnit("ALEVEL");
    // list of different type of unit for the defender
    ArrayList<Unit> defending_copy_list = defender.getUnitList();
    ArrayList<Unit> defending_list = new ArrayList<>();
    for (Unit u : defending_copy_list) {
      //remove the SPY level from the defending_list
      if(u.getUnitType().equals("SPY")){
        continue;
      }
      if (u.getUnitNum() != 0) {
        defending_list.add(new Unit(u.getUnitType(), u.getUnitNum()));
      }
    }
    Collections.sort(defending_list, (o1, o2) -> o1.getBonus() - o2.getBonus());
    Random attackerDice = new Random();
    Random defenderDice = new Random();
    int diceSides = 20;
    System.out.println("Starting combat...");
    // start combat
    // define a count the total loop times
    int count = 0;
    while (true) {

      // step-1: role a 20 sided twice for both players
      int attackerDiceVal = attackerDice.nextInt(diceSides);
      int defenderDiceVal = defenderDice.nextInt(diceSides);

      // step-3: detect the result, if one side lose, update the map
      // if no side has lost, continue the loop
      String loserTerr;
      if (defender_result(defending_list)) {
        // clear all the units in temp map and add the attacker's attacking units take
        // hold
        ArrayList<String> origin_list = tempMap.getTerritoryUnitType(defenderTerritory);
        for (String s : origin_list) {
          tempMap.updateTerritoryInMap(defenderTerritory, s, -defender.getUnit(s), defender.getColor());
        }
        for (Unit u : attacking_list) {
          String unit_type = u.getUnitType();
          defender.updateUnit(unit_type, 0);
          tempMap.updateTerritoryInMap(defenderTerritory, u.getUnitType(), u.getUnitNum(), attacker.getColor());
        }
        loserTerr = defenderTerritory;
        combatResult = "Attacker won!\n";
        break;
      } else if (attacker_result(attacking_list)) {
        for (Unit u : defending_list) {
          String unit_type = u.getUnitType();
          tempMap.updateTerritoryInMap(defenderTerritory, u.getUnitType(), u.getUnitNum() - defender.getUnit(unit_type),
              defender.getColor());
        }
        loserTerr = attackerTerritory;
        combatResult = "Defender won!\n";
        break;
      }

      // step-2: compare dice values. Lower loses 1 unit
      // if: attacker wins
      // else: defender wins
      if (count % 2 == 0) {
        if (attackerDiceVal + attacking_list.get(attacking_list.size() - 1).getBonus() > defenderDiceVal
            + defending_list.get(0).getBonus()) {
          int origin_unit = defending_list.get(0).getUnitNum();
          int curr_unit = origin_unit - 1;
          defending_list.get(0).updateUnit(curr_unit);
          if (curr_unit == 0) {
            defending_list.remove(0);
          }
        } else {
          int origin_unit = attacking_list.get(attacking_list.size() - 1).getUnitNum();
          int curr_unit = origin_unit - 1;
          attacking_list.get(attacking_list.size() - 1).updateUnit(curr_unit);
          if (curr_unit == 0) {
            attacking_list.remove(attacking_list.size() - 1);
          }
        }
      } else {// attacker wins
        if (attackerDiceVal + attacking_list.get(0).getBonus() > defenderDiceVal
            + defending_list.get(defending_list.size() - 1).getBonus()) {
          int origin_unit = defending_list.get(defending_list.size() - 1).getUnitNum();
          int curr_unit = origin_unit - 1;
          defending_list.get(defending_list.size() - 1).updateUnit(curr_unit);
          if (curr_unit == 0) {
            defending_list.remove(defending_list.size() - 1);
          }
        } else {// defender wins
          int origin_unit = attacking_list.get(0).getUnitNum();
          int curr_unit = origin_unit - 1;
          attacking_list.get(0).updateUnit(curr_unit);
          if (curr_unit == 0) {
            attacking_list.remove(attacking_list.size() - 1);
          }
        }
      }
      count++;
    }
    // step-3: detect the result, if one side lose, update the map
    // if no side has lost, continue the loop
    // update true map
    this.theMap = tempMap;
    return combatResult;
  }

  /**
   * function to see the remain of the defender
   *
   *
   * @param defender_list
   * @return true when defender lose all its units
   */
  private boolean defender_result(ArrayList<Unit> defender_list) {
    for (Unit u : defender_list) {
      if (u.getUnitNum() != 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * function to see the remain of the attacker
   *
   *
   * @param attacking
   * @return true when attacker lose all its units
   */
  private boolean attacker_result(ArrayList<Unit> attacking) {
    for (Unit u : attacking) {
      if (u.getUnitNum() > 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Method to handle All Upgrade Orders
   * 
   */
  public void handleAllUpgradeOrder() {
    for (int i = 0; i < all_order_list.size(); i++) {
      TurnList curr = all_order_list.get(i);
      for (int j = 0; j < curr.getListLength(); j++) {
        Turn curr_turn = curr.order_list.get(j);
        if (curr_turn.getTurnType().equals("Upgrade")) {
          // System.out.println("Try to handle upgrade order");
          handleSingleUpgradeOrder((UpgradeTurn) curr_turn);
        }
      }

    }
  }

  public void handleSingleUpgradeOrder(UpgradeTurn upgradeTurn) {
    UpgradeChecking<T> upgradeChecker = new UpgradeChecking<>();
    if (upgradeChecker.checkMyRule(theMap, upgradeTurn, tech_list)) {
      // the Upgrade Order is valid.
      // update the tech resources of players
      int upgrade_cost = upgradeChecker.upgrade_cost;
      tech_list.addResource(upgradeTurn.getPlayerColor(), new TechResource(-upgrade_cost));
      String old_type = upgradeTurn.getOldUnitType();
      String new_type = upgradeTurn.getNewUnitType();
      int unitChange = upgradeTurn.getNumber();
      theMap.updateMapForUpgrade(upgradeTurn.getFromTerritory(), old_type, new_type, unitChange);
    }
    turnStatus.add(upgradeChecker.upgradeStatus);
  }

  /**
   * Method to decrease one unit from each territory Used after each turn
   * 
   */
  public void updateMapbyOneUnit() {
    theMap.updateMapbyOneUnit();
  }
  public void handleAllCloaking(){
    for (int i = 0; i < all_order_list.size(); i++) {
      TurnList curr = all_order_list.get(i);
       for (int j = 0; j < curr.getListLength(); j++) {
        Turn curr_turn = curr.order_list.get(j);
        if (curr_turn.getTurnType().equals("Cloak")) {
          handleOneCloaking((CloakingTurn) curr_turn);
        }
      }
    }
  }
  /**
   * function to deal with one cloaking order
   * @param cloakingTurn
   */
  public void handleOneCloaking(CloakingTurn cloakingTurn){
     CloakChecking<T> check=new CloakChecking<>();
     if(check.checkMyRule(theMap,cloakingTurn,tech_list)) {
       String territory = cloakingTurn.getFromTerritory();
       String player_color = cloakingTurn.getPlayerColor();
       Territory<T> terr = theMap.getAllTerritories().get(territory);
       //change the state of the territory to be cloaked，and set the remained times to be 3
       theMap.addCloakedTerritory(terr);
     }
     turnStatus.add(check.CloakingStatus);
  }
  /**
   * Method to handle All kinds of Orders
   * 
   */
  public Map<T> handleOrders(ArrayList<TurnList> all_order_list, Map<T> theMap) {
    this.all_order_list = all_order_list;
    this.theMap = theMap;
    handleAllMoveOrder();
    handleAllAttackOrder();
    handleAllUpgradeOrder();
    handleAllCloaking();
    //need to decrease the remained cloaking times by 1
    //if remained time is zero, remove it from the list
    theMap.editCloakedTerritory();

    // NEED TO RETURN UPDATED MAP
    return theMap;
  }

  public FoodResourceList getFoodList() {
    return this.food_list;
  }

  public TechResourceList getTechList() {
    return this.tech_list;
  }

  /**
   * Method to compute all possible paths from the source territory to destination
   * territory using recursion
   * 
   * @param source territory, destination territory, list of all paths (used in
   *               recursion) currPath that's being computed
   * @return ArrayList of all possible path where path is an ArrayList of
   *         territories
   *
   */
  private ArrayList<Deque<Territory<T>>> computeAllPossiblePaths(String source, String destination,
      HashMap<String, Integer> allunits) {
    ArrayList<Deque<Territory<T>>> allPaths = new ArrayList<>();
    // Algorithm used: Depth First Search
    Deque<Territory<T>> stack = new ArrayDeque<>();

    // Keep track of territories visited to avoid loop
    Deque<Territory<T>> visited = new ArrayDeque<>();

    // get the source territory
    Territory<T> start = theMap.getAllTerritories().get(source);
    stack.push(start);
    String colorID = start.getColor();

    // Deque<Territory<T>> res = new ArrayDeque<>();
    // while all the territories are not visited
    while (!stack.isEmpty()) {
      Territory<T> currTerritory = stack.pop();
      // res.add(currTerritory);
      String currname = currTerritory.getName();

      if (currname.equals(destination)) { // found a path
        // add the path to the list
        Deque<Territory<T>> res = new ArrayDeque<Territory<T>>(visited);
        allPaths.add(res);
        continue;
      }

      // if the territory is not visited already
      if (!visited.contains(currTerritory)) {
        visited.push(currTerritory);

        // for each neighbour of this territory
        for (String s : currTerritory.getAdjacency()) {
          Territory<T> thisTerritory = theMap.getAllTerritories().get(s);
          // if the territory is owned by the player
          if (thisTerritory.getColor().equals(colorID)) {
            stack.push(thisTerritory);
          }
        }
      }
    }

    return allPaths;
  }

  /**
   * function to compute all possible paths to the result
   * @param source
   * @param destination
   * @param allunits
   * @return
   */
  private ArrayList<Deque<Territory<T>>> computeAllPossiblePathsForSpy(String source, String destination,
                                                                 HashMap<String, Integer> allunits) {
    ArrayList<Deque<Territory<T>>> allPaths = new ArrayList<>();
    // Algorithm used: Depth First Search
    Deque<Territory<T>> stack = new ArrayDeque<>();

    // Keep track of territories visited to avoid loop
    Deque<Territory<T>> visited = new ArrayDeque<>();

    // get the source territory
    Territory<T> start = theMap.getAllTerritories().get(source);
    stack.push(start);

    // Deque<Territory<T>> res = new ArrayDeque<>();
    // while all the territories are not visited
    while (!stack.isEmpty()) {
      Territory<T> currTerritory = stack.pop();
      // res.add(currTerritory);
      String currname = currTerritory.getName();

      if (currname.equals(destination)) { // found a path
        // add the path to the list
        Deque<Territory<T>> res = new ArrayDeque<Territory<T>>(visited);
        allPaths.add(res);
        continue;
      }

      // if the territory is not visited already
      if (!visited.contains(currTerritory)) {
        visited.push(currTerritory);

        // for each neighbour of this territory
        for (String s : currTerritory.getAdjacency()) {
          Territory<T> thisTerritory = theMap.getAllTerritories().get(s);
          stack.push(thisTerritory);
        }
      }
    }

    return allPaths;
  }

  /**
   * Method to caculate the minimum cost to move from source to destination
   * 
   * @param source territory, destination territory, number of units to move
   * @return Integer cost
   */
  private int calculateMinimumCostToMove(String source, String destination, HashMap<String, Integer> moveUnits) {
    int totalUnitsToMove = 0;
    for (HashMap.Entry<String, Integer> unit : moveUnits.entrySet()) {
      totalUnitsToMove += unit.getValue();
    }
    System.out.println("units to move: " + totalUnitsToMove);

    // create an empty path of all pa
    ArrayList<Deque<Territory<T>>> allpaths=new ArrayList<>();
    if(!moveUnits.containsKey("SPY")) {//path for all type except unit
      allpaths = computeAllPossiblePaths(source, destination, moveUnits);
    }else{//path for spy unit
      allpaths =computeAllPossiblePathsForSpy(source,destination,moveUnits);
    }
    int number = Integer.MAX_VALUE;
    int total_unit = 0;
    // compute the minimal cost for the move
    for (Deque<Territory<T>> currPath : allpaths) {
      total_unit = 0;
      for (Territory<T> terr : currPath) {
        total_unit += terr.getSize();
      }
      number = Math.min(number, total_unit);
    }

    return totalUnitsToMove * number;
  }

  /**
   * Method to printout the path
   */
  private void printPath(Deque<Territory<T>> path) {
    String sep = "";
    for (Territory<T> territory : path) {
      System.out.print(sep + territory.getName());
      sep = "-->";
    }
    System.out.print("\n");
  }

}
