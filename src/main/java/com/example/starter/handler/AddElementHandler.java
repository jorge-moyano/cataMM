package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

public class AddElementHandler implements Handler<RoutingContext>{

  private final PostgresDB app;

  public AddElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {

    JsonObject newElement = event.getBodyAsJson();

    app.insert(newElement).subscribe(__ -> event.response().setStatusCode(200).end());

    /*app.insert(newElement)
      .subscribe(id -> {
        System.out.println(id);
        event.json(newElement.put("_id", id));
      }, error -> {
        System.out.println(error.getMessage());
        event.response()
          .putHeader("content-type", "application/json")
          .end(Json.encodePrettily(new JsonObject().put("response","Error")));

      });*/
  }
}
