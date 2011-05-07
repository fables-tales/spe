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
		
		lblTitle.setText(parent.graphManager.getName());
		lblUsers.setText("Directed, weighted graph");
		
		lblTitle.addDoubleClickHandler(new DoubleClickHandler()
		{
			@Override
			public void onDoubleClick(DoubleClickEvent arg0)
			{
				parent.dialogGraphName.show(parent.graphManager.getName());
			}			
		});
	}
	
	public void update(int users){
		lblTitle.setText(parent.graphManager.getName());
		
		/*if (users > 0)
		{
			lblTitle.setText(users + " number of editors.");
		}
		else
		{
			lblUsers.setText("Unknown number of editors.");
		}*/
	}	
}