package edu.duke.ece651.mp.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TechResourceList implements Serializable {
    public ArrayList<String> players_identity;
    public HashMap<String, TechResource> resource_list;

    public TechResourceList(ArrayList<String> players_identity) {
        this.players_identity = players_identity;
        resource_list = new HashMap<String, TechResource>();
        for (String playerColor : players_identity) {
            resource_list.put(playerColor, new TechResource(0));
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(resource_list);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        /*
         * Call even if there is no default serializable fields.
         * Enables default serializable fields to be added in future versions
         * and skipped by this version which has no default serializable fields.
         */
        s.defaultReadObject();
        // restore
        resource_list = (HashMap<String, TechResource>) s.readObject();
    }

    public void addResource(String color, TechResource techResource) {
        for (Map.Entry<String, TechResource> set : resource_list.entrySet()) {
            if (set.getKey().equals(color)) {
                int old_num = set.getValue().getResourceAmount();
                int new_num = old_num + techResource.getResourceAmount();
                resource_list.put(color, new TechResource(new_num));
                break;
            }
        }
    }

    /*
     * public void addResource(String color, int techResource) {
     * for (Map.Entry<String, TechResource> set : resource_list.entrySet()) {
     * if (set.getKey().equals(color)) {
     * int old_num = set.getValue().getResourceAmount();
     * int new_num = old_num + techResource;
     * resource_list.put(color, new TechResource(new_num));
     * break;
     * }
     * }
     * }
     */

    public void printList() {
        for (Map.Entry<String, TechResource> set : resource_list.entrySet()) {
            System.out.println(set.getKey() + ":" + set.getValue().getResourceAmount());
        }
    }

}
