package uk.me.graphe.client;

import uk.me.graphe.shared.Graph;
public class GraphString{ 

   
    public static String getDot(Graph graph){
        String graphString = "graph ethane {C_0 -- H_0 [type=s];C_0 -- H_1 [type=s];}";
        return graphString;
    }
    
}
