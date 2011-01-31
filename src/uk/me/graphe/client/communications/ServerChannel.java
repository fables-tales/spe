package uk.me.graphe.client.communications;

import java.util.ArrayList;
import java.util.List;

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
            var jso = message.substring(0,message.length-1);
            scInstance.@uk.me.graphe.client.communications.ServerChannel::receive(Ljava/lang/String;)(jso);
        }
    }-*/;

    public native void send(String s) /*-{
        $wnd.orbitedSend(s + "\0");
    }-*/;

    public void receive(String s) {
        notifyAllCallbacks(s);
    }

    public void addReceiveNotification(ReceiveNotificationRunner rn) {
        mNotificationCallbacks.add(rn);
    }

    public void notifyAllCallbacks(String s) {
        for (ReceiveNotificationRunner rn : mNotificationCallbacks) {
            rn.run(s);
        }
    }

}
