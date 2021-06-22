package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import io.vertx.core.AsyncResult;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.core.Handler;

public class DeleteElementHandler implements Handler<RoutingContext> {

  private final PostgresDB app;

  public DeleteElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {
    app.delete(new Queries().getDeleteQuery(), event.pathParam("elementId"))
      .subscribe(r -> event.response().send("Deleted: "+r.rowCount()+" row(s)"));
  }

}
