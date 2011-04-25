package uk.me.graphe.client;

import java.util.Collection;

public class DrawingGraphML{ 

    private static Collection<EdgeDrawable> mEdgesToDraw;
    private static Collection<VertexDrawable> mVerticesToDraw;
   
    public static String getString(Collection<EdgeDrawable> edges,Collection<VertexDrawable> vertices){
        mEdgesToDraw = edges;
        mVerticesToDraw = vertices;
        return "hello world";
    }
}
