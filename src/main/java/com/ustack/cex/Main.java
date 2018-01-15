/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustack.cex;

import static com.ustack.cex.Utils.jval;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 *
 * @author nuwansa
 */
public class Main {
  public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {
    
    String userID = "xxx";
    String apiKey = "xxx";
    String apiSecret = "xxx";
    
    Messages.init();
    TickerHandler streamHandler = new TickerHandler();
    PortfolioHandler portHandler = new PortfolioHandler();
    StreamFilter filter = new StreamFilter();
    streamHandler.subscribe(filter,Events.TICK, json->{return jval("symbol1", json).equals("XRP") && jval("symbol2", json).equals("USD");},streamHandler::onLastTradePrice);
  //  filter.dispatch("tick", json->{return jval("symbol1", json).equals("XRP") && jval("symbol2", json).equals("USD");}, streamHandler,streamHandler::onLastTradePrice);
    portHandler.subscribe(filter, Events.GET_BALANCE, json->true,portHandler::onBalance);
  //filter.dispatch("get-balance", json->true, portHandler,portHandler::onBalance);
    Sender sender = new Sender(userID,apiKey,apiSecret);
    sender.connect(e->filter.onNext(e));
    sender.login();
    sender.send(Messages.ticker);
    sender.send(Messages.balance());
  //  sender.send(Messages.orderBookSub("XRP", 10));
  }
}
