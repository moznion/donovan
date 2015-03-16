package net.moznion.donovan;

import me.geso.webscrew.response.WebResponse;

public interface ErrorRenderer {
  /**
   * Render error web response with message.
   * 
   * @param code Status code of response
   * @param message Error message
   * @return Error web response
   */
  public WebResponse renderError(final int code, final String message);
}
