package com.example.starter.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlClient;
import io.vertx.reactivex.sqlclient.Tuple;
import io.vertx.sqlclient.PoolOptions;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PostgresDB {


  private final String url = "jdbc:postgresql://localhost/postgres";
  private final String user = "catamm";
  private final String password = "password";
  private final String insertQuery = "INSERT INTO periodicTable (id, category, numbr, perd, summary, symbol) VALUES ($1, $2, $3, $4, $5, $6)";

  private final SqlClient client;

  public PostgresDB() {
    this.client = configurePostgresDB();
  }

  private SqlClient configurePostgresDB() {

    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("localhost")
      .setDatabase("postgres")
      .setUser("catamm")
      .setPassword("password");

    // Pool Options
    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

    // Create the pool from the data object
    PgPool pool = PgPool.pool(Vertx.vertx(), connectOptions, poolOptions);

    pool.getConnection(ar -> {
      System.out.println("Postgres client connected");
    });

    return pool;
  }


  /**
   * Connect to the PostgreSQL database
   *
   * @return a Connection object
   */

  public Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url, user, password);
      System.out.println("Connected to the PostgreSQL server successfully.");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  public void populateDB(JsonObject jsonObject) {

    jsonObject.getJsonArray("elements")
      .stream().forEach(item -> insertFile((JsonObject) item));


  }

  private Object insertFile(JsonObject item) {
    ArrayList list = new ArrayList();
    list.add(item.getString("name"));
    list.add(item.getString("category"));
    list.add(item.getInteger("number"));
    list.add(item.getInteger("period"));
    list.add(item.getString("summary"));
    list.add(item.getString("symbol"));

    return client
      .preparedQuery(insertQuery)
      .rxExecute(Tuple.from(list))
      .doOnSuccess(r -> System.out.println("Tuple added to de table"))
      .doOnError(r -> System.out.println("Error adding tuple to table"))
      .ignoreElement();
  }

  public Single<RowSet<Row>> insert(JsonObject json){
    String id, category, summary, symbol, query;
    Long number, period;
    int numbr, perd;

    id = json.getString("name");

    category = json.getString("category");

    number = json.getLong("number");
    numbr = number.intValue();

    period = json.getLong("period");
    perd = period.intValue();

    summary = json.getString("summary");

    symbol = json.getString("symbol");
    return client
      .preparedQuery(insertQuery)
      .rxExecute(Tuple.of(id, category, numbr, perd, summary, symbol))
      .doOnSuccess(r -> System.out.println("Tuple added to de table"))
      .doOnError(r -> System.out.println("Error adding tuple to table"));

  }

  /*public Maybe<JsonObject> find(JsonObject json){
    return client.rxFindOne("cars", json, new JsonObject());
  }

  public Maybe remove(JsonObject json){
    return client.rxRemoveDocument("cars", json);
  }

  public Maybe<JsonObject> modify(JsonObject original, JsonObject actual){
    return client.rxFindOneAndReplace("cars", original, actual);
  }*/

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {


    Vertx vertx = Vertx.vertx();
    PostgresDB app = new PostgresDB();
    vertx.fileSystem().rxReadFile("/Users/jorge.moyanomasmovil.com/Documents/projects/cataMM/starter/src/main/java/com/example/starter/db/elements.json")
      .map(Buffer::toJsonObject)
      .subscribe(app::populateDB);



    //app.connect();

    //app.populateDB(jsonObject);
  }
}
