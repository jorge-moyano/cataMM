package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

public class ElementHandler implements Handler<RoutingContext> {

  private PostgresDB app;

  public ElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {

  }
}
