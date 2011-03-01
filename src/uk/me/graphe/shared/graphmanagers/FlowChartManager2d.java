package uk.me.graphe.shared.graphmanagers;

import java.util.Collection;

import uk.me.graphe.client.EdgeDrawable;
import uk.me.graphe.client.VertexDrawable;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;

public class FlowChartManager2d extends GraphManager2dImpl implements StyleGraphManager2d {

    @Override
    public void setVertexStyle(String label, int style) {
        this.getVertexDrawable(label).setStyle(style);
    }

    @Override
    public int getVertexStyle(String label) {
        return this.getVertexDrawable(label).getStyle();
    }
}
