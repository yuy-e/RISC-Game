package edu.duke.ece651.mp.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

import edu.duke.ece651.mp.common.OwnerChecking;
import edu.duke.ece651.mp.common.PathChecking;
import edu.duke.ece651.mp.common.V1Map;

public class ClientTest {
  @Test
  void test_map() throws InterruptedException, IOException, UnknownHostException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Client theClient = new Client("null", 0, input, System.out); // we don't care about server yet!
    // The map should be received from master
    // using minimal V1Map for now
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));

    //OwnerChecking<Character> ownerchecker = new OwnerChecking<Character>(null);
    //PathChecking<Character> pathchecker = new PathChecking<Character>(ownerchecker);
    V1Map<Character> mapFromServer = new V1Map<Character>(players_colors);
    theClient.theTextPlayer.updateMap(mapFromServer);

    String expected = "Green player:\n" + "-----------\n"
      + "8 units in Narnia (next to: Midemio, Elantris)\n"
      + "3 units in Midemio (next to: Narnia, Oz, Elantris, Scadnal)\n"
      + "12 units in Oz (next to: Midemio, Scadnal)\n"
      + "\n"
      + "Blue player:\n" + "-----------\n"
      + "7 units in Elantris (next to: Scadnal, Narnia, Roshar, Midemio)\n"
      + "6 units in Roshar (next to: Elantris, Scadnal)\n"
      + "10 units in Scadnal (next to: Roshar, Elantris, Oz, Midemio)\n";
    assertEquals(expected, theClient.theTextPlayer.view.displayMap());
  }

  @Disabled
  @Test
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  void test_main_V1() throws IOException, InterruptedException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
    InputStream input = getClass().getClassLoader().getResourceAsStream("input1.txt");
    assertNotNull(input);
    InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output1.txt");
    assertNotNull(expectedStream);
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
    try {
      System.setIn(input);
      System.setOut(out);
      String[] args = { "null", "0" };
      Client.main(args);
    } finally {
      System.setIn(oldIn);
      System.setOut(oldOut);
    }
    String expected = new String(expectedStream.readAllBytes());
    String actual = bytes.toString();
    assertEquals(expected, actual);
  }

}
