package net.moznion.donovan;

import lombok.Getter;

import me.geso.webscrew.response.WebResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller implements JSONErrorPageRenderer, JacksonJSONRenderer {
  @Getter
  private HttpServletRequest servletRequest;
  @Getter
  private Map<String, String> pathParams;

  private HttpServletResponse servletResponse;

  public Controller(final HttpServletRequest servletRequest,
      final HttpServletResponse servletResponse,
      final Map<String, String> captured) {
    this.servletRequest = servletRequest;
    this.servletResponse = servletResponse;
    this.setDefaultCharacterEncoding();

    this.pathParams = Collections.unmodifiableMap(captured);
  }

  private void setDefaultCharacterEncoding() {
    servletResponse.setCharacterEncoding("UTF-8");
  }

  public void invoke(final ThrowableFunction<Controller, WebResponse> action) {
    try {
      final WebResponse res = action.throwableApply(this);
      res.write(servletResponse);
    } catch (Throwable e) {
      final WebResponse response = this.handleException(e);
      try {
        response.write(servletResponse);
      } catch (final IOException ioe) {
        // this.logException(e);
        throw new RuntimeException(ioe);
      }
    }
  }

  public WebResponse handleException(Throwable e) {
    // this.logException(e);
    return this.renderError(500, "Internal Server Error");
  }
}
