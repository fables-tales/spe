package uk.me.graphe.server.database.dbitems;

import uk.me.graphe.shared.Vertex;

import com.google.code.morphia.annotations.Entity;

@Entity
public class DBVertex {

    String mName;
    
    public DBVertex (Vertex vertex) {
        mName = vertex.getLabel();
    }
    
    public String getName (){
        return mName;
    }
}
