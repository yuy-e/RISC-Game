package edu.duke.ece651.mp.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;

public class AttackTurnTest {
  @Test
  public void test_AttackTurn() throws UnknownHostException, IOException{
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

