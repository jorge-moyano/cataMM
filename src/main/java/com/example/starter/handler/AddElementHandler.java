package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.ext.web.RoutingContext;

public class AddElementHandler implements Handler<RoutingContext>{

  private PostgresDB app;

  public AddElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {

    MultiMap attributes = event.request().formAttributes();

    if(attributes.get("id") == null) {
      event.response().putHeader("content-type", "application/json")
        .end((Handler<AsyncResult<Void>>) new JsonObject().put("error", "The new element id must be specified"));
      return;
    }

    JsonObject newElement = new JsonObject()
      .put("id", attributes.get("id"))
      .put("category", attributes.get("category"))
      .put("number", Long.parseLong(attributes.get("number")))
      .put("period", Long.parseLong(attributes.get("period")))
      .put("summary", attributes.get("summary"))
      .put("symbol", attributes.get("symbol"));

    app.insert(newElement)
      .subscribe(id -> {
        System.out.println(id);
        event.json(newElement.put("_id", id));
      }, error -> {
        System.out.println(error.getMessage());
        event.response()
          .putHeader("content-type", "application/json")
          .end(Json.encodePrettily(new JsonObject().put("response","Error")));

      });
  }
}
