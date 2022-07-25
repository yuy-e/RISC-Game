package edu.duke.ece651.mp.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.MapTextView;
import edu.duke.ece651.mp.common.OwnerChecking;
import edu.duke.ece651.mp.common.PathChecking;
import edu.duke.ece651.mp.common.V1Map;
import edu.duke.ece651.mp.common.Territory;   
public class MasterServerTest {
  @Test
  public void test_getPort() throws IOException {
    MasterServer ms = new MasterServer(8002, 1);
    assertEquals(8002, ms.getPort());
    assertEquals(1, ms.num_players);
    assertEquals(new ArrayList<Socket>(), ms.player_socket_list);
    ms.close();
  }

  // help client to send obj
  public void sendToServer_helper(Socket socket, Object obj) {
    try {
      OutputStream o = socket.getOutputStream();
      ObjectOutputStream s = new ObjectOutputStream(o);
      s.writeObject(obj);
      s.flush();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  // help client to receive obj
  public Object receiveFromServer_helper(Socket socket) {
    try {
      InputStream o = socket.getInputStream();
      ObjectInputStream s = new ObjectInputStream(o);
      Object obj = s.readObject();
      // s.close();
      return obj;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  @Test
  public void test_acceptplayers() throws UnknownHostException, IOException, InterruptedException {
    MasterServer ms = new MasterServer(8003, 1);
    Socket s = new Socket("127.0.0.1", 8003);
    String msg = "for testing";
    sendToServer_helper(s, msg);
    ms.acceptPlayers();
    assertNotNull(ms.player_socket_list);
    ms.close();
    s.close();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void test_sendToAll() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
    MasterServer ms = new MasterServer(8004, 1);
    Socket soc = new Socket("127.0.0.1", 8004);
    
    // client send msg to server
    String msg = "for testing";
    sendToServer_helper(soc, msg);
    ms.acceptPlayers();
    assertNotNull(ms.player_socket_list);

    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));
    OwnerChecking<Character> ocheck=new OwnerChecking<>(null);
    PathChecking<Character> pcheck=new PathChecking<>(ocheck);
    V1Map<Character> Mymap = new V1Map<Character>(players_colors);
    ms.sendToAll(Mymap);
    
    // client receive msg from server
    Object rec_obj = receiveFromServer_helper(soc);
    assertNotNull(rec_obj);
    String expected = "Green player:\n" + "-----------\n"
      + "8 units in Narnia (next to: Midemio, Elantris)\n"
      + "3 units in Midemio (next to: Narnia, Oz, Elantris, Scadnal)\n"
      + "12 units in Oz (next to: Midemio, Scadnal)\n"
      + "\n"
      + "Blue player:\n" + "-----------\n"
      + "7 units in Elantris (next to: Scadnal, Narnia, Roshar, Midemio)\n"
      + "6 units in Roshar (next to: Elantris, Scadnal)\n"
      + "10 units in Scadnal (next to: Roshar, Elantris, Oz, Midemio)\n";;
    
    //assertEquals(expected, obj);
    V1Map<Character> actual = (V1Map<Character>)rec_obj;
    assertNotNull(actual);
    // To Do: transfer obj to string display.
    MapTextView map_view = new MapTextView(actual);
    assertNotNull(actual);
    assertEquals(expected, map_view.displayMap());
    ms.close();
    soc.close();
  }
  @Test
  public void test_detect() throws  UnknownHostException, IOException, ClassNotFoundException,InterruptedException{
    MasterServer m = new MasterServer(8008, 1);
    Socket soc = new Socket("127.0.0.1", 8008);
    String msg = "for testing";                                                                         sendToServer_helper(soc, msg);
    m.acceptPlayers();
    ArrayList<String> players_colors = new ArrayList<String>(Arrays.asList("Green", "Blue"));           V1Map<Character> Mymap = new V1Map<Character>(players_colors);                                      HashMap<String, Territory<Character>> myTerritories=Mymap.getAllTerritories();    
    m.sendToAll(Mymap);    

    assertEquals(m.detectresult(Mymap),null);
    //    assertEquals(m.sendresult(Mymap), "Please continue");

    assertNull(m.detectresult(Mymap));
    //assertEquals(m.sendresult(Mymap), "Please continue");

    for(String s:myTerritories.keySet()){
      Territory<Character> terr=myTerritories.get(s);
      terr.updateColor("Blue");
    }
    assertEquals(m.detectresult(Mymap),"Blue");
    // assertEquals(m.sendresult(Mymap), "Blue player has won"); 
    m.close();
    soc.close();

  }

} 
