package uk.me.graphe.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
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
	public int x1, x2, y1, y2;
	public boolean pressed;
	@UiField
	public CanvasWrapper canvasPanel;	

	public Canvas(Graphemeui parent) {
		initWidget(uiBinder.createAndBindUi(this));
		pressed = false;
		this.parent = parent;
	}
	
	@UiHandler("canvasPanel")
	void onMouseDown(MouseDownEvent e){
		x1 = e.getX();
		y1 = e.getY();
		pressed = true;
		
	}
	
	@UiHandler("canvasPanel")
	void onMouseMove(MouseMoveEvent e){
		x2 = e.getX();
		y2 = e.getY();
		if (parent.tools.getTool() == 5 && pressed) {
			parent.move(x1, y1, x2, y2);
		}
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
	}
}
