package edu.duke.ece651.mp.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TerritoryTest {
  @Test
  public void test_name() {

    Territory<Character> terr1=new Territory<Character>("Narnia","Green",new ArrayList<String>(), 2);

    assertEquals("Narnia",terr1.getName());
  }

}
