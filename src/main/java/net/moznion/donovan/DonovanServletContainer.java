package net.moznion.donovan;

import lombok.Getter;

import me.geso.webscrew.response.WebResponse;

abstract class DonovanServletContainer implements AutoCloseable {
  @Getter
  protected String url;
  protected final Dispatcher dispatcher;

  public DonovanServletContainer() {
    dispatcher = new Dispatcher();
  }

  abstract public void start() throws Exception;

  abstract public void stop() throws Exception;

  public void get(String path, ThrowableFunction<Controller, WebResponse> action) {
    dispatcher.getRouter().get(path, action);
  }

  public void post(String path, ThrowableFunction<Controller, WebResponse> action) {
    dispatcher.getRouter().post(path, action);
  }

  @Override
  public void close() throws Exception {
    stop();
  }
}
