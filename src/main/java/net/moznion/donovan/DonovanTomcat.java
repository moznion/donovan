package net.moznion.donovan;

import lombok.Getter;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DonovanTomcat extends DonovanServletContainer {
  @Getter
  private Tomcat tomcat;

  public DonovanTomcat() {
    super();
  }

  @Override
  public void start() throws Exception {
    tomcat = new Tomcat();
    tomcat.setPort(0);

    Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());
    Tomcat.addServlet(ctx, "donovanTomcatHandler", new DonovanTomcatHandler());
    ctx.addServletMapping("/*", "donovanTomcatHandler");
    ctx.setIgnoreAnnotations(true);

    tomcat.start();

    int port = tomcat.getConnector().getLocalPort();
    url = "http://127.0.0.1:" + port;
  }

  @Override
  public void stop() throws Exception {
    tomcat.stop();
    tomcat.destroy();
  }

  public void await() {
    tomcat.getServer().await();
  }

  private class DonovanTomcatHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DonovanTomcatHandler() {
      super();
    }

    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
      dispatcher.dispatch(servletRequest, servletResponse);
    }
  }
}
