
package uk.me.graphe.client.dialogs;

import uk.me.graphe.client.Graphemeui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GraphOptionsDialog extends PopupPanel
{
	private static GraphOptionsDialog sInstance;
	
	private final Graphemeui parent;
	private final VerticalPanel pnlCont;
	private final HorizontalPanel pnlBtns;
	private final Label lblTitle;
	private final CheckBox cbIsWeighted, cbIsDigraph, cbIsFlowChart;
	private final Button btnOk, btnCancel;

	public GraphOptionsDialog (Graphemeui gui)
	{
		this.parent = gui;
		this.setAnimationEnabled(true);
		this.setGlassEnabled(true);
		this.setAutoHideEnabled(true);
		this.setTitle("Graph options");		
		this.setStyleName("optionsDialog");
		
		lblTitle = new Label("Graph options:");
		cbIsFlowChart = new CheckBox("Set to flow chart.");
		cbIsDigraph = new CheckBox("Set to directed-graph.");
		cbIsWeighted = new CheckBox("Set to weighted-graph.");
		btnOk = new Button("Save");
		btnCancel = new Button ("Cancel");
		
		btnOk.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent arg0)
			{
				ok();
			}
			
		});		
		
		btnCancel.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent arg0)
			{
				cancel();
			}
			
		});	
		
		pnlCont = new VerticalPanel();
		pnlBtns = new HorizontalPanel();
		
		pnlCont.add(lblTitle);
		pnlCont.add(cbIsFlowChart);
		pnlCont.add(cbIsDigraph);
		pnlCont.add(cbIsWeighted);
		
		pnlBtns.add(btnOk);
		pnlBtns.add(btnCancel);		
		
		pnlCont.add(pnlBtns);
		
		cbIsFlowChart.addValueChangeHandler(new ValueChangeHandler<Boolean>()
		{
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> arg0)
			{
				if (arg0.getValue())
				{
					cbIsDigraph.setEnabled(false);
					cbIsWeighted.setEnabled(false);
				}
				else
				{
					cbIsDigraph.setEnabled(true);
					cbIsWeighted.setEnabled(true);
				}
			}
			
		});
		
		super.add(pnlCont);
	}

	public static GraphOptionsDialog getInstance (Graphemeui gui)
	{
		if (sInstance == null) sInstance = new GraphOptionsDialog(gui);
        return sInstance;
	}
	
	public static GraphOptionsDialog getInstance ()
	{
		return sInstance;
	}
	
	private void ok ()
	{
		parent.editGraphProperties(cbIsDigraph.getValue(), cbIsFlowChart.getValue(), cbIsWeighted.getValue());
		parent.isHotkeysEnabled = true;
		super.hide();
	}
	
	private void cancel ()
	{
		parent.isHotkeysEnabled = true;
		super.hide();
	}
	
	public void show (String s)
	{
		cbIsDigraph.setValue(parent.drawing.isDigraph());
		cbIsWeighted.setValue(parent.drawing.isWeighted());
		cbIsFlowChart.setValue(parent.drawing.isFlowChart());
		parent.isHotkeysEnabled = false;
		super.center();
		super.show();		
	}
}
