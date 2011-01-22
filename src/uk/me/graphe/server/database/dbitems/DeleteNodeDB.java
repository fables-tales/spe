package uk.me.graphe.server.database.dbitems;


import uk.me.graphe.shared.Vertex;

public class DeleteNodeDB extends NodeDB {

    public DeleteNodeDB(Vertex v) {
        super(v);
    }

    @Override
    public boolean deletesNode(Vertex effectedNode) {
        return effectedNode.equals(this.getNode());
    }

}
