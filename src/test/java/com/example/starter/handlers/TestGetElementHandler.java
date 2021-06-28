package com.example.starter.handlers;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import com.example.starter.handler.GetElementHandler;
import io.reactivex.Flowable;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test get element handler")
public class TestGetElementHandler {

  @Mock
  private RoutingContext context;

  @Mock
  private PostgresDB app;

  @Mock
  private HttpServerResponse response;

  @Mock
  private JsonObject jsonObject;

  private GetElementHandler getElementHandler;

  @BeforeEach
  void setUp() {
    getElementHandler = new GetElementHandler(app);
  }

  @Test
  void find_element_ok() {


    doReturn("Atium").when(context).pathParam("elementId");
    mockHttpOkResponse();

    Mockito.when(app.find(anyString(),
      anyString()))
      .thenReturn(Flowable.just(jsonObject));

    getElementHandler.handle(context);

    verify(app)
      .find(new Queries().getGetQuery(), "Atium");
    verify(response).setStatusCode(HTTP_OK);

  }

  @Test
  void find_element_ko() {

    Exception findException = new Exception("Element not found");
    doReturn("AguA").when(context).pathParam("elementId");
    mockHttpBadResponse();

    Mockito.when(app.find(anyString(),
      anyString()))
      .thenReturn(Flowable.error(findException));

    getElementHandler.handle(context);

    verify(app).find(new Queries().getGetQuery(), "AguA");
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
