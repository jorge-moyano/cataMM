package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.core.Handler;

public class UpdateElementHandler implements Handler<RoutingContext> {

  private final PostgresDB app;

  public UpdateElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {
    app.update(new Queries().getModifyQuery(), event.pathParam("elementId"), event.pathParam("newValue"))
      .subscribe(r -> event.response().send("Element updated"));
  }

}
