/**
 * 
 */
package com.jeffmaury.tools.vertx.vertxserver;

/**
 * @author Jeff MAURY
 *
 */
public class HttpServerConfiguration {

  
  private int port;
  
  private int ioThreadsNumber;
  
  private int handlerThreadsNumber;
  
  private String baseDir;
  
  private int timeout;

  public HttpServerConfiguration(int port, int ioThreadsNumber, int handlerThreadsNumber, String baseDir, int timeout) {
    this.port = port;
    this.ioThreadsNumber = ioThreadsNumber;
    this.handlerThreadsNumber = handlerThreadsNumber;
    this.baseDir = baseDir;
    this.timeout = timeout;
  }
  
  /**
   * @return the port
   */
  public int getPort() {
    return this.port;
  }

  /**
   * @return the ioThreadsNumber
   */
  public int getIoThreadsNumber() {
    return this.ioThreadsNumber;
  }

  /**
   * @return the handlerThreadsNumber
   */
  public int getHandlerThreadsNumber() {
    return this.handlerThreadsNumber;
  }

  /**
   * @return the baseDir
   */
  public String getBaseDir() {
    return this.baseDir;
  }

  /**
   * @return the timeout
   */
  public int getTimeout() {
    return this.timeout;
  }
}
