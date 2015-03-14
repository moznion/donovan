package net.moznion.donovan;

import lombok.NonNull;

import me.geso.webscrew.response.WebResponse;

public interface JSONErrorPageRenderer extends ErrorRenderer, JSONRendererProvider {
  @Override
  public default WebResponse renderError(final int code, @NonNull final String message) {
    final BasicAPIResponse apiResponse = new BasicAPIResponse(code, message);
    return this.renderJSON(code, apiResponse);
  }
}
