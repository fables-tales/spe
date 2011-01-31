package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;

@Embedded("graph")
public class DeleteNodeDB extends NodeDB {

    public DeleteNodeDB() {
        
    }
    
    public DeleteNodeDB(DBVertex v) {
        super(v);
    }

    @Override
    public boolean deletesNode(DBVertex effectedNode) {
        return effectedNode.equals(this.getNode());
    }

}
