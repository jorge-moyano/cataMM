package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

public class AppValidationHandler implements Handler<RoutingContext>{

  private PostgresDB app;

  public AppValidationHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {
    event.next();
  }
}
