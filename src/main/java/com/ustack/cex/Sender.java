/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author nuwansa
 */
public class Sender extends WebSocketClient {

  private String userID;
  private String apiKey;
  private String apiSecret;
  private CountDownLatch latch;
  private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
  private Consumer<JsonObject> consumer;
  private JsonParser parser = new JsonParser();

  public Sender(String userID,String apiKey,String apiSecret) throws URISyntaxException {
    super(new URI("wss://ws.cex.io/ws/"));
    this.userID = userID;
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
  }

  @Override
  public void onOpen(ServerHandshake sh) {
    System.out.println("connected");
  }

  @Override
  public void onMessage(String string) {
    latch.countDown();
    JsonObject msg = parser.parse(string).getAsJsonObject();
    String evtType = msg.get("e").getAsString();
    System.out.println(">> "+string);
    if (evtType.equals("ping")) {
      send(Messages.pong);
    } 
    consumer.accept(msg);
  }

  @Override
  public void onClose(int i, String string, boolean bln) {
    System.out.println("close " + string);
  }

  @Override
  public void onError(Exception excptn) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public void send(JsonObject json) {
    this.send(json.toString());
  }

  public void login() throws UnsupportedEncodingException {
    send(Messages.login(userID, apiKey, apiSecret));
  }

  public void connect(Consumer<JsonObject> consumer) {
    this.consumer = consumer;
    super.connect();
    latch = new CountDownLatch(1);
    try {
      latch.await();
    } catch (InterruptedException ex) {
      Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
    }
    //service.scheduleAtFixedRate(()->send(Messages.ping()), 1,10,TimeUnit.SECONDS);
  }
}
