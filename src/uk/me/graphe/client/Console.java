package uk.me.graphe.client;

public class Console {
    public static native void log(String arg) /*-{      
        if(typeof(console) !== 'undefined' && console != null) {
            console.log(arg);
        }
    }-*/;
}
