package net.moznion.donovan;

import lombok.Getter;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Donovan servlet which is based on Jetty.
 * 
 * @author moznion
 *
 */
public class DonovanJetty extends DonovanServletContainer {
  @Getter
  private Server jetty;

  public DonovanJetty() {
    super();
  }

  @Override
  public void start() throws Exception {
    jetty = new Server(0);
    jetty.setStopAtShutdown(true);
    jetty.setHandler(new DonovanJettyHandler());

    jetty.start();

    ServerConnector connector = (ServerConnector) jetty.getConnectors()[0];
    int port = connector.getLocalPort();
    url = "http://127.0.0.1:" + port;
  }

  @Override
  public void stop() throws Exception {
    jetty.stop();
  }

  /**
   * Make server persistent.
   * 
   * @throws InterruptedException Occur when failed to make server persistent
   */
  public void join() throws InterruptedException {
    jetty.join();
  }

  private class DonovanJettyHandler extends AbstractHandler {
    public DonovanJettyHandler() {
      super();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest servletRequest,
        HttpServletResponse servletResponse) throws IOException, ServletException {
      baseRequest.setHandled(true);
      dispatcher.dispatch(servletRequest, servletResponse, maybeResponseFilter,
          maybeBeforeDispatchTrigger);
    }
  }
}
