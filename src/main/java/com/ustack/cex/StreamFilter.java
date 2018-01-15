/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author nuwansa
 */
public class StreamFilter {

  private final Map<String, Container> subscribers = new ConcurrentHashMap<>();

  public void dispatch(String msgType, Function<JsonObject, Boolean> filter, StreamHandler hnd, Consumer<JsonObject> consumer) {
    subscribers.put(msgType, new Container(filter, hnd, consumer));
  }

  public void onNext(JsonObject json) {
    String evtType = json.get("e").getAsString();
    Container container = subscribers.get(evtType);
    if (container != null) {
      JsonObject data = json.getAsJsonObject("data");
      if (container.getFilter().apply(data)) {
        container.getHnd().getExecutor().execute(() -> container.getConsumer().accept(data));
      }
    }
  }

  public static final class Container {

    private Function<JsonObject, Boolean> filter;
    private StreamHandler hnd;
    private Consumer<JsonObject> consumer;

    public Container(Function<JsonObject, Boolean> filter, StreamHandler hnd, Consumer<JsonObject> consumer) {
      this.filter = filter;
      this.hnd = hnd;
      this.consumer = consumer;
    }

    public Consumer<JsonObject> getConsumer() {
      return consumer;
    }

    public Function<JsonObject, Boolean> getFilter() {
      return filter;
    }

    public StreamHandler getHnd() {
      return hnd;
    }

  }
}
