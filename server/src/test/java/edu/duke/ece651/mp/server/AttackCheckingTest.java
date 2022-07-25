package edu.duke.ece651.mp.server;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.V1Map;
import edu.duke.ece651.mp.common.AttackTurn;
import edu.duke.ece651.mp.common.Territory;
public class AttackCheckingTest {
  @Test
  public void test_attackingcheck() {
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    V1Map<Character> map = new V1Map<>(players_colors);
    AttackChecking<Character> check=new AttackChecking<>();
    AttackTurn attackOrder1 = new AttackTurn("Narnia","Midemio",2, "Green");
    assertEquals(check.checkMyRule(map,attackOrder1), false);
    
    AttackTurn attackOrder2 = new AttackTurn("Narnia","Elantris",10, "Green");
    assertEquals(check.checkMyRule(map, attackOrder2), false);

    AttackTurn attackOrder3 = new AttackTurn("Narnia","Elantris",5, "Green");
    assertEquals(check.checkMyRule(map, attackOrder3), true);

    AttackTurn attackOrder4 = new AttackTurn("Narnia","Scadnal",10, "Green");
    assertEquals(check.checkMyRule(map, attackOrder4), false);
    
  }

}
