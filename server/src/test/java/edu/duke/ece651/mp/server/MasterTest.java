package edu.duke.ece651.mp.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.mp.common.V1Map;

public class MasterTest {
  @Test
  public void test_master() throws IOException {
    Master m = new Master(8000, 2);
    assertEquals(8000, m.theMasterServer.port);
    assertEquals(2, m.theMasterServer.num_players);
    // V1Map<Character> map = new V1Map<Character>();
    // assertEquals(map, m.theMap);
    assertNotNull(m.players_identity);
    m.close();
  }

  // help client to send Obj
  public void sendToServer_helper(Socket socket, Object obj) {
    try {
      OutputStream o = socket.getOutputStream();
      ObjectOutputStream s = new ObjectOutputStream(o);
      s.writeObject(obj);
      s.flush();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("Error during serialization");
      e.printStackTrace();
    }
  }

  // help client to receive Obj
  public Object receiveFromServer(Socket socket) {
    try {
      InputStream o = socket.getInputStream();
      ObjectInputStream s = new ObjectInputStream(o);
      Object obj = s.readObject();
      // s.close();
      return obj;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("Error during serialization");
      e.printStackTrace();
      return null;
    }
  }

  @Test
  public void test_acceptplayers() throws IOException, InterruptedException {
    Master m = new Master(8001, 1);
    Socket s1 = new Socket("127.0.0.1", 8001);
    String msg = "for testing";
    sendToServer_helper(s1, msg);
    m.acceptPlayers();
    // m.close();
    assertNotNull(m.theMasterServer.player_socket_list);
    // assertNull(m.theMasterServer.server_socket.accept());
    m.close();
    s1.close();
  }

  @Test
  public void test_sendMapToAll()
      throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
    Master m = new Master(8005, 1);
    Socket soc = new Socket("127.0.0.1", 8005);
    String msg = "for testing";
    sendToServer_helper(soc, msg);
    m.acceptPlayers();
    assertNotNull(m.theMap);
    m.sendMapToAll();

    Object obj = receiveFromServer(soc);
    assertNotNull(obj);
    //System.out.print("Compare SendMap and ReceivedMap");
    //assertEquals(m.theMap, (V1Map)obj);
    m.close();
    soc.close();

  }

  @Test
  public void test_sendPlayerIndentityToAll() throws UnknownHostException, IOException, InterruptedException {
    Master m = new Master(8006, 1);
    Socket soc = new Socket("127.0.0.1", 8006);
    String msg = "for testing";
    sendToServer_helper(soc, msg);
    m.acceptPlayers();

    m.sendPlayerIdentityToAll();
    Object obj = receiveFromServer(soc);
    assertNotNull(obj);
    assertEquals("Green", (String)obj);
    m.close();
    soc.close();
  }

  /*
  @Test
  public void test_receiveObjectFromPlayer() throws IOException, InterruptedException, ClassNotFoundException {
    Master m = new Master(8007, 1);
    Socket soc = new Socket("127.0.0.1", 8007);
    String msg = "for testing";
    sendToServer_helper(soc, msg);
    m.acceptPlayers();

    sendToServer_helper(soc, msg);
    System.out.println("Waiting for client to send msg - test");
    String ans = (String) m.receiveObjectFromPlayer();
    assertEquals(msg, ans);
  }*/
}
