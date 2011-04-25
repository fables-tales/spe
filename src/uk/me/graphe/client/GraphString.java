package uk.me.graphe.client;

import java.util.List;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;

public class GraphString{ 
    
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
            switch (vertexStyle) {
            case VertexDrawable.STROKED_TERM_STYLE:
                graphString += "[label=<<FONT COLOR=\"black\">"+label+
                    "</FONT>> fillcolor=gray, style=\"rounded,filled\", shape=box]";
                break;
            case VertexDrawable.STROKED_SQUARE_STYLE:
                graphString += "[label=<<FONT COLOR=\"black\">"+label+
                    "</FONT>> fillcolor=gray, style=\"filled\", shape=box]";
                break;
            case VertexDrawable.STROKED_DIAMOND_STYLE:
                graphString += "[label=<<FONT COLOR=\"black\">"+label+
                    "</FONT>> fillcolor=gray, style=\"filled\", shape=diamond]";
                break;
            case VertexDrawable.COLORED_FILLED_CIRCLE:
                graphString += "[fillcolor=black, style=\"filled\", shape=circle]";
                break; 
            default:
                graphString += "[label=<<FONT COLOR=\"white\">"+label+
                    "</FONT>> fillcolor=black, style=\"filled\", shape=circle]";
                break;
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
