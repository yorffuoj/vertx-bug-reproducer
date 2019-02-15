package main;

import java.util.concurrent.CountDownLatch;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

public class Main
{
  private static Vertx commonVertx;

  public static void main(String[] args)
  {
    // Activate clustered mode.
    VertxOptions vertxOptions = new VertxOptions();
    // Use Public Address !
    vertxOptions.setClusterHost("127.0.0.1");

    vertxOptions.setClusterPort(200);

    CountDownLatch latch = new CountDownLatch(1);
    
    Vertx.clusteredVertx(vertxOptions, result -> {
      if (result.succeeded())
      {
        commonVertx = result.result();
        
        latch.countDown();
      }
    });
    
    try
    {
      latch.await();
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    connectRouterForConfiguration("/events");
    
  }

  private static void connectRouterForConfiguration(String route)
  {
    Router router = Router.router(commonVertx);

    router.route()
            .handler(CorsHandler.create("*").allowedMethod(HttpMethod.OPTIONS).allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.POST)
                    .allowedMethod(HttpMethod.DELETE).allowedMethod(HttpMethod.HEAD).allowedHeader("Authorization").allowedHeader("www-authenticate")
                    .allowedHeader("Content-Type"));

    SockJSHandlerOptions options = new SockJSHandlerOptions().setHeartbeatInterval(2000);
    SockJSHandler sockJSHandler = SockJSHandler.create(commonVertx, options);
    ServicesAndEventsOnWebSocketHandler handler = new ServicesAndEventsOnWebSocketHandler(commonVertx);
    sockJSHandler.socketHandler(handler);
    router.route(route + "/*").handler(sockJSHandler);

    HttpServerOptions httpServerOptions = new HttpServerOptions().setSsl(false);
    
    HttpServer server = commonVertx.createHttpServer(httpServerOptions).requestHandler(router::accept).listen(8080, "127.0.0.1", result -> {
      if (result.succeeded())
      {
        System.out.println("HTTP SERVER UP");
      }
      else
      {
        System.out.println("HTTP SERVER FAILED");
      }
    });
  }
}
