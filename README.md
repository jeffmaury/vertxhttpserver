vertxhttpserver
===============

A simple file HTTP server based on Vertx. It uses Vertx in embedded mode.

It tries to leverage the asynchronous features of Vertx for file based operations, leaving the IO threads dealing with HTTP
traffic handling.

Connection management is not included as it is already done at the Vert lower level (Netty).

Testing is provided and can be activated through Maven: mvn clean test
