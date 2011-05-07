package uk.me.graphe.client;

import java.util.HashMap;
import java.util.List;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;

public class GraphString{ 
    
    
    /**
     * Adds to the graph manager nodes,edges contained in the dot language code
     * @param graphManager
     * @param graphCode
     * @return
     *      false if error in the DOT code
     */
    public static boolean addDot(GraphManager2d graphManager,String graphCode){

        HashMap<String, Vertex> verticesMap = new HashMap<String, Vertex>();
        graphCode = graphCode.replaceAll("\\r|\\n", "");
        if(graphCode.indexOf("{") < 0)return false;
        if(graphCode.indexOf("}") < 0)return false;
        String betweenCurly = graphCode.split("{")[1].split("}")[0];
        String[] lines = betweenCurly.split(";");
        for(int i = 0;i<lines.length;i++){
            String line = lines[i].trim();
            if(line.indexOf("[") > 0){
                line = line.split("\\[")[0].trim();
            }
            if(line.indexOf("->") > 0){
                String[] nodes = line.split("->");
                if(nodes.length!=2)return false;
                if(!verticesMap.containsKey(nodes[0]))verticesMap.put(nodes[0],new Vertex(nodes[0]));
                if(!verticesMap.containsKey(nodes[1]))verticesMap.put(nodes[1],new Vertex(nodes[1]));
                graphManager.addEdge(verticesMap.get(nodes[0].trim()), 
                        verticesMap.get(nodes[1].trim()), VertexDirection.fromTo, 1);
            }else{
                // Add node
                Vertex v = new Vertex(line);
                verticesMap.put(line,v);
                int randomX = Random.nextInt(500)+50;
                int randomY = Random.nextInt(200)+50;
                graphManager.addVertex(v, randomX, randomY, Graphemeui.VERTEX_SIZE);
            }
        }
        return true;
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
