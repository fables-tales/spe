package uk.me.graphe.client;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;

public class CanvasWrapper extends GWTCanvas implements MouseOutHandler, MouseDownHandler, MouseUpHandler,
        MouseMoveHandler, MouseWheelHandler, HasMouseDownHandlers, HasMouseMoveHandlers, HasMouseUpHandlers,
        HasMouseOutHandlers, HasMouseWheelHandlers, DoubleClickHandler, HasDoubleClickHandlers {

    public CanvasWrapper() {
        super(2000, 2000);
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
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addHandler(handler, MouseOutEvent.getType());
    }    

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addHandler(handler, DoubleClickEvent.getType());
	}
    
    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return addHandler(handler, MouseWheelEvent.getType());
    }
    
    @Override
    public void onMouseMove(MouseMoveEvent event) {
        Window.alert("An error has occurred.");
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        Window.alert("An error has occurred.");
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        Window.alert("An error has occurred.");
    }

	@Override
	public void onMouseOut(MouseOutEvent event) {
        Window.alert("An error has occurred.");		
	}

	@Override
	public void onMouseWheel(MouseWheelEvent arg0) {
		Window.alert("An error has occurred.");
	}
	
	public void onDoubleClick(DoubleClickEvent arg0) {
		Window.alert("An error has occurred.");
	}
}
