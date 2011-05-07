package uk.me.graphe.client.algorithms;

import java.util.List;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;

public class ShortestPathDjikstras implements Algorithm {

    private Graph mGraph = null;
    private Djikstra mDjikstra = null;
    
    @Override
    public List<Edge> getHilightedEdges() {  
        return mDjikstra.getProcessedEdges();
    }
    
    @Override
    public boolean hasFinished() {
        return mDjikstra.hasFinished();
    }

    @Override
    public List<Vertex> getHilightedVerticies() {
        return mDjikstra.getProcessedVertex();
    }

    @Override
    public String getResult() throws Exception {
        return mDjikstra.getResult();
    }

    public void initialise(Graph g, Vertex start, Vertex end) {
        mGraph = g;
        mDjikstra = new Djikstra(g,start,end);
    }
    
    @Override
    public void initialise(Graph g) {
        // No point in initialise graph without starting parameters
    }

    @Override
    public void step() {
        mDjikstra.step();        
    }

    @Override
    public void stepAll() {
        mDjikstra.iterate();
    }

}
