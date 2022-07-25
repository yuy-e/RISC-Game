package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Resource implements Serializable {
    private String type; // resources' type: food * tech
    private int amount; // production amount/per round game the territory produces.

    public Resource(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(type);
        s.writeObject(amount);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        type = (String) s.readObject();
        amount = (int) s.readObject();
    }

    public int getResourceAmount() {
        return this.amount;
    }

    public void setResourceAmount(int amount) {
        this.amount = amount;
    }
}
