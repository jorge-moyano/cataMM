package com.example.starter.handlers;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import com.example.starter.handler.DeleteElementHandler;
import io.reactivex.Completable;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test delete element handler")
public class TestDeleteElementHandler {

  @Mock
  private RoutingContext context;

  @Mock
  private PostgresDB app;

  @Mock
  private HttpServerResponse response;

  @Mock
  private DeleteElementHandler deleteElementHandler;

  @BeforeEach
  void setUp() {
    deleteElementHandler = new DeleteElementHandler(app);
  }

  @Test
  void delete_element_ok() {

    doReturn("Agua").when(context).pathParam("elementId");
    mockHttpOkResponse();

    Mockito.when(app.delete(anyString(),
      anyString()))
      .thenReturn(Completable.complete());

    deleteElementHandler.handle(context);

    verify(app)
      .delete(new Queries().getDeleteQuery(), "Agua");
    verify(response).setStatusCode(HTTP_OK);

  }

  @Test
  void delete_element_ko() {

    Exception deleteException = new Exception("Element not found");
    doReturn("AguA").when(context).pathParam("elementId");
    mockHttpBadResponse();

    Mockito.when(app.delete(anyString(),
      anyString()))
      .thenReturn(Completable.error(deleteException));

    deleteElementHandler.handle(context);

    verify(app).delete(new Queries().getDeleteQuery(), "AguA");
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
