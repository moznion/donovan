package net.moznion.donovan;

import javax.servlet.http.HttpServletResponse;

public interface ErrorPageWriter {
  public default void writeNotFoundErrorPage(final HttpServletResponse servletResponse) {
    servletResponse.setCharacterEncoding("UTF-8");
    servletResponse.setStatus(404);
    servletResponse.setContentType("text/html; charset=utf-8");
    try {
      servletResponse.getWriter()
          .write("<!doctype html><html><div style='font-size: 400%'>404 Not Found</div></html>");
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public default void writeMethodNotAllowedErrorPage(final HttpServletResponse servletResponse) {
    servletResponse.setCharacterEncoding("UTF-8");
    servletResponse.setStatus(405);
    servletResponse.setContentType("text/html; charset=utf-8");
    try {
      servletResponse
          .getWriter()
          .write(
              "<!doctype html><html><div style='font-size: 400%'>405 Method Not Allowed</div></html>");
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

}
