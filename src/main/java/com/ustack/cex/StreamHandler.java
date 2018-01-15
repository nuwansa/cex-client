/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import com.google.gson.JsonObject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author nuwansa
 */
public abstract class StreamHandler {

  private final Executor executors = Executors.newSingleThreadExecutor();

  public Executor getExecutor() {
    return executors;
  }

  public void subscribe(StreamFilter filterStream, String eventType, Function<JsonObject, Boolean> filter, Consumer<JsonObject> consumer) {
    filterStream.dispatch(eventType, filter, this, consumer);
  }
 
}
