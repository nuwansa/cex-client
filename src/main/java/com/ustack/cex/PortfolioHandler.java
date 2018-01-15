/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import com.google.gson.JsonObject;
import static com.ustack.cex.Utils.jval;

/**
 *
 * @author nuwansa
 */
public class PortfolioHandler extends StreamHandler {

  private JsonObject balance = new JsonObject();
  private JsonObject oBalance = new JsonObject();

  protected void onBalance(JsonObject json) {
    balance = jval(el -> el.getAsJsonObject(), "balance", json);
    oBalance = jval(el -> el.getAsJsonObject(), "obalance", json);

    System.out.println("balance " + balance);
    System.out.println("oBalance " + oBalance);
  }

}
