/**
 * 
 */
package com.jeffmaury.tools.vertx.vertxserver;

import java.io.File;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.streams.Pump;

/**
 * @author Jeff MAURY
 *
 */
public class FileHandler implements Handler<HttpServerRequest> {

  private HttpServerConfiguration configuration;
  private Vertx vertx;

	/**
   * 
   */
  public FileHandler(HttpServerConfiguration configuration, Vertx vertx) {
  	this.configuration = configuration;
  	this.vertx = vertx;
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public void handle(final HttpServerRequest request) {
    final File file = new File(configuration.getBaseDir() + request.path());
    if (request.method().equals("GET")) {
      doGet(request, file);
    } else if (request.method().equals("PUT")) {
      doPut(request, file);
    } else if (request.method().equals("DELETE")) {
      doDelete(request, file);
    } else {
    	request.response().setStatusCode(405).end();
    }
  }

  /**
   * @param request
   * @param file
   */
  protected void doGet(final HttpServerRequest request, final File file) {
    if (file.exists()) {
      if (file.isDirectory()) {
        vertx.fileSystem().readDir(file.getPath(), new Handler<AsyncResult<String[]>>() {
          @Override
          public void handle(AsyncResult<String[]> result) {
            if (result.succeeded()) {
              StringBuilder builder = new StringBuilder("<html><body>");
              for(String path : result.result()) {
                builder.append("<p><a href='");
                builder.append(path);
                builder.append("'>");
                builder.append(path);
                builder.append("</a></p>");
              }
              builder.append("</body></html>");
              request.response().end(builder.toString());
            } else {
              request.response().setStatusCode(500).end();
            }
          }
        });
      } else {
        request.response().putHeader("Content-Length", Long.toString(file.length()));
        vertx.fileSystem().open(file.getPath(), new Handler<AsyncResult<AsyncFile>>() {
          @Override
          public void handle(final AsyncResult<AsyncFile> result) {
            if (result.succeeded()) {
              result.result().endHandler(new Handler<Void>() {
                @Override
                public void handle(Void event) {
                  request.response().end();
                }
              });
              Pump.createPump(result.result(), request.response()).start();
            } else {
              request.response().setStatusCode(500).end();
            }
          }
        });
      }
    } else {
      request.response().setStatusCode(404).end();
    }
  }

  /**
   * @param request
   * @param file
   */
  protected void doDelete(final HttpServerRequest request, final File file) {
    if (file.exists()) {
      vertx.fileSystem().delete(file.getPath(), new Handler<AsyncResult<Void>>() {

        @Override
        public void handle(AsyncResult<Void> result) {
          request.response().setStatusCode(result.succeeded()?200:500).end();
        }
      });
    } else {
      request.response().setStatusCode(404).end();
    }
  }

  /**
   * @param request
   * @param file
   */
  protected void doPut(final HttpServerRequest request, final File file) {
    if (!file.exists() || file.isFile())  {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      /*
       * we shoud pause the request as we are opening the file asynchronously
       */
      request.pause();
      vertx.fileSystem().open(file.getPath(), new Handler<AsyncResult<AsyncFile>>() {
        @Override
        public void handle(final AsyncResult<AsyncFile> result) {
          if (result.succeeded()) {
            request.response().setStatusCode(201).end();
            request.endHandler(new Handler<Void>() {
              @Override
              public void handle(Void event) {
                result.result().close();
              }
            });
            Pump.createPump(request, result.result()).start();
            request.resume();
          } else {
            request.response().setStatusCode(500).end();
            request.resume();
          }
        }
      });
    } else {
      request.response().setStatusCode(405).end();
    }
  }
}
