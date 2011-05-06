package uk.me.graphe.client;

import java.util.List;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;

public class GraphString{ 
    
    
    /**
     * Adds to the graph manager nodes,edges contained in the dot language code
     * @param graphManager
     * @param graphCode
     */
    public static void addDot(GraphManager2d graphManager,String graphCode){
        Vertex v = new Vertex("test");
        graphManager.addVertex(v, 200, 200, Graphemeui.VERTEX_SIZE);
    }
    
    /**
     * returns the "DOT language" (http://en.wikipedia.org/wiki/DOT_language) 
     * representation of the graph
     *
     * @param graphManager
     *            the graph manager containing the graph
     * @param graphName
     *            name to use in the DOT code
     * @param directed
     *            true if the graph to be coded is directed
     * @param showWeights
     *            true if the graph should display edge waits
     */
    public static String getDot(GraphManager2d graphManager, String graphName, boolean directed, boolean showWeights){
        
        VertexDrawable vertexToDraw;
        String graphString;
        List<Vertex> mVertices;
        List<Edge> mEdges;
        int vertexStyle;
        String label;
        String arrow = "--";
        mVertices = graphManager.getUnderlyingGraph().getVertices();
        mEdges = graphManager.getUnderlyingGraph().getEdges();
        
        if(directed)graphString = "digraph "+graphName+" {";
        else graphString = "graph "+graphName+" {";
        
        // Set up the styles for the nodes
        for (Vertex thisVertex : mVertices) {
            
            vertexToDraw = graphManager.getVertexDrawable(thisVertex.getLabel());
            vertexStyle = vertexToDraw.getStyle();
            label = vertexToDraw.getLabel();
            graphString += cleanLabel(vertexToDraw.getLabel())+" ";

            if (vertexStyle == VertexDrawable.STROKED_TERM_STYLE){
                graphString += "[label=<<FONT COLOR=\"black\">"+label+
                    "</FONT>> fillcolor=gray, style=\"rounded,filled\", shape=box]";
            }else if (vertexStyle == VertexDrawable.STROKED_SQUARE_STYLE){
                graphString += "[label=<<FONT COLOR=\"black\">"+label+
                    "</FONT>> fillcolor=gray, style=\"filled\", shape=box]";
            }else if (vertexStyle == VertexDrawable.STROKED_DIAMOND_STYLE){
                graphString += "[label=<<FONT COLOR=\"black\">"+label+
                    "</FONT>> fillcolor=gray, style=\"filled\", shape=diamond]";
            }else if (vertexStyle == VertexDrawable.COLORED_FILLED_CIRCLE){
                graphString += "[fillcolor=black, style=\"filled\", shape=circle]";
            }else{
                graphString += "[label=<<FONT COLOR=\"white\">"+label+
                    "</FONT>> fillcolor=black, style=\"filled\", shape=circle]";
            }
            graphString += ";";
        }
        
        // Do the edges between the nodes
        if(directed) arrow = "->";
        for (Edge thisEdge : mEdges) {
            graphString += cleanLabel(thisEdge.getFromVertex().getLabel())+" "+arrow+" "+
                cleanLabel(thisEdge.getToVertex().getLabel());
            if(showWeights)graphString += "[label=\""+thisEdge.getWeight()+"\"]";
            graphString += ";";
        }
        
        graphString+="}";
        
        return graphString;
    }
    
    private static String cleanLabel(String label){
        label = label.replaceAll(" ", "_");
        return label;
    }
    
    
}
