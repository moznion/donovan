package net.moznion.donovan;

import lombok.Getter;

import me.geso.routes.RoutingResult;
import me.geso.routes.WebRouter;
import me.geso.webscrew.response.WebResponse;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
public class Dispatcher {
  private final WebRouter<ThrowableFunction<Controller, WebResponse>> router;

  public Dispatcher() {
    router = new WebRouter<>();
  }

  public void dispatch(final HttpServletRequest servletRequest,
      final HttpServletResponse servletResponse) {
    final String method = servletRequest.getMethod();
    final String path = servletRequest.getPathInfo();

    final RoutingResult<ThrowableFunction<Controller, WebResponse>> match =
        router.match(method, path);
    if (match == null) {
      this.writeNotFoundErrorPage(servletResponse);
      return;
    }

    if (!match.methodAllowed()) {
      this.writeMethodNotAllowedErrorPage(servletResponse);
      return;
    }

    final Map<String, String> captured = match.getCaptured();
    final ThrowableFunction<Controller, WebResponse> action = match.getDestination();
    Controller controller = new Controller(servletRequest, servletResponse, captured);
    controller.invoke(action);
  }

  private void writeMethodNotAllowedErrorPage(final HttpServletResponse servletResponse) {
    servletResponse.setCharacterEncoding("UTF-8");
    servletResponse.setStatus(405);
    servletResponse.setContentType("text/html; charset=utf-8");
    try {
      servletResponse
          .getWriter()
          .write(
              "<!doctype html><html><div style='font-size: 400%'>405 Method Not Allowed</div></html>");
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void writeNotFoundErrorPage(final HttpServletResponse servletResponse) {
    servletResponse.setCharacterEncoding("UTF-8");
    servletResponse.setStatus(404);
    servletResponse.setContentType("text/html; charset=utf-8");
    try {
      servletResponse.getWriter()
          .write("<!doctype html><html><div style='font-size: 400%'>404 Not Found</div></html>");
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
