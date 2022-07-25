package edu.duke.ece651.mp.client;

import edu.duke.ece651.mp.common.*;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class GameController {

  // Text fields to show territory names in map
  @FXML
  private Button Terr1Button;
  @FXML
  private Button Terr2Button;
  @FXML
  private Button Terr3Button;
  @FXML
  private Button Terr4Button;
  @FXML
  private Button Terr5Button;
  @FXML
  private Button Terr6Button;

  private ArrayList<Button> terrButtons;

  private HashMap<String, Button> TerritoryButtons;
  private ArrayList<String> TerritoryNames;

  // Text field to show game status
  @FXML
  private TextField GameStatus;

  /**
   * Method to setup the map in the UI
   *
   */
  public void setUpMap() {
    V2Map<Character> initialMap = theTextPlayer.theMap;
    // setPlayerResourceView();
    setPlayerResourceDisplay();
    setUpTerritories(initialMap);
  }

  /**
   * Method to setup territories
   */
  private void setUpTerritories(V2Map<Character> initialMap) {
    // setup name, units and color of each territory first
    initTerritories(initialMap);
  }

  private void setTerritoryDetailsDisplay(String terrName) {
    Territory curTerr = theTextPlayer.theMap.getAllTerritories().get(terrName);
    int foodnum = curTerr.getFoodNum();
    int technum = curTerr.getTechNum();
    ArrayList<Unit> unit_list = curTerr.getUnitList();

    for (String unit : UnitNums.keySet()) {
      UnitNums.get(unit).setText(Integer.toString(0)); // default: 0
      for (Unit u : unit_list) {
        if (unit.equals(u.getUnitType())) {
          UnitNums.get(unit).setText(Integer.toString(u.getUnitNum()));
          break;
        }
      }
    }
    terrfood.setText(Integer.toString(foodnum));
    terrtech.setText(Integer.toString(technum));

    // show if the player has any spy in this enemy territory

    // print fog of war spy map
    System.out.println("**********SPY MAP********");
    for (String color : theTextPlayer.theMap.spy_map.keySet()) {
      System.out.println("Player: " + color);
      for (String terrString : theTextPlayer.theMap.spy_map.get(color).keySet()) {
        System.out.println("Spy in " + terrString);
      }
    }

    if (theTextPlayer.theMap.spy_map.get(theTextPlayer.identity).containsKey(terrName)) {
      // if this territory exists in the player's spy map
      int spyNum = theTextPlayer.theMap.spy_map.get(theTextPlayer.identity).get(terrName);
      // System.out.println(terrName + "has " + spyNum);
      UnitNums.get("MySpies").setText(Integer.toString(spyNum));
    }
  }

  ArrayList<StringProperty> terrUnitsList = new ArrayList<>();

  /**
   * Method to initialize the hashmaps
   */
  private void initTerritories(V2Map<Character> initialMap) {
    // init lists with Java FX components
    initLists();
    System.out.println("In initTerritories");
    HashMap<String, Territory<Character>> allTerritories = initialMap.getAllTerritories();

    // organize the territories according to player color
    HashMap<String, ArrayList<String>> terrGroups = initialMap.getOwnersTerritoryGroups();

    // TerritoryButtons = new HashMap<>();
    TerritoryNames = new ArrayList();
    int i = 0;
    // System.out.println(terrGroups.keySet());
    for (String player_color : terrGroups.keySet()) {
      Color terrColor = Color.WHITE; // default
      if (player_color.equals("Green")) {
        terrColor = Color.GREEN;
      } else if (player_color.equals("Blue")) {
        terrColor = Color.BLUE;
      }
      // get territories of this player color
      ArrayList<String> terrList = terrGroups.get(player_color);
      // System.out.println(player_color + ": " + terrList);
      for (String terrName : terrList) {
        String button_style = "-fx-background-color: rgba(240,240,240,.3)"; // default: white

        if (terrColor == Color.GREEN) {
          button_style = "-fx-background-color: rgba(60,179,113,.3)";
          System.out.println(terrName + ": green");
        } else if (terrColor == Color.BLUE) {
          button_style = "-fx-background-color: rgba(0,0,255,.3)";
          System.out.println(terrName + ": blue");
        }

        // Button curbutton = TerritoryButtons.get(terrName);
        TerritoryButtons.get(terrName).setStyle(button_style);
        // TerritoryButtons.put(terrName, curbutton);
        TerritoryNames.add(terrName);
        i++;
      }
    }
  }

  /**
   * Method to initialize the lists of Java FX components
   */
  private void initLists() {
    // Add rectangles

    terrButtons = new ArrayList<Button>();
    terrButtons.add(Terr1Button);
    terrButtons.add(Terr2Button);
    terrButtons.add(Terr3Button);
    terrButtons.add(Terr4Button);
    terrButtons.add(Terr5Button);
    terrButtons.add(Terr6Button);

    TerritoryButtons = new HashMap<String, Button>();
    TerritoryButtons.put("Narnia", terrButtons.get(0));
    TerritoryButtons.put("Midemio", terrButtons.get(1));
    TerritoryButtons.put("Oz", terrButtons.get(2));
    TerritoryButtons.put("Elantris", terrButtons.get(3));
    TerritoryButtons.put("Roshar", terrButtons.get(4));
    TerritoryButtons.put("Scadnal", terrButtons.get(5));

    // add UnitsTypes hashMap
    UnitNums = new HashMap<String, Label>();
    UnitNums.put("Guards", unit1num);
    UnitNums.put("Infantry", unit2num);
    UnitNums.put("SPY", unit8num);
    UnitNums.put("Archer", unit3num);
    UnitNums.put("Cavalry", unit4num);
    UnitNums.put("Dwarves", unit5num);
    UnitNums.put("Orcs", unit6num);
    UnitNums.put("Elves", unit7num);
    UnitNums.put("MySpies", mySpiesNum);
  }

  private TextPlayer theTextPlayer;
  ObservableList<String> playeraction_list = FXCollections.observableArrayList("Move", "Attack", "Upgrade", "Cloak");
  ObservableList<String> source_list = FXCollections.observableArrayList();
  ObservableList<String> destination_list = FXCollections.observableArrayList();
  ObservableList<String> unitType_list = FXCollections.observableArrayList();
  ObservableList<Integer> unitANum_list = FXCollections.observableArrayList();
  ObservableList<Integer> unitBNum_list = FXCollections.observableArrayList();
  ObservableList<Integer> unitCNum_list = FXCollections.observableArrayList();
  ObservableList<Integer> unitDNum_list = FXCollections.observableArrayList();
  ObservableList<Integer> unitENum_list = FXCollections.observableArrayList();
  ObservableList<Integer> unitFNum_list = FXCollections.observableArrayList();
  ObservableList<Integer> unitGNum_list = FXCollections.observableArrayList();
  ObservableList<Integer> spyNum_list = FXCollections.observableArrayList();;
  ObservableList<Integer> mySpyInEnemyNum_list = FXCollections.observableArrayList();

  ObservableList<Integer> upgradeNum_list = FXCollections.observableArrayList();

  TurnList myTurn;

  @FXML
  private Label player_info;
  @FXML
  private ComboBox<String> playeraction;
  @FXML
  private Button order;
  @FXML
  private Button commit;
  @FXML
  public TextArea turnstatus;
  @FXML
  public TextArea errormessage;

  // Move and Attack order inputs
  @FXML
  private Pane MoveAttackPane;
  @FXML
  private ComboBox<String> from;
  @FXML
  private ComboBox<String> to;
  @FXML
  private ComboBox<Integer> Units_A;
  @FXML
  private ComboBox<Integer> Units_B;
  @FXML
  private ComboBox<Integer> Units_C;
  @FXML
  private ComboBox<Integer> Units_D;
  @FXML
  private ComboBox<Integer> Units_E;
  @FXML
  private ComboBox<Integer> Units_F;
  @FXML
  private ComboBox<Integer> Units_G;
  @FXML
  private ComboBox<Integer> SPY;

  private HashMap<String, ComboBox<Integer>> UnitTypeEntries;

  // Upgrade Order inputs
  @FXML
  private Pane UpgradePane;
  @FXML
  private ComboBox<Integer> UpgradeUnits;
  @FXML
  private ComboBox<String> UpgradeTerritory;
  @FXML
  private ComboBox<String> UpgradeFrom;
  @FXML
  private ComboBox<String> UpgradeTo;
  // cloaking order inputs
  @FXML
  public Pane CloakingPane;
  @FXML
  public ComboBox<String> CloakingTerritory;

  @FXML
  private Label totalfood;
  @FXML
  private Label totaltech;

  @FXML
  private Label unit1num;
  @FXML
  private Label unit2num;
  @FXML
  private Label unit3num;
  @FXML
  private Label unit4num;
  @FXML
  private Label unit5num;
  @FXML
  private Label unit6num;
  @FXML
  private Label unit7num;
  @FXML
  private Label unit8num;
  @FXML
  private Label mySpiesNum;

  private HashMap<String, Label> UnitNums;

  @FXML
  private Label terrfood;
  @FXML
  private Label terrtech;

  public void setPlayer(TextPlayer player) {
    theTextPlayer = player;
  }

  public void initGame() {
    // Step-1 of playGame()
    theTextPlayer.receiveMap();
    theTextPlayer.receiveResource();
    setUpMap();

    // Step-2 of playGame()
    // Receive Game Status from server
    receiveAndUpdateGameStatus();

    setName();
    myTurn = new TurnList(theTextPlayer.identity);

    initiateUnitList();
    setOrderPane();
  }

  /**
   * Method to set up all the entry boxes for taking order
   */
  private void setOrderPane() {
    setActionBox();
    setTerritoryDropDowns();
    setUnitTypeBox();
  }

  /**
   * Method to set up drop downs for territories
   */
  private void setTerritoryDropDowns() {
    setSourceBox(from);
    setSourceBox(UpgradeTerritory);
    setSourceBox(CloakingTerritory);
    setDestinationBox();
    disableUnitsNumBox();
    // disable upgrade num box
    // UpgradeUnits.setDisable(true);
  }

  /**
   * Method to create a list pf all unit type textfield entries
   */
  private void initiateUnitList() {
    UnitTypeEntries = new HashMap<>();
    UnitTypeEntries.put("Guards", Units_A);
    UnitTypeEntries.put("Infantry", Units_B);
    UnitTypeEntries.put("SPY", SPY);
    UnitTypeEntries.put("Archer", Units_C);
    UnitTypeEntries.put("Cavalry", Units_D);
    UnitTypeEntries.put("Dwarves", Units_E);
    UnitTypeEntries.put("Orcs", Units_F);
    UnitTypeEntries.put("Elves", Units_G);
  }

  public void setName() {
    String name = theTextPlayer.identity;
    player_info.setText(name);
  }

  Tooltip playerResourceTooltip;

  /**
   * Method to display player's resources
   */
  private void setPlayerResourceDisplay() {
    int food = theTextPlayer.getTotalFoodResourceAmount();
    int tech = theTextPlayer.getTotalTechResourceAmount();
    totalfood.setText(Integer.toString(food));
    totaltech.setText(Integer.toString(tech));
  }

  /**
   * Method to update player resources display
   */
  private void updatePlayerResourceDisplay() {
    setPlayerResourceDisplay();
  }

  @FXML
  public void setActionBox() {
    playeraction.setItems(playeraction_list);
    playeraction.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setTerritoryDropDowns();
      }
    });
  }

  public String getAction() {
    return (String) playeraction.getValue();
  }

  @FXML
  public void setSourceBox(ComboBox<String> whichBox) {
    ArrayList<String> own_territory_list = theTextPlayer.getMyOwnTerritories();
    source_list.clear();

    source_list.addAll(own_territory_list);
    source_list.addAll(theTextPlayer.getOthersTerritories());

    whichBox.setItems(source_list);
    whichBox.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setUnitTypeBox();
        setUnitsNumBox();
      }
    });
  }

  public String getSource() {
    return (String) from.getValue();
  }

  public String getUpgradeSource() {
    return (String) UpgradeTerritory.getValue();
  }

  public String getCloakingTerritory() {
    return (String) CloakingTerritory.getValue();
  }

  @FXML
  public void setDestinationBox() {
    ArrayList<String> des_territory_list = new ArrayList<String>();
    if (getAction() != null) {
      if (getAction().equals("Move")) {
        des_territory_list = theTextPlayer.getMyOwnTerritories();
      } else if (getAction().equals("Attack")) {
        des_territory_list = theTextPlayer.getOthersTerritories();
      }
    }
    destination_list.clear();
    // destination_list.addAll(des_territory_list);
    destination_list.addAll(theTextPlayer.getMyOwnTerritories());
    destination_list.addAll(theTextPlayer.getOthersTerritories());
    to.setItems(destination_list);
  }

  public String getDestination() {
    return (String) to.getValue();
  }

  /**
   * Method to set the unit type boxes for Upgrade Turn
   */
  public void setUnitTypeBox() {
    ArrayList<String> own_unitType_list = theTextPlayer.theMap.getTerritoryUnitType(getUpgradeSource());
    unitType_list.clear();
    unitType_list.addAll(own_unitType_list);
    // upgrade from should be showing units owned by the territory
    UpgradeFrom.setItems(unitType_list);

    // upgrade to should be should be showing all the available levels
    unitType_list.clear();
    unitType_list.addAll(theTextPlayer.theMap.AllUnitTypes);
    UpgradeTo.setItems(unitType_list);

  }

  /*
   * public void setUpgradeUnitNumTypeBox(){ String currTerrName =
   * getUpgradeSource(); String currUnitType = getUpgradeFromUnitType();
   * ArrayList<Unit> unitList =
   * theTextPlayer.theMap.getAllTerritories().get(currTerrName).getUnitList();
   * for(Unit u: unitList){ if(u.getUnitType().equals(currUnitType)){ int
   * availableUpgradeNum = u.getUnitNum(); if(availableUpgradeNum != 0){
   * UpgradeUnits.setDisable(false); } for(int i=1; i<=availableUpgradeNum; i++){
   * upgradeNum_list.add(i); } UpgradeUnits.setItems(upgradeNum_list);
   * 
   * break; } } }
   */

  public void setUnitsNumBox() {
    unitANum_list.clear();
    unitBNum_list.clear();
    unitCNum_list.clear();
    unitDNum_list.clear();
    unitENum_list.clear();
    unitFNum_list.clear();
    unitGNum_list.clear();
    spyNum_list.clear();
    mySpyInEnemyNum_list.clear();
    upgradeNum_list.clear();

    String currTerrName = getSource();
    // System.out.println("Current source:" + currTerrName);

    boolean upgradeLayout = false;
    if (currTerrName == null) {
      currTerrName = getUpgradeSource();
      upgradeLayout = true;
    }
    // In a layout with source and destination

    ArrayList<Unit> unitList = theTextPlayer.theMap.getAllTerritories().get(currTerrName).getUnitList();
    Integer totalUnits = 0;
    for (Unit u : unitList) {
      if (!upgradeLayout) { // in a lau=yout with source and destination
        if (u.getUnitType().equals("Guards")) {
          setUnitNumBox(unitANum_list, Units_A, u.getUnitNum());
        } else if (u.getUnitType().equals("Infantry")) {
          setUnitNumBox(unitBNum_list, Units_B, u.getUnitNum());
        } else if (u.getUnitType().equals("Archer")) {
          setUnitNumBox(unitCNum_list, Units_C, u.getUnitNum());
        } else if (u.getUnitType().equals("Cavalry")) {
          setUnitNumBox(unitDNum_list, Units_D, u.getUnitNum());
        } else if (u.getUnitType().equals("Dwarves")) {
          setUnitNumBox(unitENum_list, Units_E, u.getUnitNum());
        } else if (u.getUnitType().equals("Orcs")) {
          setUnitNumBox(unitFNum_list, Units_F, u.getUnitNum());
        } else if (u.getUnitType().equals("Elves")) {
          setUnitNumBox(unitGNum_list, Units_G, u.getUnitNum());
        } else if (u.getUnitType().equals("SPY")) {
          setUnitNumBox(spyNum_list, SPY, u.getUnitNum());
        }
      }
      totalUnits += u.getUnitNum();
    }

    // since the player can move into an enemy territory, we need to check the spy
    // map
    // and add that in the SPY field
    if (theTextPlayer.theMap.spy_map.get(theTextPlayer.identity).containsKey(currTerrName)) {
      // if this territory exists in the player's spy map
      int spyNum = theTextPlayer.theMap.spy_map.get(theTextPlayer.identity).get(currTerrName);
      setUnitNumBox(mySpyInEnemyNum_list, SPY, spyNum);
    }

    // update the Upgrade units box
    System.out.println("Total units for upgrade:" + totalUnits);
    setUnitNumBox(upgradeNum_list, UpgradeUnits, totalUnits);
  }

  public void disableUnitsNumBox() {
    for (HashMap.Entry<String, ComboBox<Integer>> unit : UnitTypeEntries.entrySet()) {
      unit.getValue().setDisable(true);
    }
    UpgradeUnits.setDisable(true);
  }

  public void setUnitNumBox(ObservableList<Integer> numList, ComboBox<Integer> whichBox, Integer num) {
    if (num != 0) {
      whichBox.setDisable(false);
    }
    for (int i = 1; i <= num; i++) {
      numList.add(i);
    }
    whichBox.setItems(numList);
  }

  /**
   * Method to get the unit type selected
   */
  public String getUpgradeFromUnitType() {
    return UpgradeFrom.getValue();
  }

  /**
   * Method to get the unit type selected
   */
  public String getUpgradeToUnitType() {
    return UpgradeTo.getValue();
  }

  /**
   * Method to get user entered unit number
   *
   * @param which textfield
   */
  public int getUnitNum(ComboBox<Integer> UnitType) {
    int enteredVal;
    try {
      enteredVal = Integer.parseInt(String.valueOf(UnitType.getValue()));
    } catch (NumberFormatException e) {
      enteredVal = 0;
    }
    return enteredVal;
  }

  public int getUnitNum(TextField UnitType) {
    int enteredVal;
    try {
      enteredVal = Integer.parseInt(UnitType.getText());
    } catch (NumberFormatException e) {
      enteredVal = 0;
    }
    return enteredVal;
  }

  public String getPlayerColor() {
    return theTextPlayer.identity;
  }

  /*
   * Method to check if the user inputs for adding order is valid
   *
   * @returns true if valid inputs and false otherwise
   */
  @FXML
  boolean errorMessageShowing() {
    // Clear any previous error message
    errormessage.clear();

    boolean result = true;

    // For move/attack order, make sure the "from" and "to" are entered
    if ((getAction().equals("Move") || getAction().equals("Attack"))
        && (getSource() == null || getDestination() == null)) {
      errormessage.appendText("Both source and destination is needed.");
      result &= false;
    }

    // Now check if the unit number is positive and greater than zero
    boolean allZero = false;
    for (String unitType : UnitTypeEntries.keySet()) {
      int unitNum = getUnitNum(UnitTypeEntries.get(unitType));
      if (unitNum == 0) {
        allZero &= true;
      } else {
        allZero &= false;
        if (unitNum < 0) {
          errormessage.appendText(unitType + " Unit number must be positive & greater than zero.");
          result &= false;
        }
      }
    }
    if (allZero) {
      errormessage.appendText("Unit number must be provided for at least one unit type.");
      result &= false;
    }
    return result;
  }

  @FXML
  void onAddOrderButton(MouseEvent event) {
    boolean result = errorMessageShowing();

    if (result) { // if the inputs are valid
      // Check the type order
      if (getAction().equals("Move")) {
        HashMap<String, Integer> units = getUnitsEntry();
        Turn newOrder = new MoveTurn(getSource(), getDestination(), units, getPlayerColor());
        // newOrder.printTurn();
        myTurn.addTurn(newOrder);
        GameStatus.setText(getAction() + " order from " + getSource() + " to " + getDestination() + " added");
      } else if (getAction().equals("Attack")) {
        HashMap<String, Integer> units = getUnitsEntry();
        Turn newOrder = new AttackTurn(getSource(), getDestination(), units, getPlayerColor());
        // newOrder.printTurn();
        myTurn.addTurn(newOrder);
        GameStatus.setText(getAction() + " order from " + getSource() + " to " + getDestination() + " added");
      } else if (getAction().equals("Upgrade")) {
        // create upgrade
        Turn newOrder = new UpgradeTurn(getUpgradeSource(), getUpgradeFromUnitType(), getUpgradeToUnitType(),
            getUnitNum(UpgradeUnits), getPlayerColor());
        myTurn.addTurn(newOrder);
        GameStatus.setText(getAction() + " order in " + getUpgradeSource() + " from " + getUpgradeFromUnitType()
            + " to " + getUpgradeToUnitType() + " added");
      } else if (getAction().equals("Cloak")) {
        Turn newOrder = new CloakingTurn(getCloakingTerritory(), getPlayerColor());
        myTurn.addTurn(newOrder);
        GameStatus.setText(getAction() + " " + getCloakingTerritory() + " for 3 Turns");
      }
    }
    // theClient.theTextPlayer.takeAndSendTurn();
    System.out.println("Added a New Order");
    clearSelectedAction();

  }

  /**
   * Method to create a hashmap with keys as unit type and value as number of
   * units entered by the player Note the return hashmap includes the unit entry
   * which were valid i.e. non-zero and positive. The returned hashmap cannot be
   * empty as we check for all zero inputs in errorMessageShowing() method
   */
  private HashMap<String, Integer> getUnitsEntry() {
    HashMap<String, Integer> units = new HashMap<>();
    for (String unitType : UnitTypeEntries.keySet()) {
      int unitNum = getUnitNum(UnitTypeEntries.get(unitType));
      // Don't add if there are 0 units
      if (unitNum > 0) {
        units.put(unitType, unitNum);
      }
    }
    return units;
  }

  @FXML
  private void clearSelectedAction() {
    playeraction.setValue("Select an action");
  }

  @FXML
  void onCommitButton(MouseEvent event) {
    // Clear the turn status box before committing new turn
    turnstatus.deleteText(0, turnstatus.getLength());

    clearSelectedAction();

    // send TurnList to Server
    // Step-3 in Master playGame() in server
    // Similar to "takeAndSendTurn"
    theTextPlayer.connectionToMaster.sendToServer(myTurn);
    GameStatus.setText("Turn sent to server. Waiting for turn result...");
    System.out.println("Sent the TurnList");

    // Disable the button
    order.setDisable(true);
    commit.setDisable(true);

    // receive turn status
    // Step-4 in Master playGame() in server
    ArrayList<String> turnResult = theTextPlayer.receiveTurnStatus();

    // display the turn status in UI
    for (String s : turnResult) {
      turnstatus.appendText(s + "\n");
    }
    // remove the current turnlist
    myTurn.order_list.clear();

    // save old map for display 'hidden' territory's infomation
    V2Map oldMap = new V2Map(theTextPlayer.theMap);

    // receive updated map
    // Step-1 in Master playGame() in server
    theTextPlayer.receiveMap();
    theTextPlayer.receiveResource();

    // combine oldMap and updated Map to a new Map
    V2Map newMap = new V2Map<>(theTextPlayer.theMap);
    HashMap<String, Territory<Character>> allTerritories = theTextPlayer.theMap.getAllTerritories();
    for (String terrName : allTerritories.keySet()) {
      // Update color of territory
      String player_color = allTerritories.get(terrName).getColor();
      if (player_color.equals("Hidden")) {
        Territory<Character> hiddenTerr = (Territory<Character>) oldMap.getAllTerritories().get(terrName);
        hiddenTerr.updateColor("Hidden");
        newMap.updateTerritoryInMap(terrName, hiddenTerr);
      }
    }
    theTextPlayer.theMap = new V2Map<Character>(newMap);

    updateUIMap();
    // updateBox
    setOrderPane();

    // receive game status
    // Step-2 in Master playGame() in server
    String gameStatus = receiveAndUpdateGameStatus();

    if (gameStatus.startsWith("Ready")) {
      // ready for next turn
      // re-enable commit button
      order.setDisable(false);
      commit.setDisable(false);
    } else {
      // end of game
      GameStatus.appendText(" GAME OVER!!!");
    }
  }

  /**
   * method to update the map after each turn
   */
  private void updateUIMap() {
    // update player's resources

    updatePlayerResourceDisplay();

    HashMap<String, Territory<Character>> allTerritories = theTextPlayer.theMap.getAllTerritories();
    for (String terrName : TerritoryNames) {
      // Update color of territory
      String player_color = allTerritories.get(terrName).getColor();
      String button_style = "-fx-background-color: rgba(240,240,240,.3)"; // default: white
      if (player_color.equals("Green")) {
        button_style = "-fx-background-color: rgba(60,179,113,.3)";
      } else if (player_color.equals("Blue")) {
        button_style = "-fx-background-color: rgba(0,0,255,.3)";
      }
      TerritoryButtons.get(terrName).setStyle(button_style);
    }
  }

  /**
   * method to receive the game status and update the GUI
   */
  private String receiveAndUpdateGameStatus() {
    String gamestatus = theTextPlayer.receiveAndPrintGameStatus();
    GameStatus.setText(gamestatus);
    return gamestatus;
  }

  /**
   * Method to show/hide specific order details pane based on selected action
   */
  @FXML
  void OnSelectedOrderType() {
    String selectedAction = getAction();
    if (selectedAction.equals("Upgrade")) {
      UpgradePane.setVisible(true);
      MoveAttackPane.setVisible(false);
      CloakingPane.setVisible(false);

    } else if (selectedAction.equals("Cloak")) {
      UpgradePane.setVisible(false);
      MoveAttackPane.setVisible(false);
      CloakingPane.setVisible(true);
    } else if (selectedAction.equals("Move") || selectedAction.equals("Attack")) {
      MoveAttackPane.setVisible(true);
      UpgradePane.setVisible(false);
      CloakingPane.setVisible(false);

    } else {
      MoveAttackPane.setVisible(false);
      UpgradePane.setVisible(false);
      CloakingPane.setVisible(false);

    }
  }

  @FXML
  void onTerrButtonClick(MouseEvent event) {
    Button sourceButton = (Button) event.getSource();
    String terrName = sourceButton.getText();

    setTerritoryDetailsDisplay(terrName);
  }

}
