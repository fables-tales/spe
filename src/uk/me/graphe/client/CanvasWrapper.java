package uk.me.graphe.client;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class CanvasWrapper extends GWTCanvas implements MouseOutHandler, MouseDownHandler, MouseUpHandler,
        MouseMoveHandler, KeyUpHandler, HasMouseDownHandlers, HasMouseMoveHandlers, HasMouseUpHandlers,
        HasMouseOutHandlers, HasKeyUpHandlers {

    public CanvasWrapper() {
        super(2000, 2000);
        sinkEvents(Event.MOUSEEVENTS);
        sinkEvents(Event.KEYEVENTS);
        this.createHandlerManager();
    }

    public CanvasWrapper(int coordX, int coordY, int pixelX, int pixelY) {
        super(coordX, coordY, pixelX, pixelY);
        sinkEvents(Event.MOUSEEVENTS);
        sinkEvents(Event.KEYEVENTS);
    }

    public CanvasWrapper(int coordX, int coordY) {
        super(coordX, coordY);
        sinkEvents(Event.MOUSEEVENTS);
        sinkEvents(Event.KEYEVENTS);
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
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return addHandler(handler, KeyUpEvent.getType());
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
	public void onKeyUp(KeyUpEvent event) {
		Window.alert("An error has occurred.");	
	}
}
