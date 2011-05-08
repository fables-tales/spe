package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
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
	
	public int lMouseDown[];
	private int lMouseMove[];
		
	private static final int X = 0, Y = 1;

	private boolean isMouseDown;
	
	public Canvas (Graphemeui gUI)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.parent = gUI;

		lMouseDown = new int[2];
		lMouseMove = new int[2];
	}
	
	@UiHandler("canvasPanel")
	public void onDoubleClick(DoubleClickEvent e)
	{
		Console.log("Double click");
		if (parent.tools.currentTool == Tools.select)
		{
			parent.clearSelectedObjects();
			
			if (parent.toggleSelectedVertexAt(lMouseDown[X], lMouseDown[Y]))
			{
				parent.dialogVertex.show(parent.selectedVertices.get(0).getLabel()
						, e.getX(), e.getY());	
				Console.log("Double click 1");
			}
			else if (parent.toggleSelectedEdgeAt(lMouseDown[X], lMouseDown[Y]))
			{
				parent.dialogEdge.show(String.valueOf(parent.selectedEdges.get(0).getWeight())
						, e.getX(), e.getY());
				Console.log("Double click 2");
			}
			
			parent.clearSelectedObjects();
		}
	}
	
	@UiHandler("canvasPanel")
	void onMouseDown(MouseDownEvent e)
	{
		isMouseDown = true;
		
		lMouseDown[X] = getMouseX(e.getX());
		lMouseDown[Y] = getMouseY(e.getY());
		
		lMouseMove[X] = lMouseDown[X];
		lMouseMove[Y] = lMouseDown[Y];
		
		switch (parent.tools.currentTool)
		{
			case addEdge:
				if (!parent.selectedVertices.contains(parent.graphManager.getVertexDrawableAt(lMouseDown[X], lMouseDown[Y])))
				{
					parent.toggleSelectedVertexAt(lMouseDown[X], lMouseDown[Y]);
				}
				parent.drawing.setUILine(lMouseDown[X], lMouseDown[Y], lMouseDown[X], lMouseDown[Y]);

				break;
			case move:
				if (parent.selectedVertices.size() < 1)
				{
					parent.toggleSelectedVertexAt(lMouseDown[X], lMouseDown[Y]); // try to select vertex.
				}
				break;
			case select:
				EdgeDrawable ed = parent.graphManager.getEdgeDrawableAt(lMouseDown[X], lMouseDown[Y]);
				
				if ((parent.selectedEdges.size() == 1) && (parent.selectedVertices.size() == 0) && ed != null && parent.selectedEdges.contains(ed))
				{
					parent.dialogEdge.show(String.valueOf(ed.getWeight()), e.getX(), e.getY());
				}
				else if ((parent.selectedEdges.size()  == 0) && (parent.selectedVertices.size() == 1))
				{
					VertexDrawable vd = parent.graphManager.getVertexDrawableAt(lMouseDown[X], lMouseDown[Y]);
					
					if (vd != null && parent.selectedVertices.contains(vd))
					{
						parent.dialogVertex.show(vd.getLabel(), e.getX(), e.getY());
					}
				}
				break;
		}
		
		parent.tooltip.hide();
	}
	
	@UiHandler("canvasPanel")
	void onMouseMove(MouseMoveEvent e)
	{
		int x = getMouseX(e.getX());
        int y = getMouseY(e.getY());
        
		if (isMouseDown)
		{	 
            switch (parent.tools.currentTool)
            {
	            case addEdge:        	
	            	VertexDrawable vHover = parent.graphManager.getVertexDrawableAt(x, y);
	            	if ((vHover == null) && (parent.selectedVertices.size() == 2))
	            	{
	            		parent.selectedVertices.get(1).setHilighted(false);
	            		parent.selectedVertices.remove(1);
	            	}
	            	else if (!parent.selectedVertices.contains(vHover))
					{
						parent.toggleSelectedVertexAt(x, y);
					}
	            	parent.drawing.setUILine(lMouseDown[X], lMouseDown[Y], x, y);
	            	parent.graphManager.invalidate();
	            	break;
	            case move:
					if (parent.selectedVertices.size() > 0)
					{
						for (VertexDrawable vd : parent.selectedVertices)
						{
							int xC = vd.getCenterX() -(lMouseMove[X] - x);
							int yC = vd.getCenterY() -(lMouseMove[Y] -y);
							
							parent.moveNode(vd, xC, yC);
						}
					}
					else
					{
						parent.pan(-(lMouseDown[X] - x), -(lMouseDown[Y] -y));
					}
					break;
            }
		}
		else
		{
			VertexDrawable vd = parent.graphManager.getVertexDrawableAt(x, y);
			
			if (vd != null)
			{				
				parent.tooltip.setPopupPosition((e.getX() + 60), (e.getY() + 140));
				parent.tooltip.setText(vd.getLabel());
				parent.tooltip.show();
			}
			else
			{
				parent.tooltip.hide();
			}
		}
		
		lMouseMove[X] = x;
		lMouseMove[Y] = y;
	}
	
	@UiHandler("canvasPanel")
	void onMouseOut (MouseOutEvent e)
	{
		isMouseDown = false;
		parent.drawing.hideUIline();
		parent.tooltip.hide();
	}
	
	@UiHandler("canvasPanel")
	void onMouseWheel (MouseWheelEvent e)
	{
		if(e.isAltKeyDown())
		{
			if(e.isNorth())
			{
				parent.zoomIn();
			}
			else
			{
				parent.zoomOut();
			}
		}
	}
	
	@UiHandler("canvasPanel")
	void onMouseUp (MouseUpEvent e)
	{
		isMouseDown = false;
		
		switch (parent.tools.currentTool)
		{
			case addVertex:
				parent.dialogVertex.show("", e.getX(), e.getY());
				break;
			case addEdge:
				parent.drawing.hideUIline();
				if (parent.selectedVertices.size() == 2) 
				{
					if(!parent.graphManager.isDirectedEdgeBetweenVertices(
							parent.graphManager.getVertexFromDrawable(parent.selectedVertices.get(0)), 
							parent.graphManager.getVertexFromDrawable(parent.selectedVertices.get(1))))
					{
						if ((parent.drawing.isFlowChart()) || (!parent.drawing.isWeighted()))
						{
							parent.addEdge(parent.selectedVertices.get(0), parent.selectedVertices.get(1), 0);
						}
						else
						{
							parent.dialogEdge.show("", e.getX(), e.getY());
						}
					}
				}				
				else if ((lMouseDown[X] != lMouseMove[X]) || (lMouseDown[Y] != lMouseMove[Y]))
				{
					parent.clearSelectedObjects();
				}
				
				parent.graphManager.invalidate();	
				break;
			case move:
				if (parent.selectedVertices.size() == 1)
				{
					parent.clearSelectedObjects(); //deselect the moved vertex if only one moved.
				}
				break;
			case select:
				if (e.isControlKeyDown())
				{
					parent.toggleSelectedObjectAt(lMouseDown[X], lMouseDown[Y]);
				}
				else
				{
					parent.clearSelectedObjects(); // clearing because we are selecting object on own.
					parent.toggleSelectedObjectAt(lMouseDown[X], lMouseDown[Y]);
				}
				break;
			case zoom:
				if (e.isControlKeyDown())
				{
					parent.zoomOut();
				}
				else
				{
					parent.zoomIn();
				}
				break;
		}
	}
	
	private int getMouseX (int x)
	{
		return (int)(x / parent.drawing.getZoom()) - parent.drawing.getOffsetX();
	}
	
	private int getMouseY (int y)
	{
		return (int)(y / parent.drawing.getZoom()) - parent.drawing.getOffsetY();
	}
}
