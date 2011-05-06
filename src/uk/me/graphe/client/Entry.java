package uk.me.graphe.client;

import com.google.gwt.core.client.EntryPoint;
import uk.me.graphe.client.Graphemeui;
import uk.me.graphe.client.UserPanel;
import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.client.json.wrapper.JSOFactory;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.messages.UserAuthMessage;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;



public class Entry implements EntryPoint {

	public final Graphemeui editor = new Graphemeui();
	
    public void onModuleLoad() {
    	    	
        JSONImplHolder.initialise(new JSOFactory());

        ServerChannel sc = ServerChannel.getInstance();
        ClientOT.getInstance();
        sc.init();
    	
        
        if(Window.Location.getParameter("action") == "userauth"){
        	
        	UserPanel.verify();
            
        }else if(Window.Location.getHash().substring(1) != ""){
    	editor.show();
        }else{
    	UserPanel.show();
    	
        }
    	
    }
	
	
}
