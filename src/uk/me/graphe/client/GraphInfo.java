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
	
	private static GraphInfo sInstance;
	
	private GraphInfo(Graphemeui parent) {
		initWidget(uiBinder.createAndBindUi(this));
		lblTitle.setText("Test Title");
		lblUsers.setText("Unknown number of users viewing");
	}
	
	public static GraphInfo getInstance(Graphemeui parent){
		if(sInstance == null) sInstance = new GraphInfo(parent);
		return sInstance;
	}
	
	public static GraphInfo getInstance(){
		return sInstance;
	}
	
	public void update(String title, int users){
		lblTitle.setText(title);
		lblTitle.setText(users + " users viewing.");
	}
}
