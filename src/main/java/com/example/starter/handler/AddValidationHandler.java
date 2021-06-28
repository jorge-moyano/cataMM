package com.example.starter.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class AddValidationHandler implements Handler<RoutingContext>{

  @Override
  public void handle(RoutingContext event) {

    JsonObject newElement = event.getBodyAsJson();

    if(validateJson(newElement))
      event.next();
    else
      event.response()
        .putHeader("content-type", "application/body")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end("Input error");

  }

  private boolean validateJson(JsonObject json) {
    try {
      json.getString("name");
      json.getString("category");
      json.getInteger("number");
      json.getInteger("period");
      json.getString("summary");
      json.getString("symbol");
    } catch (ClassCastException e){
      return false;
    }
    return true;
  }

}
