package net.moznion.donovan;

import lombok.NonNull;

import me.geso.webscrew.response.RedirectResponse;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Map;

public interface Redirector {
  public default RedirectResponse redirect(@NonNull final String location) {
    return new RedirectResponse(location);
  }

  public default RedirectResponse redirect(@NonNull final String location,
      @NonNull Map<String, String> parameters) throws URISyntaxException {
    final URIBuilder uriBuilder = new URIBuilder(location);
    parameters.entrySet().stream().forEach(e -> uriBuilder.setParameter(e.getKey(), e.getValue()));
    return new RedirectResponse(uriBuilder.build().toString());
  }
}
