package net.moznion.donovan;

import me.geso.webscrew.response.WebResponse;

public interface ErrorPageRenderer {
  public WebResponse renderError(final int code, final String message);
}
