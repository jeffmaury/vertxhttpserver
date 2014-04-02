/**
 * 
 */
package com.jeffmaury.tools.vertx.vertxserver;

/**
 * @author Jeff MAURY
 *
 */
public class HttpServerConfigurationBuilder {
  /*
   * The default TCP port used
   */
  public static final int DEFAULT_PORT = 8080;
  
  private int port = DEFAULT_PORT;
  
  /*
   * The default session timeout (s)
   */
  public static final int DEFAULT_TIMEOUT = 5;
      
  private int timeout = DEFAULT_TIMEOUT;
  
  /*
   * Number of threads in the pool. Defaults to the number of processors.
   */
  private int ioThreadsNumber = Runtime.getRuntime().availableProcessors();
  
  /*
   * Number of threads for handler processing. Defaults to the number of processors.
   */
  private int handlerThreadsNumber = Runtime.getRuntime().availableProcessors();

  /*
   * The directory the file are stored in. Defaults to current directory
   */
  private String baseDir = "."; //$NON-NLS-1$

  /**
   * Set the TCP port to use.
   * 
   * @param port the TCP port to use
   * @return the configuration builder
   */
  public HttpServerConfigurationBuilder port(int port) {
    this.port = port;
    return this;
  }
  
  public HttpServerConfigurationBuilder numberOfIoThreads(int numberOfThreads) {
    this.ioThreadsNumber = numberOfThreads;
    return this;
  }
  
  public HttpServerConfigurationBuilder numberOfHandlerThreads(int numberOfThreads) {
    this.handlerThreadsNumber = numberOfThreads;
    return this;
  }

  public HttpServerConfigurationBuilder baseDir(String baseDir) {
    this.baseDir = baseDir;
    return this;
  }
  
  public HttpServerConfigurationBuilder timeout(int timeout) {
    this.timeout = timeout;
    return this;
  }
  
  public HttpServerConfiguration build() {
    return new HttpServerConfiguration(port, ioThreadsNumber, handlerThreadsNumber, baseDir, timeout);
  }
}
