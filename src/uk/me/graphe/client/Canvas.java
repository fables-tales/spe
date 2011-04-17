package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

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
	void onMouseDown(MouseDownEvent e){
		if (!isMouseDown)
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
						//parent.addEdge(parent.selectedVertices.get(0),parent.selectedVertices.get(1));
						//parent.tools.setTool(Tools.weightEdge);
					}
					break;
				case move:
					if (parent.selectedVertices.size() > 0)
					{
						parent.toggleSelectedVertexAt(lMouseDown[X], lMouseDown[Y]); // try to select vertex.
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
	}
	
	@UiHandler("canvasPanel")
	void onMouseMove(MouseMoveEvent e)
	{
		if (isMouseDown)
		{
			this.setTitle("");
			
			int x = getMouseX(e.getX());
            int y = getMouseY(e.getY());
            
			switch (parent.tools.currentTool)
			{
				case move:
					if (parent.selectedVertices.size() > 0) {
						// TODO: Move the nodes here by the offset.
					} else {
						// TODO: User wants to pan - fix this...it is jittery.
						parent.pan(-(lMouseMove[X] - x), -(lMouseMove[Y] -y));
					}
					break;
				default:
					break;
			}
			
			lMouseMove[X] = x;
			lMouseMove[Y] = y;
		}
	}
	
	@UiHandler("canvasPanel")
	void onMouseOut(MouseOutEvent e){
		isMouseDown = false;
		//parent.moving = false;
		//parent.movingVertex = null;
	}
	
	@UiHandler("canvasPanel")
	void onMouseUp(MouseUpEvent e){
		switch (parent.tools.currentTool){
			case addVertex:
				parent.dialog.show(DialogType.vertexName,"");
				break;
			case addEdge:
				if (parent.selectedVertices.size() == 2) {
					parent.dialog.show(DialogType.edgeWeight,"");
				}
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
	}
	
	private int getMouseX(int x) {
		return (int)(x / parent.drawing.getZoom()) - parent.drawing.getOffsetX();
	}
	
	private int getMouseY(int y) {
		return (int)(y / parent.drawing.getZoom()) - parent.drawing.getOffsetY();
	}
}
