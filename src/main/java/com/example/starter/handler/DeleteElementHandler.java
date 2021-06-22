package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.core.Handler;

public class DeleteElementHandler implements Handler<RoutingContext> {

  private final PostgresDB app;

  public DeleteElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {
    app.update(new Queries().getModifyQuery(), event.pathParam("elementId"), event.pathParam("newValue")).subscribe();
  }

}
