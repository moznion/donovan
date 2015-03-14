package net.moznion.donovan;

import static org.junit.Assert.assertEquals;
import me.geso.mech2.Mech2;
import me.geso.mech2.Mech2Result;
import me.geso.mech2.Mech2WithBase;

import org.apache.http.HttpResponse;
import org.junit.Test;

import java.net.URI;

public class ResponseFilterTest {
  @Test
  public void shuoldAddedResponseHeaderRightly() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/", c -> {
        return c.renderJSON(new BasicAPIResponse());
      });

      dj.registerResponseFilter(res -> {
        res.addHeader("X-Content-Type-Options", "nosniff");
        res.addHeader("X-Frame-Options", "DENY");
      });

      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/").execute();

      HttpResponse response = result.getResponse();
      assertEquals(200, response.getStatusLine().getStatusCode());
      assertEquals("nosniff", response.getHeaders("X-Content-Type-Options")[0].getValue());
      assertEquals("DENY", response.getHeaders("X-Frame-Options")[0].getValue());
    }
  }

  @Test
  public void proveHeaderIsEmpty() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/", c -> {
        return c.renderJSON(new BasicAPIResponse());
      });

      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/").execute();

      HttpResponse response = result.getResponse();
      assertEquals(200, response.getStatusLine().getStatusCode());
      assertEquals(0, response.getHeaders("X-Content-Type-Options").length);
      assertEquals(0, response.getHeaders("X-Frame-Options").length);
    }
  }
}
