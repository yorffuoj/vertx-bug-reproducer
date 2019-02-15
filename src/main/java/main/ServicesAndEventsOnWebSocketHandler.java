/**
 *-------------------------------------------------------------------------
 * Copyright (C) 2017 THALES ALENIA SPACE FRANCE. All rights reserved
 *-------------------------------------------------------------------------
 */
package main;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

/**
 * The Class ServicesAndEventsOnWebSocketHandler.
 */
public class ServicesAndEventsOnWebSocketHandler implements Handler<SockJSSocket>
{
  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ServicesAndEventsOnWebSocketHandler.class);

  /** The clients. */
  private List<SockJSSocket> clients;

  /** The vertx. */
  private Vertx vertx;
  /**
   * Instantiates a new services and events on web socket handler.
   *
   * @param vertx
   *          the vertx
   * @param accessControl
   *          the access control
   * @param websocketConfiguration
   *          the websocket configuration
   * @param codecs
   *          the codecs
   */
  public ServicesAndEventsOnWebSocketHandler(Vertx vertx)
  {
    clients = new ArrayList<>();
    this.vertx = vertx;
  }

  @Override
  public void handle(SockJSSocket sockJSSocket)
  {
    // for events
    clients.add(sockJSSocket);
    sockJSSocket.endHandler(h -> clients.remove(sockJSSocket));

    // for services
    sockJSSocket.handler(new IncomingWebsocketServiceCallHandler(vertx, sockJSSocket));
  }

  /**
   * Close.
   */
  public void close()
  {
    clients.forEach(SockJSSocket::close);
    clients.clear();
  }

}
