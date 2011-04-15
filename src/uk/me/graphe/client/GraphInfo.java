package uk.me.graphe.client;

import com.google.gwt.core.client.GWT;
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
		
		lblTitle.setText("Unknown graph title");
		lblUsers.setText("Unknown number of users viewing");
	}
	
	public void update(String title, int users){
		lblTitle.setText(title);
		if (users > 0)
		{
			lblTitle.setText(users + " users viewing.");
		}
		else
		{
			lblUsers.setText("Unknown number of users viewing");
		}
	}	
}