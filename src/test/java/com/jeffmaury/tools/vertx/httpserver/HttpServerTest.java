/**
 * 
 */
package com.jeffmaury.tools.vertx.httpserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jeffmaury.tools.vertx.vertxserver.HttpServerConfigurationBuilder;
import com.jeffmaury.tools.vertx.vertxserver.VertxHttpServer;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

/**
 * @author Jeff MAURY
 *
 */
@RunWith(Parameterized.class)
public class HttpServerTest {

  private static VertxHttpServer server;
  
  private static AsyncHttpClient client;
  
  private String connection;
  
  @Parameters
  public static Collection<Object[]> getParameters() {
    Object[][] parameters = {{"close"}, {"keep-alive"}};
    return Arrays.asList(parameters);
  }
  
  public HttpServerTest(String connection) {
    this.connection = connection;
  }
  
  @BeforeClass
  public static void setUp() throws IOException, IllegalArgumentException, InterruptedException, ExecutionException {
    server = new VertxHttpServer(new HttpServerConfigurationBuilder().baseDir("target/www").build());
    server.start();
    client = new AsyncHttpClient();
    client.preparePut("http://localhost:8080/index.html").setBody(HttpServerTest.class.getResourceAsStream("/index.html")).execute().get();
  }
  
  @AfterClass
  public static void shutdown() {
    server.stop();
  }
  
  @Test
  public void checkSimpleConnection() throws UnknownHostException, IOException {
    Socket s = new Socket("localhost", 8080);
    Assert.assertTrue(s.isConnected());
    s.close();
  }
  
  @Test
  public void checkNonExistantRequest() throws InterruptedException, ExecutionException, IOException {
    Response response = client.prepareGet("http://localhost:8080/non-existant-resource").addHeader("Connection", connection).execute().get();
    Assert.assertNotNull(response);
    Assert.assertEquals(404, response.getStatusCode());
  }

  @Test
  public void checkPostIsRejected() throws InterruptedException, ExecutionException, IOException {
    Response response = client.preparePost("http://localhost:8080/").addParameter("param", "value").addHeader("Connection", connection).execute().get();
    Assert.assertNotNull(response);
    Assert.assertEquals(405, response.getStatusCode());
  }
  
  @Test
  public void checkConnectIsRejected() throws InterruptedException, ExecutionException, IOException {
    Response response = client.prepareConnect("http://localhost:8080/").addHeader("Connection", connection).execute().get();
    Assert.assertNotNull(response);
    Assert.assertEquals(405, response.getStatusCode());
  }

  @Test
  public void checkOptionstIsRejected() throws InterruptedException, ExecutionException, IOException {
    Response response = client.prepareOptions("http://localhost:8080/").addHeader("Connection", connection).execute().get();
    Assert.assertNotNull(response);
    Assert.assertEquals(405, response.getStatusCode());
  }

  @Test
  public void checkRootRequest() throws InterruptedException, ExecutionException, IOException {
    Response response = client.prepareGet("http://localhost:8080/").addHeader("Connection", connection).execute().get();
    Assert.assertNotNull(response);
  }
  
  @Test
  public void checkRootFile() throws InterruptedException, ExecutionException, IOException {
    Response response = client.prepareGet("http://localhost:8080/index.html").addHeader("Connection", connection).execute().get();
    Assert.assertNotNull(response);
    Assert.assertEquals(200, response.getStatusCode());
    Assert.assertTrue(response.hasResponseBody());
  }
  
  @Test
  public void checkPutInNonExistantDirectory() throws InterruptedException, ExecutionException, IOException {
    Response response = client.preparePut("http://localhost:8080/subdir/index.html").setBody(HttpServerTest.class.getResourceAsStream("/index.html")).addHeader("Connection", connection).execute().get();
    Assert.assertNotNull(response);
    Assert.assertEquals(201, response.getStatusCode());
  }
  
  @Test
  @Ignore
  public void checkSessionClosedAfterTimemout() throws UnknownHostException, IOException, InterruptedException {
    Socket s = new Socket("localhost", 8080);
    s.setSoLinger(false, 0);
    Assert.assertTrue(s.isConnected());
    Thread.sleep(10000);
    assertEquals(-1, s.getInputStream().read());
    s.close();
  }
}
