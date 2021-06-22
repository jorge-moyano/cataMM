package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.sqlclient.Row;

public class ElementHandler implements Handler<RoutingContext> {

  private PostgresDB app;

  public ElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {
    app.find(new Queries().getGetQuery(), event.pathParam("elementId"))
      .subscribe(event::json);

  }
}
