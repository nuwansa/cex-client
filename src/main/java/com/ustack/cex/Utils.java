/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Function;

/**
 *
 * @author nuwansa
 */
public class Utils {

  public static final <T> T jval(Function<JsonElement, T> convertor, String member, JsonObject json) {
    JsonElement val = json.get(member);
    return convertor.apply(val);
  }

  public static final String jval(String member, JsonObject json) {
    return json.get(member).getAsString();
  }
}
