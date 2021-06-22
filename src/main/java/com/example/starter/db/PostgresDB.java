package com.example.starter.db;

import java.sql.*;
import java.util.ArrayList;


import io.reactivex.Completable;
import io.reactivex.Flowable;
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

public class PostgresDB {


  private final String url = "jdbc:postgresql://localhost:5432/postgres";
  private final String user = "catamm";
  private final String password = "password";
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

  /*public Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url, user, password);
      System.out.println("Connected to the PostgreSQL server successfully.");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }*/

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
    Queries query = new Queries();

    return client
      .preparedQuery(query.getInsertQuery())
      .rxExecute(Tuple.from(list))
      .doOnSuccess(result -> System.out.println("Tuple added to de table"))
      .doOnError(result -> System.out.println("Error adding tuple to table"))
      .ignoreElement();
  }

  public Single<RowSet<Row>>  insert(JsonObject json){

    Queries query = new Queries();
    return client
      .preparedQuery(query.getInsertQuery())
      .rxExecute(Tuple.of(json.getString("name"), json.getString("category"), json.getInteger("number"), json.getInteger("period"), json.getString("summary"), json.getString("symbol")))
      .doOnSuccess(result -> System.out.println("Tuple inserted to the table"))
      .doOnError(result -> System.out.println("Error inserted tuple to the table"));

  }

  public Flowable<JsonObject> find(String query, String id){
    return client
      .preparedQuery(query)
      .rxExecute(Tuple.of(id))
      .doOnError(result -> System.out.println("Error while searching for the element"))
      .filter(r -> r.rowCount() != 0)
      .switchIfEmpty(Single.error(new Exception("No matches were found")))
      .doOnSuccess(result -> System.out.println("Element found in the table, retrieved "+result.size()+" rows"))
      .flattenAsFlowable(r -> r)
      .map(Row::toJson);
  }

    public Completable delete(String query, String id) {
    return client
      .preparedQuery(query)
      .rxExecute(Tuple.of(id))
      .doOnError(result -> System.out.println("Error deleting rows from the table"))
      .filter(r -> r.rowCount() != 0)
      .switchIfEmpty(Single.error(new Exception("Element not found")))
      .doOnSuccess(result -> System.out.println("Element deleted from the table"))
      .ignoreElement();
  }

  public Completable update(String query, String id, String value) {
    return client
      .preparedQuery(query)
      .rxExecute(Tuple.of(value, id))
      .doOnError(error -> System.out.println("Error updating promotion"))
      .filter(r -> r.rowCount() != 0)
      .switchIfEmpty(Single.error(new Exception("No matches were found")))
      .doOnSuccess(result -> System.out.println("Element updated"))
      .ignoreElement();
  }

  /**
   * @param args the command line arguments
   */
//  public static void main(String[] args) {
//
//    Vertx vertx = Vertx.vertx();
//    PostgresDB app = new PostgresDB();
//    vertx.fileSystem().rxReadFile("/Users/jorge.moyanomasmovil.com/Documents/projects/cataMM//src/main/java/com/example/starter/db/elements.json")
//      .map(Buffer::toJsonObject)
//      .subscribe(app::populateDB);
//    JsonObject jsonObject = new JsonObject();
//    jsonObject.put("name", "Atium")
//      .put("category", "noble metal")
//      .put("number", 16)
//      .put("period", 16)
//      .put("summary", "Atium is a God Metal. An Allomancer burning atium is able to see into the future by a few seconds. Feruchemists can use atium to store youthfulness, and when used as a Hemalurgic spike, atium steals any property or power. The metal is a reflective, silvery color, so much so that beads of atium almost appear to be drops of liquid.")
//      .put("symbol", "At");
//    app.insert(jsonObject);
//    Queries query = new Queries();
//    //app.find(query.getGetQuery(), "Atium").subscribe();
//    app.update(query.getModifyQuery(), "Atm", "Atium").subscribe();
//    //app.delete(query.getDeleteQuery(), "Atium").subscribe();
//
//  }
}
