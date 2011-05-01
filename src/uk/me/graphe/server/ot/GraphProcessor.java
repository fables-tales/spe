package uk.me.graphe.server.ot;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import uk.me.graphe.server.Client;
import uk.me.graphe.server.ClientManager;
import uk.me.graphe.server.ClientMessageSender;
import uk.me.graphe.server.DataManager;
import uk.me.graphe.shared.GraphTransform;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.messages.StateIdMessage;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;

import com.google.gwt.dev.util.Pair;

public class GraphProcessor extends Thread {

    private static GraphProcessor sInstance = null;;

    public static GraphProcessor getInstance() {
        if (sInstance == null) sInstance = new GraphProcessor();
        return sInstance;
    }

    private BlockingQueue<Pair<Client, GraphOperation>> mQueuedOperations = new ArrayBlockingQueue<Pair<Client, GraphOperation>>(
            1024);

    private boolean mShutdown = false;

    private GraphProcessor() {}

    @Override
    public void run() {
        while (!mShutdown) {
            try {
                Pair<Client, GraphOperation> op = mQueuedOperations.take();
                Client c = op.left;
                GraphOperation operation = op.right;
                int mGraphId = c.getCurrentGraphId();

                // get the graph and get the history delta
                System.err.println("bees" + mGraphId);

                OTGraphManager2d graph = DataManager.getGraph(mGraphId);
                System.err.println(graph);
                CompositeOperation historyDelta = graph.getOperationDelta(c
                        .getCurrentStateId());

                // transform and apply the new operation
                GraphOperation newOp = GraphTransform.transform(operation,
                        historyDelta);
                System.err.println("applying: " +  newOp.toString());
                graph.applyOperation(newOp);
                System.err.println("nodes:" + graph.getVertexDrawables().size());
                System.err.println("edges:" + graph.getEdgeDrawables().size());

                // send update to client
                int serverStateId = graph.getStateId();
                ClientMessageSender.getInstance().sendMessage(c, historyDelta);
                ClientMessageSender.getInstance()
                        .sendMessage(
                                c,
                                new StateIdMessage(c.getCurrentGraphId(),
                                        serverStateId));
                c.updateStateId(graph.getStateId());
                DataManager.save(graph);
                List<Client> otherClients = ClientManager.getInstance()
                        .clientsForGraph(c.getCurrentGraphId());
                otherClients.remove(c);
                for (Client cl : otherClients) {
                    int state = cl.getCurrentStateId();
                    System.err.println("updating other clients");
                    System.err.println(state);
                    CompositeOperation delta = graph.getOperationDelta(state);
                    System.err.println(delta.asIndividualOperations());
                    ClientMessageSender.getInstance().sendMessage(cl, delta);
                    ClientMessageSender.getInstance().sendMessage(
                            cl,
                            new StateIdMessage(graph.getGraphId(),
                                    serverStateId));
                }

            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void submit(Client c, GraphOperation o) {
        Pair<Client, GraphOperation> pair = Pair.create(c, o);
        try {
            mQueuedOperations.put(pair);
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    public void shutDown() {
        mShutdown = true;
        this.interrupt();
    }

}
