package me.teaisaweso.client;

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
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class CanvasWrapper extends GWTCanvas implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, HasMouseDownHandlers, HasMouseMoveHandlers, HasMouseUpHandlers {

    public CanvasWrapper() {
        super();
        sinkEvents(Event.MOUSEEVENTS);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        // TODO Auto-generated method stub
        
    }
    
    
}
