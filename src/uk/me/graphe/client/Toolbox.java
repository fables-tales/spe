package uk.me.graphe.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Toolbox extends Composite {

	private static UiBinderToolbox uiBinder = GWT.create(UiBinderToolbox.class);
	public int toolNum;

	interface UiBinderToolbox extends UiBinder<Widget, Toolbox> {}

	@UiField
	HorizontalPanel options, tools;
	@UiField
	Button button1, button2, button3, button4, button5;
	Graphemeui parent;
	
	public Toolbox(Graphemeui parent) {
		this.parent = parent;
		initWidget(uiBinder.createAndBindUi(this));
		toolNum = 0;
		ClickHandler ch = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == button1){
					setTool(1);
				}else if(event.getSource() == button2){
					setTool(2);
				}else if(event.getSource() == button3){
					setTool(3);
				}else if(event.getSource() == button4){
					setTool(4);
				}else if(event.getSource() == button5){
					setTool(5);
				}
			}
		};
		button1.addClickHandler(ch);
		button2.addClickHandler(ch);
		button3.addClickHandler(ch);
		button4.addClickHandler(ch);
		button5.addClickHandler(ch);
	}
	
	public int getTool(){
		return toolNum;
	}

	private void setTool(int i) {
		toolNum = i;
		if (options.getWidgetCount() != 0) options.remove(0);
		if (i == 2 || i == 3 || i == 4) {
			parent.initOptions(0, 0, 0, 0);
		}
	}
	
	public HorizontalPanel getOptionsPanel(){
		return options;
	}
	
	public HorizontalPanel getToolsPanel(){
		return tools;
	}
}
