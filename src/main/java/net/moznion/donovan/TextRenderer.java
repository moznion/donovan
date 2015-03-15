package net.moznion.donovan;

import lombok.NonNull;

import me.geso.webscrew.response.ByteArrayResponse;
import me.geso.webscrew.response.WebResponse;

import java.nio.charset.StandardCharsets;

public interface TextRenderer {
  public default WebResponse renderText(@NonNull final String text) {
    final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

    final ByteArrayResponse res = new ByteArrayResponse(200, bytes);
    res.setContentType("text/plain; charset=utf-8");
    return res;
  }
}
