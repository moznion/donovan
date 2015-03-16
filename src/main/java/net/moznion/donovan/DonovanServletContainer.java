package net.moznion.donovan;

import me.geso.webscrew.response.WebResponse;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Abstract class to provide minimal functions and interfaces of donovan for Servlet containers.
 * 
 * @author moznion
 */
public abstract class DonovanServletContainer implements AutoCloseable {
  protected String url;
  protected Optional<Consumer<WebResponse>> maybeResponseFilter;
  protected Optional<ThrowableFunction<Controller, Optional<WebResponse>>> maybeBeforeDispatchTrigger;
  protected final Dispatcher dispatcher;

  /**
   * Initialize components.
   */
  public DonovanServletContainer() {
    dispatcher = new Dispatcher();
    maybeResponseFilter = Optional.empty();
    maybeBeforeDispatchTrigger = Optional.empty();
  }

  /**
   * Start Servlet web server.
   * 
   * @throws Exception Occur when something wrong to start up the server
   */
  public abstract void start() throws Exception;

  /**
   * Stop Servlet web server.
   * 
   * @throws Exception Occur when something wrong to stop the server
   */
  public abstract void stop() throws Exception;

  /**
   * Register a route with behavior as function for GET/HEAD method.
   * 
   * @param path Path of a route
   * @param action Behavior of a route when accessed
   */
  public void get(String path, ThrowableFunction<Controller, WebResponse> action) {
    dispatcher.getRouter().get(path, action);
  }

  /**
   * Register a route with behavior as function for POST method.
   * 
   * @param path Path of a route
   * @param action Behavior of a route when accessed
   */
  public void post(String path, ThrowableFunction<Controller, WebResponse> action) {
    dispatcher.getRouter().post(path, action);
  }

  /**
   * Register a response filter.
   * 
   * <p>
   * Response filter applies something processing for dispatched response. It means all of responses
   * go through the registered filter.<br>
   * <br>
   * e.g.
   * 
   * <pre>
   * {@code
   * donovanServlet.registerResponseFilter(res -> {
   *   res.addHeader("X-Content-Type-Options", "nosniff");
   *   res.addHeader("X-Frame-Options", "DENY");
   * });
   * }
   * </pre>
   * 
   * @param responseFilter Consumer lambda which is given WebResponse as an argument
   */
  public void registerResponseFilter(Consumer<WebResponse> responseFilter) {
    maybeResponseFilter = Optional.ofNullable(responseFilter);
  }

  /**
   * Register a trigger which fires on before dispatching.
   * 
   * <p>
   * If response of this trigger is {@code Optional.empty()} then dispatching will continue, means
   * request will be passes to specified route.<br>
   * <br>
   * e.g.
   * 
   * <pre>
   * {@code
   * donovanServlet.registerBeforeDispatchTrigger(c -> {
   *   // do something
   *   return Optional.empty(); // pass the request to succeeding route
   * });
   * }
   * </pre>
   * 
   * <p>
   * Other hands, if result of this trigger has entity, it will leave dispatching and returns a
   * response of this trigger.<br>
   * <br>
   * e.g.
   * 
   * <pre>
   * {@code
   * donovanServlet.registerBeforeDispatchTrigger(c -> {
   *   // leave dispatching and use this return value as a response
   *   return Optional.of(new ByteArrayResponse(200, "Trigger Response".getBytes(StandardCharsets.UTF_8))); 
   * });
   * }
   * </pre>
   * 
   * @param beforeDispatchTrigger Function lambda which can throw exception. It receives a
   *        controller instance as argument and returns maybe web response. Return value of this
   *        must not be null.
   */
  public void registerBeforeDispatchTrigger(
      ThrowableFunction<Controller, Optional<WebResponse>> beforeDispatchTrigger) {
    maybeBeforeDispatchTrigger = Optional.ofNullable(beforeDispatchTrigger);
  }

  @Override
  public void close() throws Exception {
    stop();
  }

  /**
   * Return URL of upped server.
   * 
   * @return URL of upped server
   */
  public String getURL() {
    return url;
  }
}
