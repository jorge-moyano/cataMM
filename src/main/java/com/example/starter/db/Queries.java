package com.example.starter.db;

public class Queries {

  private String deleteQuery;
  private String getQuery;
  private String insertQuery;
  private String modifyQuery;

  public Queries() {
    deleteQuery = "DELETE FROM periodicTable WHERE id = ($1);";
    getQuery = "SELECT * FROM periodicTable WHERE id = ($1);";
    insertQuery = "INSERT INTO periodicTable (id, category, numbr, perd, summary, symbol) VALUES ($1, $2, $3, $4, $5, $6);";
    modifyQuery = "UPDATE periodicTable SET symbol = ($1) WHERE id = ($2)";

  }

  public void setDeleteQuery(String deleteQuery) {
    this.deleteQuery = deleteQuery;
  }

  public void setGetQuery(String getQuery) {
    this.getQuery = getQuery;
  }

  public void setInsertQuery(String insertQuery) {
    this.insertQuery = insertQuery;
  }

  public void setModifyQuery(String modifyQuery) {
    this.modifyQuery = modifyQuery;
  }

  public String getDeleteQuery() {
    return deleteQuery;
  }

  public String getGetQuery() {
    return getQuery;
  }

  public String getInsertQuery() {
    return insertQuery;
  }

  public String getModifyQuery() {
    return modifyQuery;
  }

}
