package uk.me.graphe.client.algorithms;

import java.util.List;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;

public interface Algorithm {
    public void initialise(Graph g);

    /**
     * perform a single step of the algorithm
     */
    public void step();

    public List<Edge> getHilightedEdges();

    public List<Vertex> getHilightedVerticies();
    
    public boolean hasFinished();

    /**
     * perform all the steps of the algorithm until it finishes
     */
    public void stepAll();

    /**
     * gets the result from the algorithm, should thrown an exception if the
     * algorithm is not done executing
     * 
     * @return a string representing the result of the algorithm
     * @throws Exception 
     */
    public String getResult() throws Exception;
}
