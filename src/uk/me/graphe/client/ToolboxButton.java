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
				break;
			case move:
				this.setStyleName("btnMove");
				break;
			case zoom:
				this.setStyleName("btnZoom");
				break;
			case addVertex:
				this.setStyleName("btnAddVert");
				break;
			case addEdge:
				this.setStyleName("btnAddEdge");
				break;
			case delete:
				this.setStyleName("btnDelete");
				break;
			case autolayout:
				this.setStyleName("btnAutoLayout");
				break;
			case cluster:
				this.setStyleName("btnCluster");
				break;
			case styleProcess:
				this.setStyleName("btnProcess");
				break;
			case styleTerminator:
				this.setStyleName("btnTerminator");
				break;
			case styleDecision:
				this.setStyleName("btnDecision");
				break;
			case styleNormal:
				this.setStyleName("btnNormal");
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