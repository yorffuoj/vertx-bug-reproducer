/**
 *-------------------------------------------------------------------------
 * Copyright (C) 2017 THALES ALENIA SPACE FRANCE. All rights reserved
 *-------------------------------------------------------------------------
 */
package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

/**
 * The Class ServicesWebSocketHandler.
 */
public class IncomingWebsocketServiceCallHandler implements Handler<Buffer>
{
  /** Class Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(IncomingWebsocketServiceCallHandler.class);

  /** The sock JS socket. */
  private final SockJSSocket sockJSSocket;

  /** The vertx. */
  private final Vertx vertx;

  /**
   * Instantiates a new services web socket handler.
   *
   * @param vertx
   *          the vertx
   * @param sockJSSocket
   *          the sock JS socket
   */
  public IncomingWebsocketServiceCallHandler(Vertx vertx, final SockJSSocket sockJSSocket)
  {
    this.sockJSSocket = sockJSSocket;
    this.vertx = vertx;
  }

  @Override
  public void handle(Buffer buffer)
  {
    vertx.executeBlocking(h -> {
      // DO NOTHING
      h.complete();
    }, false, r -> {
      // Nothing to do
    });
  }
}
