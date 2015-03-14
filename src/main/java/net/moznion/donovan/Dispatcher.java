package net.moznion.donovan;

import lombok.Getter;

import me.geso.routes.RoutingResult;
import me.geso.routes.WebRouter;
import me.geso.webscrew.response.WebResponse;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
public class Dispatcher implements ErrorPageWriter {
  private final WebRouter<ThrowableFunction<Controller, WebResponse>> router;

  public Dispatcher() {
    router = new WebRouter<>();
  }

  public void dispatch(final HttpServletRequest servletRequest,
      final HttpServletResponse servletResponse,
      final Optional<Consumer<WebResponse>> maybeResponseFilter) {
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
    Controller controller = new Controller(servletRequest, servletResponse, captured, maybeResponseFilter);
    controller.invoke(action);
  }
}
