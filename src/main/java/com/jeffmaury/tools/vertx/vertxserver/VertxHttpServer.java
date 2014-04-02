/**
 * 
 */
package com.jeffmaury.tools.vertx.vertxserver;

import java.io.IOException;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.http.HttpServer;

/**
 * @author Jeff MAURY
 *
 */
public class VertxHttpServer {

  private HttpServerConfiguration configuration;
  
  private Vertx vertx;

	/**
   * 
   */
  public VertxHttpServer(HttpServerConfiguration configuration) {
  	this.configuration = configuration;
  }
  
  public void start() throws IOException {
    /*
     * Vertx 2.0 does not allow programmatic access for thread pool sizing.
     * This is ok for standalone use but may cause troubles in a shared multi-threaded
     * environment.
     */
    System.setProperty("vertx.pool.eventloop.size", Integer.toString(configuration.getIoThreadsNumber()));
    System.setProperty("vertx.pool.worker.size", Integer.toString(configuration.getIoThreadsNumber()));
  	vertx = VertxFactory.newVertx();
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(new FileHandler(configuration, vertx));
    server.listen(configuration.getPort());
  }
  
  public void stop() {
  	vertx.stop();
  }
  
  public static void main(String[] args) {
    try  {
      VertxHttpServer server = new VertxHttpServer(new HttpServerConfigurationBuilder().build());
      server.start();
      System.in.read();
      server.stop();
    }
    catch (IOException e) {}
  }
  
}
