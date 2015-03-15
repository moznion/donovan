package net.moznion.donovan;

import static org.junit.Assert.assertEquals;
import me.geso.mech2.Mech2;
import me.geso.mech2.Mech2Result;
import me.geso.mech2.Mech2WithBase;

import org.junit.Test;

import java.net.URI;

public class DonovanJettyTest {
  @Test
  public void shuoldReceive200ByGet() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse());
      });
      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/").execute();
      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
    }
  }

  @Test
  public void shuoldReceive200ByPost() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.post("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse());
      });
      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.post("/").execute();
      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
    }
  }

  @Test
  public void shuoldSuccessAndEnableToGetPathParams() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/{foo}", (c) -> {
        String fooParam = c.getPathParams().get("foo");
        return c.renderJSON(new BasicAPIResponse(200, fooParam));
      });
      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/bar").execute();
      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
      assertEquals("{\"code\":200,\"messages\":[\"bar\"]}", result.getResponseBodyAsString());
    }
  }

  @Test
  public void shouldBeNotFound() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/foo").execute();

      assertEquals(404, result.getResponse().getStatusLine().getStatusCode());
      assertEquals("{\"code\":404,\"messages\":[\"Not Found\"]}", result.getResponseBodyAsString());
    }
  }

  @Test
  public void shouldBeMethodNotAllowed() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse());
      });
      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new
          URI(url));
      Mech2Result result = mech.post("/").execute();
      assertEquals(405, result.getResponse().getStatusLine().getStatusCode());
      assertEquals("{\"code\":405,\"messages\":[\"Method Not Allowed\"]}",
          result.getResponseBodyAsString());
    }
  }
}
