package com.example.starter;

import io.vertx.reactivex.core.Vertx;


public class Main {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    MainVerticle vert = new MainVerticle();

    vertx.rxDeployVerticle(vert)
      .subscribe(id -> System.out.println("Verticle "+ id + " deployed"));
  }
}
