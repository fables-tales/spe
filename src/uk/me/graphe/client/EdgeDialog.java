package uk.me.graphe.client;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class EdgeDialog extends HorizontalPanel{
	public int v1Index, v2Index, edgeIndex;
	public boolean isOpen;
	public Graphemeui parent;
	int x1, y1;
	
	public EdgeDialog(Graph g, final int type, final Graphemeui parent){
		// Make a new list box, adding a few items to it.
	    final ListBox fromVertices = new ListBox();
	    final ListBox toVertices = new ListBox();
	    final ListBox edges = new ListBox();
	    for(Vertex v : g.getVertices()){
	    	fromVertices.addItem(v.getLabel());
	    	toVertices.addItem(v.getLabel());
	    }
	    for(Edge e : g.getEdges()){
	    	edges.addItem(e.getFromVertex().getLabel() + " to " + 
	    					e.getToVertex().getLabel());
	    }
	    edgeIndex = -1;
	    v1Index = -1;
	    v2Index = -1;
	    isOpen = true;
	    this.parent = parent;
	    
	    // Make enough room for all items (setting this value to 1 turns it
	    // into a drop-down list).
	    fromVertices.setVisibleItemCount(1);
	    toVertices.setVisibleItemCount(1);
	    fromVertices.setSelectedIndex(0);
	    toVertices.setSelectedIndex(0);
	    edges.setSelectedIndex(0);
	    
	    //create a label
	    Label l = new Label("From vertex: ");
	    Label l2 = new Label("To vertex: ");
	    Label l3 = new Label("Select Edge: ");
	    Label l4 = new Label("Vertex Name");
	    
	    final TextBox tb = new TextBox();
	    tb.setWidth("150");

	    final Button ok = new Button("OK");
	    final Button cancel = new Button("Cancel");
	    
	    tb.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				if(tb.getText().length() > 0){
					ok.setEnabled(true);
				}
				else {
					ok.setEnabled(false);
				}
			}
	    });
	    
	    HorizontalPanel v = new HorizontalPanel();
	    HorizontalPanel buttons = new HorizontalPanel();
	    if(type == 0){
	    	if(toVertices.getItemCount() == 0) ok.setEnabled(false);
	    	v.add(l);
	    	v.add(fromVertices);
	    	v.add(l2);
	    	v.add(toVertices);
	    }
	    if(type == 1){
	    	if(fromVertices.getItemCount() == 0) ok.setEnabled(false);
	    	v.add(l);
	    	v.add(fromVertices);
	    }
	    if(type == 2){
	    	if(edges.getItemCount() == 0) ok.setEnabled(false);
	    	v.add(l3);
	    	v.add(edges);
	    }
	    if(type == 3){
	    	ok.setEnabled(false);
	    	v.add(l4);
	    	v.add(tb);
	    }

	    ok.addClickHandler(new ClickHandler() {
	    	@Override
			public void onClick(ClickEvent event) {
	    		if(type == 0 || type == 1){
	    			v1Index = fromVertices.getSelectedIndex();
	    		}
				if(type == 0){
					v2Index = toVertices.getSelectedIndex();
				}
				if(type == 2){
					edgeIndex = edges.getSelectedIndex();
				}
				parent.addElement(type, v1Index, v2Index, edgeIndex, tb.getText(), EdgeDialog.this);
			}
	    });
	    cancel.addClickHandler(new ClickHandler() {
	    	@Override
	    	public void onClick(ClickEvent event) {
	    		parent.removeOptions(EdgeDialog.this);
	    	}
	    });
	    buttons.add(ok);
	    buttons.add(cancel);
	    v.add(buttons);
	    add(v);
	}
	
	public void setPoint(int x, int y){
		x1 = x;
		y1 = y;
	}
	
	public int[] getPoint(){
		int[] ret = {x1, y1};
		return ret;
	}
}
