package me.teaisaweso.client.graphmanagers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Runnable> mRedrawCallbacks = new ArrayList<Runnable>();
    private Map<Vertex, List<Edge>> mVertexEdgeMap = new HashMap<Vertex, List<Edge>>();

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
        mVertexEdgeMap.put(v, new ArrayList<Edge>());
        this.invalidate();
    }

    @Override
    public void removeVertex(Vertex v) {
        mVertices.remove(v);
        mVertexRenderMap.remove(v);
        
        for (Edge e : mVertexEdgeMap.get(v)) {
            mEdges.remove(e);
        }
        
        this.invalidate();
    }

    @Override
    public void moveVertexTo(Vertex v, int xPosition, int yPosition) {
        VertexDrawable vd = mVertexRenderMap.get(v);
        int halfWidth = vd.getWidth() / 2;
        int halfHeight = vd.getHeight() / 2;
        int left = xPosition - halfWidth;
        int top = yPosition - halfHeight;
        vd.updateBoundingRectangle(left, top, vd.getWidth(), vd.getHeight());
        this.invalidate();

    }

    @Override
    public void scaleVertex(Vertex v, int newSize) {
        VertexDrawable vd = mVertexRenderMap.get(v);
        int newLeft = vd.getLeft() - newSize / 2;
        int newTop = vd.getTop() - newSize / 2;
        int newWidth = newSize;
        int newHeight = newSize;
        vd.updateBoundingRectangle(newLeft, newTop, newWidth, newHeight);
        this.invalidate();
    }

    @Override
    public void addEdge(Vertex v1, Vertex v2, VertexDirection dir) {
        Edge e = new Edge(v1, v2, dir);
        mEdges.add(e);
        mVertexEdgeMap.get(v1).add(e);
        this.invalidate();
    }

    @Override
    public Collection<VertexDrawable> getVertexDrawables() {
        return Collections.unmodifiableCollection(mVertexRenderMap.values());
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
        mVertexEdgeMap.get(e.getFromVertex()).remove(e);
        mVertexEdgeMap.get(e.getToVertex()).remove(e);
        this.invalidate();
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
        mVertexEdgeMap.get(v1).clear();
        mVertexEdgeMap.get(v2).clear();
        this.invalidate();
    }

    @Override
    public void addRedrawCallback(Runnable r) {
        mRedrawCallbacks.add(r);
    }

    private void invalidate() {
        for (Runnable r : mRedrawCallbacks) {
            r.run();
        }
    }

    @Override
    public VertexDrawable getDrawableAt(int x, int y) {
        for (VertexDrawable vd : mVertexRenderMap.values()) {
            if (vd.contains(x, y)) return vd;
        }

        return null;
    }
}
