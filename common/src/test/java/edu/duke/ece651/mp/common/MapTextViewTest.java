package edu.duke.ece651.mp.common;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.MapTextView;
import edu.duke.ece651.mp.common.V1Map;

public class MapTextViewTest {
  @Test
  public void test_displayMap() {
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    //OwnerChecking<Character> ownerchecker = new OwnerChecking<Character>(null);
    //PathChecking<Character> pathchecker = new PathChecking<Character>(ownerchecker);
    V1Map<Character> m = new V1Map<Character>(players_colors);
    MapTextView mapView = new MapTextView(m);
    String expected = "Green player:\n" + "-----------\n"
      + "8 units in Narnia (next to: Midemio, Elantris)\n"
      + "3 units in Midemio (next to: Narnia, Oz, Elantris, Scadnal)\n"
      + "12 units in Oz (next to: Midemio, Scadnal)\n"
      + "\n"
      + "Blue player:\n" + "-----------\n"
      + "7 units in Elantris (next to: Scadnal, Narnia, Roshar, Midemio)\n"
      + "6 units in Roshar (next to: Elantris, Scadnal)\n"
      + "10 units in Scadnal (next to: Roshar, Elantris, Oz, Midemio)\n";
    assertEquals(expected, mapView.displayMap());
  }
}
