package me.teaisaweso.client.graphmanagers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

import me.teaisaweso.client.EdgeDrawable;
import me.teaisaweso.client.VertexDrawable;
import me.teaisaweso.shared.Edge;
import me.teaisaweso.shared.Graph;
import me.teaisaweso.shared.Vertex;
import me.teaisaweso.shared.VertexDirection;

public class GraphManager2dImpl implements GraphManager2d {

    private List<Vertex> mVertices = new ArrayList<Vertex>();
    private List<Edge> mEdges = new ArrayList<Edge>();
    private Map<Vertex, VertexDrawable> mVertexRenderMap = new HashMap<Vertex, VertexDrawable>();

    protected GraphManager2dImpl() {

    }

    @Override
    public Graph getUnderlyingGraph() {
        return new Graph(mEdges, mVertices);
    }

    @Override
    public void addVertex(Vertex v, int xPosition, int yPosition, int size) {
        mVertices.add(v);
        // left and top are x and y - size/2
        int halfSize = size / 2;
        int left = xPosition - halfSize;
        int top = yPosition - halfSize;

        mVertexRenderMap.put(v, new VertexDrawable(left, top, size, size, v.getLabel()));

    }

    @Override
    public void removeVertex(Vertex v) {
        mVertices.remove(v);
        mVertexRenderMap.remove(v);
    }

    @Override
    public void moveVertexTo(Vertex v, int xPosition, int yPosition) {
        VertexDrawable vd = mVertexRenderMap.get(v);
        int halfWidth = vd.getWidth() / 2;
        int halfHeight = vd.getHeight() / 2;
        int left = xPosition - halfWidth;
        int top = yPosition - halfHeight;
        vd.updateBoundingRectangle(left, top, vd.getWidth(), vd.getHeight());

    }

    @Override
    public void scaleVertex(Vertex v, int newSize) {
        VertexDrawable vd = mVertexRenderMap.get(v);
        int newLeft = vd.getLeft() - newSize / 2;
        int newTop = vd.getTop() - newSize / 2;
        int newWidth = newSize;
        int newHeight = newSize;
        vd.updateBoundingRectangle(newLeft, newTop, newWidth, newHeight);
    }

    @Override
    public void addEdge(Vertex v1, Vertex v2, VertexDirection dir) {
        mEdges.add(new Edge(v1, v2, dir));
    }

    @Override
    public Collection<VertexDrawable> getVertexDrawables() {
        return mVertexRenderMap.values();
    }

    @Override
    public Collection<EdgeDrawable> getEdgeDrawables() {
        List<EdgeDrawable> result = new ArrayList<EdgeDrawable>(mEdges.size());

        for (Edge e : mEdges) {
            VertexDrawable v1 = mVertexRenderMap.get(e.getFromVertex());
            VertexDrawable v2 = mVertexRenderMap.get(e.getToVertex());
            int l1 = v1.getCenterX();
            int l2 = v2.getCenterX();
            int t1 = v1.getCenterY();
            int t2 = v2.getCenterY();

            // swap l1 and t1 with l2 and t2 if we're entering the "from" node
            // NOTE: that's an in place swap algorithm using xor
            if (e.enters(e.getFromVertex())) {
                l1 ^= l2;
                l2 ^= l1;
                l1 ^= l2;

                t1 ^= t2;
                t2 ^= t1;
                t1 ^= t2;
            }

            result.add(new EdgeDrawable(l1, t1, l2, t2, e.hasDirection()));
        }

        return result;
    }

    @Override
    public void removeEdge(Edge e) {
        mEdges.remove(e);
    }

    @Override
    public void removeAllEdges(Vertex v1, Vertex v2) {
        List<Edge> toDelete = new ArrayList<Edge>();

        for (Edge e : mEdges) {
            if ((e.enters(v1) || e.exits(v1)) && (e.enters(v2) || e.exits(v2))) {
                toDelete.add(e);
            }
        }

        mEdges.removeAll(toDelete);
    }

    @Override
    public void addRedrawCallback(Runnable r) {

    }

    @Override
    public void init(GWTCanvas canvas) {
        // TODO Auto-generated method stub

    }
}
