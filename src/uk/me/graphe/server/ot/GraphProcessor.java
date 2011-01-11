package uk.me.graphe.server.ot;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.google.gwt.dev.util.Pair;



import uk.me.graphe.graphmanagers.OTGraphManager2d;
import uk.me.graphe.server.Client;
import uk.me.graphe.server.ClientMessageSender;
import uk.me.graphe.server.DataManager;
import uk.me.graphe.server.messages.UpdateStateIdMessage;
import uk.me.graphe.server.messages.operations.CompositeOperation;
import uk.me.graphe.server.messages.operations.GraphOperation;

public class GraphProcessor extends Thread {

    private BlockingQueue<Pair<Client, GraphOperation>> mQueuedOperations = new ArrayBlockingQueue<Pair<Client, GraphOperation>>(
            1024);
    
    private boolean mShutdown = false;

    public void submit(Client c, GraphOperation o) {
        Pair<Client, GraphOperation> pair = Pair.create(c, o);
        try {
            mQueuedOperations.put(pair);
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    @Override
    public void run() {
        while (!mShutdown) {
            try {
                Pair<Client, GraphOperation> op = mQueuedOperations.take();
                Client c = op.left;
                GraphOperation operation = op.right;
                int mGraphId = c.getCurrentGraphId();
                OTGraphManager2d graph = DataManager.getGraph(mGraphId);
                CompositeOperation historyDelta = graph.getOperationDelta(operation.getHistoryId());
                GraphOperation newOp = GraphTransform.transform(operation, historyDelta);
                graph.applyOperation(newOp);
                int serverStateId = graph.getStateId();
                ClientMessageSender.getInstance().sendMessage(c, new UpdateStateIdMessage(serverStateId));
                DataManager.save(graph);
            } catch (InterruptedException e) {
                throw new Error(e);
            }
        }
    }

}
