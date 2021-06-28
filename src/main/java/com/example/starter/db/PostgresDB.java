package com.example.starter.db;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlClient;
import io.vertx.reactivex.sqlclient.Tuple;
import io.vertx.sqlclient.PoolOptions;


public class PostgresDB {

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

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
    PgPool pool = PgPool.pool(Vertx.vertx(), connectOptions, poolOptions);

    pool.getConnection(ar -> System.out.println("Postgres client connected"));

    return pool;
  }

  public Completable delete(String query, String id) {

    return exQuery(client, query, id)
      .doOnError(result -> System.out.println("Error deleting rows from the table"))
      .filter(r -> r.rowCount() != 0)
      .switchIfEmpty(Single.error(new Exception("Element not found")))
      .doOnSuccess(result -> System.out.println("Element deleted from the table"))
      .ignoreElement();
      //.doOnComplete(() -> System.out.println("ey"));
  }

  public Flowable<JsonObject> find(String query, String id){

    return exQuery(client, query, id)
      .doOnError(result -> System.out.println("Error while searching for the element"))
      .filter(r -> r.rowCount() != 0)
      .switchIfEmpty(Single.error(new Exception("No matches were found")))
      .doOnSuccess((result -> System.out.println("Element found in the table, retrieved " + result.size() + " rows")))
      .flattenAsFlowable(r -> r)
      .map(Row::toJson);
  }

  /*private Completable insertFile(JsonObject item) {
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
  }*/

  public Completable insert(String query, JsonObject json){

    return exQuery(client, query, json)
      .doOnSuccess(result -> System.out.println("Tuple inserted to the table"))
      .doOnError(result -> System.out.println("Error inserting tuple to the table"))
      .ignoreElement();
  }

//  public Flowable<Object> populateDB(JsonObject jsonObject) {
//
//   return Single.just(jsonToList(jsonObject.getJsonArray("elements")))
//     .flattenAsFlowable(r -> r)
//     .map(element -> insert(element));
//      //.stream().forEach(item -> insert((JsonObject) item).subscribe());
//  }

  public void populateDB(JsonObject jsonObject) {

    jsonObject.getJsonArray("elements")
      .stream().forEach(item -> insert(new Queries().getInsertQuery(),(JsonObject) item).subscribe());
  }

  public Completable update(String query, String id, String value) {

    return exQuery(client, query, id, value)
      .doOnError(error -> System.out.println("Error updating element"))
      .filter(r -> r.rowCount() != 0)
      .switchIfEmpty(Single.error(new Exception("No matches were found")))
      .doOnSuccess(result -> System.out.println("Element updated"))
      .ignoreElement();
  }

  public Single<RowSet<Row>> exQuery(SqlClient client, String query, String id) {

    return client
      .preparedQuery(query)
      .rxExecute(Tuple.of(id));
  }

  public Single<RowSet<Row>> exQuery(SqlClient client, String query, String id, String value) {

    return client
      .preparedQuery(query)
      .rxExecute(Tuple.of(value, id));
  }

  public Single<RowSet<Row>> exQuery(SqlClient client,String query, JsonObject json) {

    return client
      .preparedQuery(query)
      .rxExecute(Tuple.of(json.getString("name"), json.getString("category"), json.getInteger("number"), json.getInteger("period"), json.getString("summary"), json.getString("symbol")));
  }

}
