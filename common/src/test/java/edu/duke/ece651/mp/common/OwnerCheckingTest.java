package edu.duke.ece651.mp.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.MoveChecking;
import edu.duke.ece651.mp.common.OwnerChecking;
import edu.duke.ece651.mp.common.PathChecking;
import edu.duke.ece651.mp.common.V1Map;

public class OwnerCheckingTest {
  @Test
  public void test_owner() {
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    //OwnerChecking<Character> ownerchecker = new OwnerChecking<Character>(null);
    V1Map<Character> map = new V1Map<>(players_colors);
    OwnerChecking<Character> check=new OwnerChecking<>(null);
    assertEquals(check.checkMyRule(map,"Narnia","Midemio",2),null);
    assertEquals(check.checkMyRule(map,"Narnia","Midemio",10),"Insuffcient units");
    //assertEquals(check.checkMyRule(map,"Narnia","Elantris"),"not same owner");
    
  }
  @Test
  public void test_chain(){
    OwnerChecking<Character> ownerchecker = new OwnerChecking<Character>(null);
    PathChecking<Character> pathchcker = new PathChecking<>(ownerchecker);
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    
    V1Map<Character> map = new V1Map<>(players_colors);
    assertEquals(pathchcker.checkMyRule(map,"Narnia","Midemio",3),null);
    assertEquals(pathchcker.checkMyRule(map,"Narnia","Elantris",3),"no valid path exists");
  }
}
