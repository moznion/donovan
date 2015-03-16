package net.moznion.donovan;

import me.geso.webscrew.response.WebResponse;

public interface JSONRendererProvider {
  /**
   * Render web response which contains JSON body.
   * 
   * <p>
   * This method sets a status code according to argument and sets content type for
   * "application/json".
   * </p>
   * 
   * @param statusCode Status code of response
   * @param obj Object to build JSON body
   * @return Web response which contains JSON body
   */
  public WebResponse renderJSON(final int statusCode, final Object obj);
}
