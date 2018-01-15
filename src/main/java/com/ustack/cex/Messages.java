/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author nuwansa
 */
public class Messages {

  public static JsonObject ping = new JsonObject();
  public static JsonObject pong = new JsonObject();
  public static JsonObject ticker = new JsonObject();
  
  private static long counter = 0;

  public static void init() {
    ping.addProperty("e", "ping");
    pong.addProperty("e", "pong");

    ticker.addProperty("e", "subscribe");
    JsonArray arr = new JsonArray();
    arr.add("tickers");
    ticker.add("rooms", arr);

  }

  public static JsonObject login(String userID, String apiKey, String apiSecret) throws UnsupportedEncodingException {
    int time = (int) (System.currentTimeMillis() / 1000);
    JsonObject login = new JsonObject();
    login.addProperty("e", "auth");
    JsonObject auth = new JsonObject();
    auth.addProperty("key", apiKey);
    auth.addProperty("signature", signature(userID, apiKey, apiSecret, time));
    auth.addProperty("timestamp", time);
    login.add("auth", auth);
    return login;
  }

  public static JsonObject orderBookSub(String symbol, int depth) {
    JsonObject msg = new JsonObject();
    msg.addProperty("e", "order-book-subscribe");
    JsonObject data = new JsonObject();
    JsonArray pair = new JsonArray();
    pair.add(symbol);
    pair.add("USD");

    data.add("pair", pair);
    data.addProperty("subscribe", true);
    data.addProperty("depth", depth);
    msg.addProperty("", Boolean.FALSE);
    msg.add("data", data);
    msg.addProperty("oid", System.currentTimeMillis() + "_" + (counter++) + "_order-book-subscribe");
    return msg;
  }

  public static JsonObject placeOrder(String symbol,BigDecimal amount,BigDecimal price,String type)
  {
    JsonObject placeOrder = new JsonObject();
    placeOrder.addProperty("e", "place-order");
    JsonObject data = new JsonObject();
    JsonArray array = new JsonArray();
    array.add(symbol);
    array.add("USD");
    data.add("pair", array);
    data.addProperty("amount", amount);
    data.addProperty("price", price);
    data.addProperty("type", type);
    placeOrder.add("data", data);
    placeOrder.addProperty("oid", System.currentTimeMillis() + "_" + (counter++) + "_place_order");
    return placeOrder;
  }
  
  public static JsonObject balance()
  {
    JsonObject json = new JsonObject();
    json.addProperty("e", "get-balance");
    json.add("data", new JsonObject());
    json.addProperty("oid", System.currentTimeMillis() + "_" + (counter++) + "_get_balance");
    return json;
  }
  
  public static JsonObject ping() {
    ping.addProperty("time", System.currentTimeMillis());
    return ping;
  }

  private static String signature(String userID, String apiKey, String apiSecret, int time) {
    String message = time + apiKey;
    Mac hmac = null;

    try {
      hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key
              = new SecretKeySpec(((String) apiSecret).getBytes("UTF-8"), "HmacSHA256");
      hmac.init(secret_key);
    } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return String.format("%X", new BigInteger(1, hmac.doFinal(message.getBytes())));
  }
}
