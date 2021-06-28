package com.example.starter.router;

import com.example.starter.db.PostgresDB;
import com.example.starter.handler.*;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class VertxRouter {

  private Router router;

  public VertxRouter(Vertx vertx){
    // Initialize variables
    this.router = Router.router( vertx);

    // Create BodyHandle to get the body in POSTs
    this.router.route().handler(BodyHandler.create());
  }

  public Router getRouter() {
    return router;
  }

  public void configureRouter(PostgresDB app){
    this.router.get("/periodicTable/:elementId")
      .handler(new GetElementHandler(app));
    this.router.post("/periodicTable/insert/fromFile/")
      .handler(new PopulateDBHandler(app));
    this.router.post("/periodicTable/insert/")
      .handler(new AddValidationHandler())
      .handler(new AddElementHandler(app));
    this.router.post("/periodicTable/update/:elementId/:newValue")
      .handler(new UpdateElementHandler(app));
    this.router.post("/periodicTable/delete/:elementId")
      .handler(new DeleteElementHandler(app));
  }

  public void setRouter (Router router) {
    this.router = router;
  }
}
