package uk.me.graphe.client;

public class Console {
    public static native void log(String arg) /*-{
        console.log(arg);
    }-*/;
}
