package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class TurnList implements Serializable {
  public String player_info;
  public ArrayList<Turn> order_list;

  public TurnList(){
    this.player_info = "";
    this.order_list = new ArrayList<>();
  }

  public TurnList(String player_info) {
    this.player_info = player_info;
    this.order_list = new ArrayList<>();
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
  }

  public int getListLength() {
    return order_list.size();
  }

  public void addTurn(Turn newTurn) {
    order_list.add(newTurn);
  }

  public void printTurnList(){
    for(Turn t: order_list){
      t.printTurn();
    }
  }
}
