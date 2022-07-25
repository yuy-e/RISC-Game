package edu.duke.ece651.mp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.*;
import java.io.ObjectInputStream;
import edu.duke.ece651.mp.common.TurnList;
import edu.duke.ece651.mp.server.MasterServer;

public class PlayerThread<T> implements Runnable {
  public Socket player_socket;
  public Object obj;

  public PlayerThread(Socket player_socket) {
    this.player_socket = player_socket;
  }

  @Override
  public void run(){
    try{
        // Read Object from Clients
        InputStream o = player_socket.getInputStream();
        ObjectInputStream s = new ObjectInputStream(o);
        Object obj = s.readObject();
        this.obj = obj;
    }catch(Exception e){
        e.printStackTrace();
    }
  }
}
