package net.moznion.donovan;

import static org.junit.Assert.assertEquals;
import me.geso.mech2.Mech2;
import me.geso.mech2.Mech2Result;
import me.geso.mech2.Mech2WithBase;
import me.geso.webscrew.response.ByteArrayResponse;

import org.junit.Test;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class BeforeDispatchFilterTest {
  private String content;

  @Test
  public void shuoldApplyBeforeDispatchTriggerWithoutDirectlyResponding() throws Exception {
    content = "NG";
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/", c -> {
        return c.renderJSON(new BasicAPIResponse(200, this.content));
      });

      dj.registerBeforeDispatchTrigger(c -> {
        this.content = "OK";
        return Optional.empty();
      });

      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/").execute();

      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
      assertEquals("{\"code\":200,\"messages\":[\"OK\"]}", result.getResponseBodyAsString());
    }
  }

  @Test
  public void shuoldApplyBeforeDispatchTriggerWithResponse() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/", c -> {
        return c.renderJSON(new BasicAPIResponse(200, "NG"));
      });

      dj.registerBeforeDispatchTrigger(c -> {
        return Optional.of(new ByteArrayResponse(200,
            "FromTrigger".getBytes(StandardCharsets.UTF_8)));
      });

      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/").execute();

      assertEquals(200, result.getResponse().getStatusLine().getStatusCode());
      assertEquals("FromTrigger", result.getResponseBodyAsString());
    }
  }

  @Test
  public void shuoldApplyBeforeDispatchTriggerWithNull() throws Exception {
    try (DonovanJetty dj = new DonovanJetty()) {
      dj.get("/", c -> {
        return c.renderJSON(new BasicAPIResponse(200, "NG"));
      });

      dj.registerBeforeDispatchTrigger(c -> {
        return null;
      });

      dj.start();

      String url = dj.getUrl();
      Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
      Mech2Result result = mech.get("/").execute();

      assertEquals(500, result.getResponse().getStatusLine().getStatusCode());
    }
  }
}
