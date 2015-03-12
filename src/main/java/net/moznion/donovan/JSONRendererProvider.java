package net.moznion.donovan;

import me.geso.webscrew.response.WebResponse;

public interface JSONRendererProvider {
  public WebResponse renderJSON(final int statusCode, final Object obj);
}
