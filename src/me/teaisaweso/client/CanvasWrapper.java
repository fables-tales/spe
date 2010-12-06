package me.teaisaweso.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dev.ui.UiEvent.Type;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class CanvasWrapper extends GWTCanvas implements MouseDownHandler, MouseUpHandler,
        MouseMoveHandler, HasMouseDownHandlers, HasMouseMoveHandlers, HasMouseUpHandlers {

    public CanvasWrapper() {
        super();
        sinkEvents(Event.MOUSEEVENTS);
        this.createHandlerManager();
    }

    public CanvasWrapper(int coordX, int coordY, int pixelX, int pixelY) {
        super(coordX, coordY, pixelX, pixelY);
        sinkEvents(Event.MOUSEEVENTS);
    }

    public CanvasWrapper(int coordX, int coordY) {
        super(coordX, coordY);
        sinkEvents(Event.MOUSEEVENTS);
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addHandler(handler, MouseUpEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addHandler(handler, MouseMoveEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        Window.alert("death");
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        Window.alert("death");
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        Window.alert("death");
    }

}
