package edu.duke.ece651.mp.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.V1Map;
import edu.duke.ece651.mp.common.MoveChecking;
import edu.duke.ece651.mp.common.OwnerChecking;
import edu.duke.ece651.mp.common.PathChecking;
import edu.duke.ece651.mp.common.Territory;
public class PathCheckingTest {
  @Test
  public void test_path() {
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    PathChecking<Character> check=new PathChecking<>(null); 
    V1Map<Character> map = new V1Map<>(players_colors);
    assertEquals(check.checkMyRule(map,"Narnia","Midemio",2), null);
    assertEquals(check.checkMyRule(map,"Narnia","Oz",2), null);
  }
  @Test
  public void test_chain(){
    OwnerChecking<Character> ocheck=new OwnerChecking<>(null);
    PathChecking<Character> pcheck = new PathChecking<>(ocheck);
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    V1Map<Character> map = new V1Map<>(players_colors);
    assertEquals(pcheck.checkMyRule(map,"Narnia","Midemio",2),null);
    //assertEquals(pcheck.checkMyRule(map,"Narnia","Elantris",2), "no valid path exists");
  }

}
