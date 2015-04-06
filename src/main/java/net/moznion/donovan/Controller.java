package net.moznion.donovan;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.geso.webscrew.response.WebResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for donovan.
 * 
 * @author moznion
 *
 */
public class Controller implements TextRenderer, JSONErrorRenderer, JacksonJSONRenderer, Redirector {
  private final HttpServletRequest servletRequest;
  private final HttpServletResponse servletResponse;
  private final Map<String, String> pathParams;

  private final Optional<Consumer<WebResponse>> maybeResponseFilter;
  private final Optional<ThrowableFunction<Controller, Optional<WebResponse>>> maybeBeforeDispatchTrigger;

  @Getter
  @Accessors(fluent = true)
  static class ControllerBuilder {
    private final HttpServletRequest servletRequest;
    private final HttpServletResponse servletResponse;
    @Setter
    private Map<String, String> captured;
    @Setter
    private Optional<Consumer<WebResponse>> maybeResponseFilter;
    @Setter
    private Optional<ThrowableFunction<Controller, Optional<WebResponse>>> maybeBeforeDispatchTrigger;

    public ControllerBuilder(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
      this.servletRequest = servletRequest;
      this.servletResponse = servletResponse;
      this.captured = Collections.<String, String>emptyMap();
      this.maybeResponseFilter = Optional.empty();
      this.maybeBeforeDispatchTrigger = Optional.empty();
    }

    public Controller build() {
      return new Controller(this);
    }
  }

  static ControllerBuilder makeControllerBuilder(HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {
    return new ControllerBuilder(servletRequest, servletResponse);
  }

  private Controller(ControllerBuilder cb) {
    this.servletRequest = cb.servletRequest;
    this.servletResponse = cb.servletResponse;
    this.setDefaultCharacterEncoding();

    this.pathParams = Collections.unmodifiableMap(cb.captured);
    this.maybeResponseFilter = cb.maybeResponseFilter;
    this.maybeBeforeDispatchTrigger = cb.maybeBeforeDispatchTrigger;
  }

  private void setDefaultCharacterEncoding() {
    servletResponse.setCharacterEncoding("UTF-8");
  }

  void invoke(final ThrowableFunction<Controller, WebResponse> action) {
    try {
      final WebResponse res = respond(action);
      maybeResponseFilter.map(responseFilter -> {
        responseFilter.accept(res);
        return null;
      });
      res.write(servletResponse);
    } catch (Throwable e) {
      final WebResponse res = this.handleException(e);

      try {
        res.write(servletResponse);
      } catch (final IOException ioe) {
        Logger.logException(ioe);
        throw new RuntimeException(ioe);
      }
    }
  }

  private WebResponse respond(final ThrowableFunction<Controller, WebResponse> action)
      throws Throwable {
    if (maybeBeforeDispatchTrigger.isPresent()) {
      Optional<WebResponse> maybeResponse = maybeBeforeDispatchTrigger.get().apply(this);
      if (maybeResponse == null) {
        throw new RuntimeException("Response of before dispatch trigger must not be null.");
      }
      if (maybeResponse.isPresent()) {
        return maybeResponse.get();
      }
    }

    return action.apply(this);
  }

  private WebResponse handleException(Throwable e) {
    Logger.logException(e);
    return this.renderError(500, "Internal Server Error");
  }

  /**
   * Get servlet request.
   * 
   * @return Servlet request
   */
  public HttpServletRequest getServletRequest() {
    return servletRequest;
  }

  /**
   * Get servlet response.
   * 
   * @return Servlet response
   */
  public HttpServletResponse getServletResponse() {
    return servletResponse;
  }

  /**
   * Get path parameters.
   * 
   * @return Path parameters
   */
  public Map<String, String> getPathParams() {
    return pathParams;
  }
}
