package edu.duke.ece651.mp.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.AttackTurn;
import edu.duke.ece651.mp.common.MoveTurn;
import edu.duke.ece651.mp.common.OwnerChecking;
import edu.duke.ece651.mp.common.PathChecking;
import edu.duke.ece651.mp.common.Territory;
import edu.duke.ece651.mp.common.TurnList;
import edu.duke.ece651.mp.common.Map;
import edu.duke.ece651.mp.common.V1Map;

public class HandleOrderTest {

  @Test
  @SuppressWarnings("unchecked")
  public void test_handleAllMoveOrder() {
    V1Map theMap = new V1Map();
    assertNotNull(theMap);
    MoveTurn mo = new MoveTurn("Narnia", "Midemio", 1, "Green");
    ArrayList<TurnList> all_order_list = new ArrayList<TurnList>();
    TurnList tl = new TurnList("Green");
    tl.order_list.add(mo);
    all_order_list.add(tl);
    assertNotNull(all_order_list);
    PathChecking<Character> pcheck = new PathChecking<>(null);
    OwnerChecking<Character> ocheck = new OwnerChecking<>(pcheck);
    HandleOrder ho = new HandleOrder(all_order_list, theMap, ocheck);
    ho.handleAllMoveOrder();
    assertEquals(7, ((Territory<Character>) ho.theMap.getAllTerritories().get("Narnia")).getUnit());
    assertEquals(4, ((Territory<Character>) ho.theMap.getAllTerritories().get("Midemio")).getUnit());
  }

  
  @SuppressWarnings("unchecked")
  @Test
  public void test_handleAllAttackOrder() {
    Territory<Character> dep = new Territory<Character>("Narnia", "Green", new ArrayList<String>(), 2);
    Territory<Character> des = new Territory<Character>("Midemio", "Blue", new ArrayList<String>(), 3);
    AttackTurn mo = new AttackTurn("Narnia", "Midemio", 1, "Green");
    ArrayList<TurnList> all_order_list = new ArrayList<TurnList>();
    TurnList tl = new TurnList("Green");
    tl.order_list.add(mo);
    all_order_list.add(tl);
    ArrayList<String> players_colors = new ArrayList<String>();
    players_colors.add("Green");
    players_colors.add("Blue");
    V1Map theMap = new V1Map(players_colors);
    PathChecking<Character> pcheck = new PathChecking<>(null);
    OwnerChecking<Character> ocheck = new OwnerChecking<>(pcheck);
    HandleOrder ho = new HandleOrder(all_order_list, theMap, ocheck);
    ho.handleAllAttackOrder();
    // To add assert

  }

  @Test
  public void test_updateMapbyOneUnit() {
    HandleOrder ho = new HandleOrder();
    ho.updateMapbyOneUnit();
    assertEquals(9, ((Territory<Character>) ho.theMap.getAllTerritories().get("Narnia")).getUnit());
  }

  @Disabled
  @Test
  public void test_handleOrders_Insuffcient_units() {
    // Test Move Orders with checker
    Map<Character> theMap = new V1Map();
    assertNotNull(theMap);
    // valid
    MoveTurn mo1 = new MoveTurn("Narnia", "Midemio", 1, "Green");
    // Insuffcient units
    MoveTurn mo2 = new MoveTurn("Narnia", "Midemio", 10, "Green");
    // not the same owner, have path
    MoveTurn mo3 = new MoveTurn("Narnia", "Elantris", 2, "Green");
    // the same owner, don't have path

    ArrayList<TurnList> all_order_list = new ArrayList<TurnList>();
    TurnList tl = new TurnList("Green");
    tl.order_list.add(mo1);
    tl.order_list.add(mo2);
    tl.order_list.add(mo3);
    // tl.order_list.add(mo4);

    all_order_list.add(tl);
    assertNotNull(all_order_list);
    PathChecking<Character> pcheck = new PathChecking<>(null);
    OwnerChecking<Character> ocheck = new OwnerChecking<>(pcheck);
    HandleOrder<Character> ho = new HandleOrder<Character>(all_order_list, theMap, ocheck);
    String actual = ""; // FIX!!!
    ho.handleOrders(all_order_list, theMap);
    String expected = "Insuffcient units";
    assertEquals(expected, actual);
    // TO DO : test attack orders with checker
  }

  @Disabled
  @Test
  public void test_handleOrders_not_same_owner() {
    // Test Move Orders with checker
    V1Map theMap = new V1Map();
    assertNotNull(theMap);
    // not the same owner, have path
    MoveTurn mo3 = new MoveTurn("Narnia", "Elantris", 2, "Green");
    ArrayList<TurnList> all_order_list = new ArrayList<TurnList>();
    TurnList tl = new TurnList("Green");
    tl.order_list.add(mo3);
    all_order_list.add(tl);
    assertNotNull(all_order_list);
    PathChecking<Character> pcheck = new PathChecking<>(null);
    OwnerChecking<Character> ocheck = new OwnerChecking<>(pcheck);

    HandleOrder ho = new HandleOrder(all_order_list, theMap, ocheck);
    String actual = ""; // FIX!!!!
    //ho.handleOrders();
    String expected = "not same owner";
    assertEquals(expected, actual);
    // TO DO : test attack orders with checker
  }

  @Disabled
  @Test
  public void test_handleOrders_noValidPath() {
    // Test Move Orders with checker
    V1Map theMap = new V1Map();
    // change the owner of territory: Midemio
    theMap.myTerritories.put("Midemio", new Territory("Midemio", "Blue", new ArrayList<String>(), 3));
    assertNotNull(theMap);

    // the same owner, don't have path
    MoveTurn mo4 = new MoveTurn("Narnia", "Oz", 3, "Green");
    ArrayList<TurnList> all_order_list = new ArrayList<TurnList>();
    TurnList tl = new TurnList("Green");
    tl.order_list.add(mo4);
    all_order_list.add(tl);
    assertNotNull(all_order_list);

    PathChecking<Character> pcheck = new PathChecking<>(null);
    OwnerChecking<Character> ocheck = new OwnerChecking<>(pcheck);
    HandleOrder ho = new HandleOrder(all_order_list, theMap, ocheck);
    String actual = ""; // FIX!!!
    //ho.handleOrders();
    String expected = "no valid path exists";
    assertEquals(expected, actual);
    // TO DO : test attack orders with checker
  }

}
