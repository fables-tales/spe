package uk.me.graphe.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class used to handle clicks on canvas.
 * 
 * @author ed/ben
 * 
 * @mouseDown stores the first point the user dragged from
 * @mouseMove continually updates the end coordinates
 * @mouseUp when this is called the last set of coordinates are 
 * used as the end coordinates.
 * Finally the canvas calls for the options panel from the
 * graphemeui class.
 */
public class Canvas extends Composite{

	private static UiBinderCanvas uiBinder = GWT
			.create(UiBinderCanvas.class);

	interface UiBinderCanvas extends UiBinder<Widget, Canvas> {
	}
	
	public Graphemeui parent;
	public int x1, x2, y1, y2, panx, pany, offsetX, offsetY;
	public boolean pressed;
	public double zoom;
	@UiField
	public CanvasWrapper canvasPanel;

	public Canvas(Graphemeui parent) {
		initWidget(uiBinder.createAndBindUi(this));
		pressed = false;
		offsetX = 0;
		offsetY = 0;
		zoom = 1;
		this.parent = parent;
	}
	
	@UiHandler("canvasPanel")
	void onMouseDown(MouseDownEvent e){
		//get initial click location
		x1 = (int)(e.getX()/zoom)-offsetX;
		y1 = (int)(e.getY()/zoom)-offsetY;
		//make end point equal to start at beginning
		x2 = x1;
		y2 = y1;
		//keeps track of previous end point for working out pan
		panx = x1;
		pany = y1;
		//user is dragging until mouse up
		pressed = true;
	}
	
	@UiHandler("canvasPanel")
	void onMouseMove(MouseMoveEvent e){
		//keep record of last end point
		panx = x2;
		pany = y2;
		//get new end point
		x2 = (int)(e.getX()/zoom)-offsetX;
		y2 = (int)(e.getY()/zoom)-offsetY;
		//if dragging using the move tool
		if (parent.tools.getTool() == 5 && pressed) {
			//call graphemeui move method
			parent.move(x1, y1, panx, pany, x2, y2);
		}
	}
	
	@UiHandler("canvasPanel")
	void onMouseOut(MouseOutEvent e){
		pressed = false;
		parent.moving = false;
		parent.movingVertex = null;
	}
	
	@UiHandler("canvasPanel")
	void onMouseUp(MouseUpEvent e){
		pressed = false;
		parent.moving = false;
		parent.movingVertex = null;
		if (parent.tools.getOptionsPanel().getWidgetCount() != 0) {
			parent.tools.getOptionsPanel().remove(0);
		}
		if (parent.tools.getTool() == 1) {
			parent.initOptions(x1, y1, x2, y2);
		}
		if(parent.tools.getTool() == 6){
			if(e.isControlKeyDown()){
				parent.zoom(false, x1, y1);
			} else {
				parent.zoom(true, x1, y1);
			}
		}
	}
	
	public void setOffset(int x, int y){
		offsetX = x;
		offsetY = y;
	}
	
	public int getOffsetX(){
		return offsetX;
	}
	
	public int getOffsetY(){
		return offsetY;
	}
}
