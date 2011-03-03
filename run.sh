#!/bin/sh
cd war/WEB-INF/classes/ && java -cp ../../../depends/*:$GWT/gwt-servlet.jar:$GWT/gwt-dev.jar:./ uk.me.graphe.server.ServerMain

