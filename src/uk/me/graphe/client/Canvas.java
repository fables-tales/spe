package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Canvas extends Composite{
	private static UiBinderCanvas uiBinder = GWT.create(UiBinderCanvas.class);
	interface UiBinderCanvas extends UiBinder<Widget, Canvas> {}

	@UiField
	public CanvasWrapper canvasPanel;
	
	public final Graphemeui parent;
	
	public int lMouseDown[], lMouseMove[], lMouseUp[]; // last mouse positions.
		
	private static final int X = 0, Y = 1;

	private boolean isMouseDown;

	
	public Canvas(Graphemeui gUI) {
		initWidget(uiBinder.createAndBindUi(this));
		this.parent = gUI;
		
		lMouseDown = new int[2];
		lMouseMove = new int[2];
		lMouseUp = new int[2];
	}
	
	@UiHandler("canvasPanel")
	void onKeyUp(KeyUpEvent e)
	{
		parent.tools.setLabel("hey");
	}
	
	@UiHandler("canvasPanel")
	void onMouseDown(MouseDownEvent e){
		if (parent.tools.currentTool != Tools.nameVertex)
		{
			isMouseDown = true;
			
			lMouseDown[X] = getMouseX(e.getX());
			lMouseDown[Y] = getMouseY(e.getY());
			
			lMouseMove[X] = lMouseDown[X];
			lMouseMove[Y] = lMouseDown[Y];
			
			lMouseUp[X] = lMouseDown[X];
			lMouseUp[Y] = lMouseDown[Y];	
			
			switch (parent.tools.currentTool) {
				case addEdge:
					parent.toggleSelectedVertexAt(lMouseDown[X], lMouseDown[Y]);
					
					if (parent.selectedVertices.size() > 1) {
						parent.addEdge(parent.selectedVertices.get(0),parent.selectedVertices.get(1));
					}
					break;
				case move:
					if (parent.selectedVertices.size() > 0) {
						// TODO: Move the nodes here by the offset.
					} else if (parent.toggleSelectedVertexAt(lMouseDown[X], lMouseDown[Y])) {
						// TODO: Move this one node then deselect it.
					} else {
						// TODO: User wants to pan.
					}
					break;
				case select:
					if (e.isControlKeyDown())
					{
						parent.toggleSelectedObjectAt(lMouseDown[X], lMouseDown[Y]);
					} else {
						parent.clearSelectedObjects(); // clearing because we are selecting object on own.
						parent.toggleSelectedObjectAt(lMouseDown[X], lMouseDown[Y]);
					}
					break;
				default:
					break;
			}
		}
		/*
		//get initial click location
		//x1 = (int)(e.getX()/zoom)-offsetX;
		//y1 = (int)(e.getY()/zoom)-offsetY;
		x1 = getMouseX(e.getX());
		y1 = getMouseX(e.getY());
		//make end point equal to start at beginning
		x2 = x1;
		y2 = y1;
		//keeps track of previous end point for working out pan
		panx = x1;
		pany = y1;
		
       /* int left = x2 - panx;
        int top = y2 - pany;
        pan(left, top);
        
        */
	}
	
	@UiHandler("canvasPanel")
	void onMouseMove(MouseMoveEvent e){
		if (parent.tools.currentTool != Tools.nameVertex){

			
			if (isMouseDown)
			{
                int x = getMouseX(e.getX());
                int y = getMouseY(e.getY());
				
				parent.pan(-(lMouseMove[X] - x), -(lMouseMove[Y] -y));
				
				lMouseMove[X] = x;
				lMouseMove[Y] = y;
			}
			
		}
	}
	
	@UiHandler("canvasPanel")
	void onMouseOut(MouseOutEvent e){
		//isMouseDown = false;
		//parent.moving = false;
		//parent.movingVertex = null;
	}
	
	@UiHandler("canvasPanel")
	void onMouseUp(MouseUpEvent e){
		switch (parent.tools.currentTool){
			case addVertex:
				parent.tools.setTool(Tools.nameVertex);
				break;
			case move:
				break;
			case select:
				break;
			case zoom:
				if(e.isControlKeyDown()){
					parent.zoomOut();
				} else {
					parent.zoomIn();
				}
				break;
			default:
				break;
		}
		//parent.tools.setTool(Tools.nameVertex);
		//parent.moving = false;
		//parent.movingVertex = null;
		isMouseDown = false;
		/*if (parent.tools.getOptionsPanel().getWidgetCount() != 0) {
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
		}*/
	}
	
	private int getMouseX(int x) {
		return (int)(x / parent.drawing.getZoom()) - parent.drawing.getOffsetX();
	}
	
	private int getMouseY(int y) {
		return (int)(y / parent.drawing.getZoom()) - parent.drawing.getOffsetY();
	}
}
