package net.moznion.donovan;

import lombok.NonNull;
import me.geso.webscrew.response.WebResponse;

public interface JSONErrorRenderer extends ErrorRenderer, JSONRendererProvider {
  @Override
  public default WebResponse renderError(final int code, @NonNull final String message) {
    final BasicAPIResponse apiResponse = new BasicAPIResponse(code, message);
    return this.renderJSON(code, apiResponse);
  }

  /**
   * Render "404 Not Found" error JSON response.
   * 
   * @param message Message to include error response
   * @return 404 Not Found error JSON response
   */
  public default WebResponse render404(@NonNull final String message) {
    final int statusCode = 404;
    final BasicAPIResponse notFoundResponse = new BasicAPIResponse(statusCode, message);
    return this.renderJSON(statusCode, notFoundResponse);
  }

  /**
   * Render "404 Not Found" error JSON response.
   * 
   * <p>
   * This response has a default error message as "Not Found".
   * </p>
   * 
   * @return 404 Not Found error JSON response
   */
  public default WebResponse render404() {
    return render404("Not Found");
  }

  /**
   * Render "405 Method Not Allowed" error JSON response.
   * 
   * @param message Message to include error response
   * @return 405 Method Not Allowed error JSON response
   */
  public default WebResponse render405(@NonNull final String message) {
    final int statusCode = 405;
    final BasicAPIResponse methodNotAllowedResponse = new BasicAPIResponse(statusCode, message);
    return this.renderJSON(statusCode, methodNotAllowedResponse);
  }

  /**
   * Render "405 Method Not Allowed" error JSON response.
   * 
   * <p>
   * This response has a default error message as "Method Not Allowed".
   * </p>
   * 
   * @return 405 Method Not Allowed error JSON response
   */
  public default WebResponse render405() {
    return render405("Method Not Allowed");
  }
}
