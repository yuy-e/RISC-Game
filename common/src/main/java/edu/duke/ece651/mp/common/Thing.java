package edu.duke.ece651.mp.common;

public class Thing {
  private String name;
  public Thing(String name){
    this.name = name;
  }
  @Override
  public String toString(){
    return "A thing in the " + name;
  }
}
