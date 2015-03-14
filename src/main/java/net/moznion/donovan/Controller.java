package net.moznion.donovan;

import lombok.Getter;

import me.geso.webscrew.response.WebResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller implements JSONErrorPageRenderer, JacksonJSONRenderer {
  @Getter
  private HttpServletRequest servletRequest;
  @Getter
  private Map<String, String> pathParams;
  @Getter
  private HttpServletResponse servletResponse;

  private Optional<Consumer<WebResponse>> maybeResponseFilter;
  private Optional<ThrowableFunction<Controller, Optional<WebResponse>>> maybeBeforeDispatchTrigger;

  public Controller(
      final HttpServletRequest servletRequest,
      final HttpServletResponse servletResponse,
      final Map<String, String> captured,
      final Optional<Consumer<WebResponse>> maybeResponseFilter,
      final Optional<ThrowableFunction<Controller, Optional<WebResponse>>> maybeBeforeDispatchTrigger) {
    this.servletRequest = servletRequest;
    this.servletResponse = servletResponse;
    this.setDefaultCharacterEncoding();

    this.pathParams = Collections.unmodifiableMap(captured);
    this.maybeResponseFilter = maybeResponseFilter;
    this.maybeBeforeDispatchTrigger = maybeBeforeDispatchTrigger;
  }

  private void setDefaultCharacterEncoding() {
    servletResponse.setCharacterEncoding("UTF-8");
  }

  public void invoke(final ThrowableFunction<Controller, WebResponse> action) {
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
        // this.logException(e);
        throw new RuntimeException(ioe);
      }
    }
  }

  private WebResponse respond(final ThrowableFunction<Controller, WebResponse> action)
      throws Throwable {
    if (maybeBeforeDispatchTrigger.isPresent()) {
      Optional<WebResponse> maybeResponse = maybeBeforeDispatchTrigger.get().throwableApply(this);
      if (maybeResponse == null) {
        throw new RuntimeException("Response of before dispatch trigger must not be null.");
      }
      if (maybeResponse.isPresent()) {
        return maybeResponse.get();
      }
    }

    return action.throwableApply(this);
  }

  public WebResponse handleException(Throwable e) {
    // this.logException(e);
    return this.renderError(500, "Internal Server Error");
  }
}
