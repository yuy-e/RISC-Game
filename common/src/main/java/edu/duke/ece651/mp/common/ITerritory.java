package edu.duke.ece651.mp.common;
import java.util.ArrayList;

public interface ITerritory<T> {
  public int getUnit();
  public String getName();
  public String getColor();
  public void addAdjacency(String name);
  public ArrayList<String> getAdjacency();
}
