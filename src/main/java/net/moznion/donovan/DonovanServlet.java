package net.moznion.donovan;

import me.geso.webscrew.response.WebResponse;

abstract class DonovanServlet implements AutoCloseable {
  protected final Dispatcher dispatcher;

  public DonovanServlet() {
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
