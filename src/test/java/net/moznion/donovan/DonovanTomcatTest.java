package net.moznion.donovan;

import static org.junit.Assert.assertEquals;
import me.geso.mech2.Mech2;
import me.geso.mech2.Mech2Result;
import me.geso.mech2.Mech2WithBase;

import org.junit.Test;

import java.net.URI;

public class DonovanTomcatTest {
  @Test
  public void shuoldReceive200ByGet() throws Exception {
    try (DonovanTomcat dt = new DonovanTomcat()) {
      dt.get("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse());
      });
      dt.start();

      String url = dt.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/").execute();
      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
    }
  }

  @Test
  public void shuoldReceive200ByPost() throws Exception {
    try (DonovanTomcat dt = new DonovanTomcat()) {
      dt.post("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse());
      });
      dt.start();

      String url = dt.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.post("/").execute();
      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
    }
  }

  @Test
  public void shuoldSuccessAndEnableToGetPathParams() throws Exception {
    try (DonovanTomcat dt = new DonovanTomcat()) {
      dt.get("/{foo}", (c) -> {
        String fooParam = c.getPathParams().get("foo");
        return c.renderJSON(new BasicAPIResponse(200, fooParam));
      });
      dt.start();

      String url = dt.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/bar").execute();
      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
      assertEquals("{\"code\":200,\"messages\":[\"bar\"]}", result.getResponseBodyAsString());
    }
  }

  @Test
  public void shouldBeNotFound() throws Exception {
    try (DonovanTomcat dt = new DonovanTomcat()) {
      dt.start();

      String url = dt.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/foo").execute();

      assertEquals(404, result.getResponse().getStatusLine().getStatusCode());
      assertEquals(
          "<!doctype html><html><div style='font-size: 400%'>404 Not Found</div></html>",
          result.getResponseBodyAsString());
    }
  }

  @Test
  public void shouldBeMethodNotAllowed() throws Exception {
    try (DonovanTomcat dt = new DonovanTomcat()) {
      dt.get("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse());
      });
      dt.start();

      String url = dt.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new
          URI(url));
      Mech2Result result = mech.post("/").execute();
      assertEquals(405, result.getResponse().getStatusLine().getStatusCode());
      assertEquals(
          "<!doctype html><html><div style='font-size: 400%'>405 Method Not Allowed</div></html>",
          result.getResponseBodyAsString());
    }
  }
}
