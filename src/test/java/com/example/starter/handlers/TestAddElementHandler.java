package com.example.starter.handlers;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import com.example.starter.handler.AddElementHandler;
import io.reactivex.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test insert element handler")
public class TestAddElementHandler {

  @Mock
  private RoutingContext context;

  @Mock
  private PostgresDB app;

  @Mock
  private HttpServerResponse response;

  @Mock
  private JsonObject jsonObject;

  private AddElementHandler addElementHandler;

  @BeforeEach
  void setUp() {
    addElementHandler = new AddElementHandler(app);
  }

  @Test
  void add_element_ok() {

    doReturn(jsonObject).when(context).getBodyAsJson();
    mockHttpOkResponse();

    Mockito.when(app.insert(anyString(), any()))
      .thenReturn(Completable.complete());

    addElementHandler.handle(context);

    verify(app)
      .insert(new Queries().getInsertQuery(),jsonObject);
    verify(response).setStatusCode(HTTP_OK);

  }

  @Test
  void add_element_ko() {

    Exception insertException = new Exception("Error inserting tuple to the table");
    doReturn(jsonObject).when(context).getBodyAsJson();
    mockHttpBadResponse();

    Mockito.when(app.insert(anyString(), any()))
      .thenReturn(Completable.error(insertException));

    addElementHandler.handle(context);

    verify(app)
      .insert(new Queries().getInsertQuery(), jsonObject);
    verify(response).setStatusCode(HTTP_BAD_REQUEST);

  }

  private void mockHttpBadResponse() {
    doReturn(response).when(response).setStatusCode(HTTP_BAD_REQUEST);
    doReturn(response).when(response).putHeader("content-type", "application/json");
    doReturn(response).when(context).response();
  }

  private void mockHttpOkResponse() {
    doReturn(response).when(response).setStatusCode(HTTP_OK);
    doReturn(response).when(response).putHeader("content-type", "application/json");
    doReturn(response).when(context).response();
  }

}
