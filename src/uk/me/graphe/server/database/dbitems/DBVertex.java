package uk.me.graphe.server.database.dbitems;

import uk.me.graphe.shared.Vertex;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class DBVertex {

    private String mName;
    
    public DBVertex () {
        
    }
    public DBVertex (Vertex vertex) {
        mName = vertex.getLabel();
    }
    
    public String getLabel (){
        return mName;
    }
}
