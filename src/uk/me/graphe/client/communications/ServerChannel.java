package uk.me.graphe.client.communications;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;

public class ServerChannel {

    private static ServerChannel sInstance = null;

    private ServerChannel() {

    }

    public static ServerChannel getInstance() {
        if (sInstance == null) sInstance = new ServerChannel();
        return sInstance;
    }

    private List<ReceiveNotificationRunner> mNotificationCallbacks = new ArrayList<ReceiveNotificationRunner>();

    public native void init() /*-{
        $wnd.orbitedInit();
        $wnd.scInstance = this;
        //setup the callback to the recieve funciton
        $wnd.websocket.onread = function(message) {
            var jso = eval("(" + message.substring(0,message.length-1) + ")");
            scInstance.@uk.me.graphe.client.communications.ServerChannel::receive(Lcom/google/gwt/core/client/JavaScriptObject;)(jso);
        }
    }-*/;

    public native void send(JavaScriptObject s) /*-{
        $wnd.orbitedSend(s);
    }-*/;

    public void receive(JavaScriptObject jso) {
        notifyAllCallbacks(jso);
    }

    public void addReceiveNotification(ReceiveNotificationRunner rn) {
        mNotificationCallbacks.add(rn);
    }

    public void notifyAllCallbacks(JavaScriptObject jso) {
        for (ReceiveNotificationRunner rn : mNotificationCallbacks) {
            rn.run(jso);
        }
    }
}
