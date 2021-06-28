package com.example.starter.handler;

import com.example.starter.db.PostgresDB;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_OK;

public class PopulateDBHandler implements Handler<RoutingContext> {

  private final PostgresDB app;

  public PopulateDBHandler(PostgresDB app) { this.app = app; }

  @Override
  public void handle (RoutingContext event) {

    Vertx.currentContext().owner().fileSystem().rxReadFile("/Users/jorge.moyanomasmovil.com/Documents/projects/cataMM//src/main/java/com/example/starter/db/elements.json")
      .map(Buffer::toJsonObject).subscribe(json -> {
      app.populateDB(json);
      event.response()
        .putHeader("content-type", "application/body")
        .setStatusCode(HTTP_OK)
        .end("Database populated successfully");
    }
    );
  }

}
