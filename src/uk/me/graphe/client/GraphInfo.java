package uk.me.graphe.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GraphInfo extends Composite {
	private static UiBinderGraphInfo uiBinder = GWT.create(UiBinderGraphInfo.class);
	interface UiBinderGraphInfo extends UiBinder<Widget, GraphInfo> {}

	@UiField
	Label lblTitle, lblUsers;
	
	private final Graphemeui parent;
	
	public GraphInfo(Graphemeui gUI) {
		initWidget(uiBinder.createAndBindUi(this));		
		this.parent = gUI;
		
		update();
		
		lblTitle.addDoubleClickHandler(new DoubleClickHandler()
		{
			@Override
			public void onDoubleClick(DoubleClickEvent arg0)
			{
				parent.dialogGraphName.show(parent.graphManager.getName());
			}			
		});
		
		lblUsers.addDoubleClickHandler(new DoubleClickHandler()
		{
			@Override
			public void onDoubleClick(DoubleClickEvent arg0)
			{
				parent.dialogGraphOptions.show("");
			}			
		});
	}
	
	public void update()
	{
		lblTitle.setText(parent.graphManager.getName());

		if (parent.drawing.isFlowChart())
		{
			lblUsers.setText("Flow chart.");
		}
		else
		{
			if (parent.drawing.isDigraph() && parent.drawing.isWeighted())
			{
				lblUsers.setText("Weighted, directed graph");
			}
			else if (parent.drawing.isDigraph() && !parent.drawing.isWeighted())
			{
				lblUsers.setText("Directed graph.");
			}
			else if (!parent.drawing.isDigraph() && parent.drawing.isWeighted())
			{
				lblUsers.setText("Weighted graph.");
			}
			else
			{
				lblUsers.setText("Simple graph.");
			}
		}
	}	
}