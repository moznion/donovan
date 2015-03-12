package net.moznion.donovan;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.geso.webscrew.response.ByteArrayResponse;
import me.geso.webscrew.response.WebResponse;

public interface JacksonJSONRenderer extends JSONRendererProvider {
  @Override
  public default WebResponse renderJSON(final int statusCode, final Object obj) {
    final ObjectMapper mapper = this.createObjectMapper();
    byte[] json;
    try {
      json = mapper.writeValueAsBytes(obj);
    } catch (final JsonProcessingException e) {
      // It caused by programming error.
      throw new RuntimeException(e);
    }

    final ByteArrayResponse res = new ByteArrayResponse(statusCode, json);
    res.setContentType("application/json; charset=utf-8");
    res.setContentLength(json.length);
    return res;
  }

  /**
   * Rendering JSON by jackson.
   *
   * @param obj
   * @return
   */
  public default WebResponse renderJSON(final Object obj) {
    return this.renderJSON(200, obj);
  }

  public default ObjectMapper createObjectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
    return mapper;
  }
}
