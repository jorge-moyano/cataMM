package com.example.starter.router;

import com.example.starter.db.PostgresDB;
import com.example.starter.handler.AddElementHandler;
import com.example.starter.handler.AppValidationHandler;
import com.example.starter.handler.ElementHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class VertxRouter {

  private Router router;

  public VertxRouter(Vertx vertx){
    // Initialize variables
    this.router = Router.router(vertx);

    // Create BodyHandle to get the body in POSTs
    this.router.route().handler(BodyHandler.create());
  }

  public Router getRouter() {
    return router;
  }

  public void configureRouter(PostgresDB app){
    this.router.get("/periodicTable/:elementId")
      .handler(new AppValidationHandler(app))
      .handler(new ElementHandler(app));
    this.router.post("/addElement")
      //.handler(new AppValidationHandler(app))
      .handler(new AddElementHandler(app));
  }

  public void setRouter (Router router) {
    this.router = router;
  }
}