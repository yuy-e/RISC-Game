package edu.duke.ece651.mp.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.OwnerChecking;
import edu.duke.ece651.mp.common.PathChecking;
import edu.duke.ece651.mp.common.V2Map;

public class TextPlayerTest {
  @Disabled
  @Test
  void test_takeTurn() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    String dummy_input = "M\n1\n1\n1\n1\nA\n1\n1\n1\nghj\nD\n1\n";
    BufferedReader input = new BufferedReader(new StringReader(dummy_input));
    TextPlayer player = new TextPlayer("null", 0, input, output);

    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    //OwnerChecking<Character> ownerchecker = new OwnerChecking<Character>(null);
    //PathChecking<Character> pathchecker = new PathChecking<Character>(ownerchecker);
    V2Map<Character> m = new V2Map<Character>(players_colors);
    player.setIdentity("Green");
    player.updateMap(m);

    //player.takeTurn();
    String expected = "You are the Green player and it's time to take your turn!.\n"
      + "There are two types of orders that you may issue: move and attack.You may issue any number of each type of these orders in a turn.Once you're done enetering your orders, hit D and your turn will be sent to the server.\n\n"
      + "\nEnter new order (M or A or D)\n"
      + "(M)ove\n"
      + "(A)ttack\n"
      + "(D)one\n"
      + "Requested order: Move\n"
      + "- From which territory? (Enter the option# from following list)\n"
      + "(1) Narnia\n"
      + "(2) Midemio\n"
      + "(3) Oz\n"
      + "You selected Narnia as the source.\n"
      + "- To which territory? (Enter the option# from following list)\n"
      + "(1) Elantris\n"
      + "(2) Roshar\n"
      + "(3) Scadnal\n"
      + "You selected Elantris as the destination.\n"
      + "- How many units?\n"
      + "Requested 1 units.\n"
      + "\nEnter new order (M or A or D)\n"
      + "(M)ove\n"
      + "(A)ttack\n"
      + "(D)one\n"
      + "That order is invalid: it does not have the correct format.\n"
      + "Please re-enter correctly!\n"
      + "\nEnter new order (M or A or D)\n"
      + "(M)ove\n"
      + "(A)ttack\n"
      + "(D)one\n"
      + "Requested order: Attack\n"
      + "- From which territory? (Enter the option# from following list)\n"
      + "(1) Narnia\n"
      + "(2) Midemio\n"
      + "(3) Oz\n"
      + "You selected Narnia as the source.\n"
      + "- To which territory? (Enter the option# from following list)\n"
      + "(1) Elantris\n"
      + "(2) Roshar\n"
      + "(3) Scadnal\n"
      + "You selected Elantris as the destination.\n"
      + "- How many units?\n"
      + "Requested 1 units.\n"
      + "\nEnter new order (M or A or D)\n"
      + "(M)ove\n"
      + "(A)ttack\n"
      + "(D)one\n"
      + "That order is invalid: it does not have the correct format.\n"
      + "Please re-enter correctly!\n"
      + "\nEnter new order (M or A or D)\n"
      + "(M)ove\n"
      + "(A)ttack\n"
      + "(D)one\n"
      + "Done with the turn!\n";
    assertEquals(expected, bytes.toString());
    bytes.reset(); // clear out bytes for next time around
  }
}
