package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class GetElementHandler implements Handler<RoutingContext> {

  private final PostgresDB app;

  public GetElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {

    app.find(new Queries().getGetQuery(), event.pathParam("elementId"))
      .subscribe(json -> {
        event.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_OK)
          .end(json.encode());
        },
        onError -> event.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end("Error, element not found")
      );
  }
}
