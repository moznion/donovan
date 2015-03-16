package net.moznion.donovan;

import lombok.Getter;
import me.geso.routes.RoutingResult;
import me.geso.routes.WebRouter;
import me.geso.webscrew.response.WebResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
class Dispatcher implements JSONErrorRenderer, JacksonJSONRenderer {
  private final WebRouter<ThrowableFunction<Controller, WebResponse>> router;

  public Dispatcher() {
    router = new WebRouter<>();
  }

  public void dispatch(
      final HttpServletRequest servletRequest,
      final HttpServletResponse servletResponse,
      final Optional<Consumer<WebResponse>> maybeResponseFilter,
      final Optional<ThrowableFunction<Controller, Optional<WebResponse>>> maybeBeforeDispatchTrigger) {
    final String method = servletRequest.getMethod();
    final String path = servletRequest.getPathInfo();

    final RoutingResult<ThrowableFunction<Controller, WebResponse>> match =
        router.match(method, path);
    if (match == null) {
      try {
        this.render404().write(servletResponse);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return;
    }

    if (!match.methodAllowed()) {
      try {
        this.render405().write(servletResponse);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return;
    }

    final Map<String, String> captured = match.getCaptured();
    final ThrowableFunction<Controller, WebResponse> action = match.getDestination();
    Controller controller = Controller.makeControllerBuilder(servletRequest, servletResponse)
        .captured(captured)
        .maybeResponseFilter(maybeResponseFilter)
        .maybeBeforeDispatchTrigger(maybeBeforeDispatchTrigger)
        .build();
    controller.invoke(action);
  }
}
