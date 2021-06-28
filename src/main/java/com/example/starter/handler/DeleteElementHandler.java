package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.core.Handler;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class DeleteElementHandler implements Handler<RoutingContext> {

  private final PostgresDB app;

  public DeleteElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {
    app.delete(new Queries().getDeleteQuery(), event.pathParam("elementId"))
      .subscribe(
        () ->
            event.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(HTTP_OK)
              .end("Row deleted")
        ,
        onError -> event.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end("Error, something went wrong")
      );
  }

}
