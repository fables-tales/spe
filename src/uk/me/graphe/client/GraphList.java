package uk.me.graphe.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GraphList extends VerticalPanel{
	

	private final ArrayList<GraphListItem> labels = new ArrayList<GraphListItem>();
	private final ClickHandler ch = new ClickHandler()
	{
		@Override
		public void onClick(ClickEvent e) {
			// TODO: LOAD GRAPH				
		}
	};
	private int iterator;
	
	public GraphList(){	
		iterator = 0;
	}
	
	public void test(){
		String[] names = new String[]{"Graph 1", "Graph 2", "Graph 3"};
		for(String s: names){
			addGraph(s, iterator);
			iterator++;
		}
	}
	
	public void addGraph(String name, int id){
		GraphListItem gli = new GraphListItem(name, id);
		gli.addClickHandler(ch);
		labels.add(gli);
		this.add(gli);
	}
	
	public void removeGraph(int id){
		for(GraphListItem gli : labels){
			if(gli.getID() == id){
				this.remove(gli);
			}
		}
	}
}
