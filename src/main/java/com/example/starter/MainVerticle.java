package com.example.starter;

import com.example.starter.db.PostgresDB;
import com.example.starter.router.VertxRouter;
import io.reactivex.Completable;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.core.Promise;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

  /*@Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }*/

  @Override
  public Completable rxStart() {

    VertxRouter router = new VertxRouter(vertx);
    PostgresDB postgres = new PostgresDB();
    router.configureRouter(postgres);

    return vertx.createHttpServer()
      .requestHandler(router.getRouter())
      .rxListen(8888,"localhost")
      .doOnSuccess(response -> System.out.println("HTTP server started on port " + response.actualPort()))
      .ignoreElement();
  }
}
