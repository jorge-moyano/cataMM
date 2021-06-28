package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class AddElementHandler implements Handler<RoutingContext>{

  private final PostgresDB app;

  public AddElementHandler(PostgresDB app) {
    this.app = app;
  }

  @Override
  public void handle(RoutingContext event) {

    JsonObject newElement = event.getBodyAsJson();

    app.insert(new Queries().getInsertQuery(), newElement)
      .subscribe(
        () ->
            event.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(HTTP_OK)
              .end("Element inserted"),
        onError -> event.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end("Error, something went wrong")
      );
  }
}
