package edu.duke.ece651.mp.common;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.MoveTurn;
import edu.duke.ece651.mp.common.Territory;

public class MoveTurnTest {
  @Test
  public void test_MoveTurn() throws UnknownHostException, IOException{
        String t1 = "Narnia";
        String t2 = "Midemio";
        MoveTurn mt = new MoveTurn(t1, t2, 2, "Green");
        assertEquals("Move", mt.type);
        assertEquals(t1, mt.getSource());
        assertEquals(t2, mt.getDestination());
        assertEquals(2, mt.num_unit);
        assertEquals("Green", mt.getPlayerColor());
    }
}
