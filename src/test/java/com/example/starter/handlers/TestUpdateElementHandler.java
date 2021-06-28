package com.example.starter.handlers;

import com.example.starter.db.PostgresDB;
import com.example.starter.db.Queries;
import com.example.starter.handler.UpdateElementHandler;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test update element handler")
class TestUpdateElementHandler {

  @Mock
  private RoutingContext context;

  @Mock
  private PostgresDB app;

  @Mock
  private HttpServerResponse response;

  private UpdateElementHandler updateElementHandler;

  @BeforeEach
  void setUp() {
    updateElementHandler = new UpdateElementHandler(app);
  }

  @Test
  void update_element_ok() {

    doReturn("Agua").when(context).pathParam("elementId");
    doReturn("h2o").when(context).pathParam("newValue");
    mockHttpOkResponse();

    Mockito.when(app.update(anyString(),
      anyString(),
      anyString()))
    .thenReturn(Completable.complete());

    updateElementHandler.handle(context);

    verify(app)
      .update(new Queries().getUpdateQuery(), "Agua", "h2o");
    verify(response).setStatusCode(HTTP_OK);

  }

  @Test
  void update_element_ko() {

    Exception updateException = new Exception("No matches were found");
    doReturn("Agua").when(context).pathParam("elementId");
    doReturn("h2o").when(context).pathParam("newValue");
    mockHttpBadResponse();

    Mockito.when(app.update(anyString(),
      anyString(),
      anyString()))
      .thenReturn(Completable.error(updateException));

    updateElementHandler.handle(context);

    verify(app).update(new Queries().getUpdateQuery(), "Agua", "h2o");
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
