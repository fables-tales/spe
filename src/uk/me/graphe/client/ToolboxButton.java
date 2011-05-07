package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;


public class ToolboxButton extends Button
{
	private final Toolbox parent;
	private final Tools type;
	
	public ToolboxButton (Toolbox toolbox, Tools tool)
	{
		this.parent = toolbox;
		this.type = tool;

		switch (tool)
		{
			case select:
				this.setStyleName("btnSelect");
				this.setTitle("Select (s)");
				break;
			case move:
				this.setStyleName("btnMove");
				this.setTitle("Move (m)");
				break;
			case zoom:
				this.setStyleName("btnZoom");
				this.setTitle("Zoom (z)");
				break;
			case addVertex:
				this.setStyleName("btnAddVert");
				this.setTitle("Add Vertex (v)");
				break;
			case djikstra:
				this.setStyleName("btnDjikstra");
				this.setTitle("Run Dijkstra");
				break;
			case step:
				this.setStyleName("btnStep");
				this.setTitle("Step Once");
				break;
			case stepAll:
				this.setStyleName("btnStepAll");
				this.setTitle("Step All");
				break;
			case addEdge:
				this.setStyleName("btnAddEdge");
				this.setTitle("Add Edge (e)");
				break;
			case delete:
				this.setStyleName("btnDelete");
				this.setTitle("Delete (DEL)");
				break;
			case autolayout:
				this.setStyleName("btnAutoLayout");
				this.setTitle("Auto Layout");
				break;
			case graphOptions:
				this.setStyleName("btnGraphOptions");
				this.setTitle("Set Graph Options");
				break;
			case cluster:
				this.setStyleName("btnCluster");
				this.setTitle("Cluster");
				break;
			case styleProcess:
				this.setStyleName("btnProcess");
				this.setTitle("Set Process Style");
				break;
			case styleTerminator:
				this.setStyleName("btnTerminator");
				this.setTitle("Set Terminator Style");
				break;
			case styleDecision:
				this.setStyleName("btnDecision");
				this.setTitle("Set Decision Style");
				break;
			case styleNormal:
				this.setStyleName("btnNormal");
				this.setTitle("Set Normal Style");
				break;
			case toggleEdgeDirection:
				this.setStyleName("btnToggleDirection");
				this.setTitle("Toggle Edge Direction");
				break;
		}
		
		super.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent arg0)
			{
				parent.setTool(type);
			}
		});
		
		super.addMouseOutHandler(new MouseOutHandler()
		{
			@Override
			public void onMouseOut(MouseOutEvent arg0)
			{
				parent.showToolInfo(parent.currentTool);
			}
		});
		
		super.addMouseOverHandler(new MouseOverHandler()
		{
			@Override
			public void onMouseOver(MouseOverEvent arg0)
			{
				parent.showToolInfo(type);
			}
		});
		
		super.addMouseMoveHandler(new MouseMoveHandler()
		{
			@Override
			public void onMouseMove(MouseMoveEvent arg0)
			{
				parent.showToolInfo(type);
			}
		});
	}
}