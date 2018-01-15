/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import com.google.gson.JsonObject;
import java.math.BigDecimal;

/**
 *
 * @author nuwansa
 */
public class TickerHandler extends StreamHandler {

  private BigDecimal ltp = BigDecimal.ZERO;

  protected void onLastTradePrice(JsonObject json) {
    ltp = new BigDecimal(json.get("price").getAsString());
    System.out.println("ltp " + ltp);
  }
}
