package edu.duke.ece651.mp.common;
import java.util.ArrayList;

public interface IITerritory<T> {
    public ArrayList<Unit> getUnitList();
    public String getName();
    public String getColor();
    public void addAdjacency(String name);
    public ArrayList<String> getAdjacency();
}
