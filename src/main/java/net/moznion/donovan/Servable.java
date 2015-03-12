package net.moznion.donovan;

import me.geso.webscrew.response.WebResponse;

public interface Servable extends AutoCloseable {
  public void start() throws Exception;

  public void stop() throws Exception;

  public void get(String path, ThrowableFunction<Controller, WebResponse> func);

  public void post(String path, ThrowableFunction<Controller, WebResponse> func);
}
