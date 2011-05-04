package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import uk.me.graphe.client.communications.ReceiveNotificationRunner;
import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.GraphTransform;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.ChatMessage;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.MessageFactory;
import uk.me.graphe.shared.messages.RequestGraphMessage;
import uk.me.graphe.shared.messages.StateIdMessage;
import uk.me.graphe.shared.messages.operations.AddEdgeOperation;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.DeleteEdgeOperation;
import uk.me.graphe.shared.messages.operations.DeleteNodeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class ClientOT {

    private static ClientOT sInstance = null;

    private ServerChannel mSc;
    private GraphManager2d mGraph;

    private Queue<GraphOperation> mUnsentOps = new LinkedList<GraphOperation>();
    private List<GraphOperation> mSentUnAcked = new ArrayList<GraphOperation>();
    private int mServerStateId = -1;
    private List<GraphOperation> mServerOperations = new ArrayList<GraphOperation>();
    private LocalStore mStore = LocalStoreFactory.newInstance();
    private StorePackage mInfo;

    private boolean mInited = true;
    private boolean mServer = false;

    public ClientOT() {
        mSc = ServerChannel.getInstance();
        mSc.addReceiveNotification(new ReceiveNotificationRunner() {

            @Override
            public void run(String s) {
                ClientOT.this.handleMessages(s);
            }
        });

         //Timer waits to see if server connection has been established
        new Timer() {
            @Override
            public void run() {
                /*
                 * Options:
                 * 1) No Server connection + any ops
                 * 2) Server connection + no local
                 * 3) Server connection + local
                 */
                if (mServer == false) {
                    Console.log("No server connection, offline mode enabled");
                    mInfo = mStore.getInformation();
                    if(Window.confirm("A previous graph has been detected, press okay to load")) {
                        List<GraphOperation> ops = mInfo.getServer();
                        if (!ops.isEmpty()) {
                            for (GraphOperation op : ops) {
                                op.applyTo(mGraph);
                            }
                        }
                        ops = mInfo.getLocal();
                        if (!ops.isEmpty()) {
                            for (GraphOperation op : ops) {
                                op.applyTo(mGraph);
                                mUnsentOps.add(op);
                            }
                        }                     
                    }
                    else
                        mStore.setup(1, null, null);
                }
                else {
                    Console.log("Server connection established");
                    List<GraphOperation> ops = mInfo.getLocal();
                    if (!ops.isEmpty() &&
                       Window.confirm("Offline operations have been detected, press okay to load these operations or cancel to discard them")) {
                       for (GraphOperation op : ops) {
                           op.applyTo(mGraph);
                           mUnsentOps.add(op);
                       }
                    }
                    else
                        mStore.setup(1, null, null);
                    // Store the graph operations retrieve from server at load
//                    for (GraphOperation op : mServerOperations) {
//                        CompositeOperation comp = (CompositeOperation)op;
//                        for (GraphOperation o : comp.asIndividualOperations()) {
//                            if (!o.isNoOperation())
//                                mStore.store(o, true);
//                        }
//                    } 
                }
            }
        }.schedule(1500);
        
        new Timer() {

            @Override
            public void run() {
                mSc.send(new RequestGraphMessage(1, 0).toJson());
                Console.log("sent ogm");
                Window.alert("SENT OGM");
                mServer = true;
                mStore.resetServer();
                mInfo = mStore.getInformation();
                new Timer() {

                    @Override
                    public void run() {
                        ClientOT.this.pumpOut();
                    }
                }.scheduleRepeating(100);

            }
        }.schedule(1000);

    }

    protected void pumpOut() {
        GraphOperation o = mUnsentOps.poll();
        if (mInited && o != null) {
            mSc.send(o.toJson());
            mSentUnAcked.add(o);
        }
    }

    protected void handleMessages(String s) {
        List<JSONObject> objs = this.readObjects(s);
        List<Message> messages = this.parseMessages(objs);
        Console.log("handling messages");
        for (Message m : messages) {
            Console.log("got message" + m.getMessage());
            if (m.isOperation()) {
                CompositeOperation graph = (CompositeOperation) messages.get(0);
                List<GraphOperation> myComposite = new ArrayList<GraphOperation>(mSentUnAcked);
                CompositeOperation me = new CompositeOperation(myComposite);
                Console.log("message is:" + m.toJson());
                Console.log("transforming against: " + me.toJson());
                GraphOperation o = GraphTransform.transform(graph, me);
                Console.log("transformed message" + m.toJson());
                Console.log("graph current state is v: " + mGraph.getVertexDrawables().size()
                        + " e:" + mGraph.getEdgeDrawables().size());
                o.applyTo(mGraph);
                Console.log("after operation current state is v: "
                        + mGraph.getVertexDrawables().size() + " e:"
                        + mGraph.getEdgeDrawables().size());
                mServerOperations.add(o);
                mSentUnAcked.clear();
                CompositeOperation co = (CompositeOperation)o;
                for (GraphOperation op : co.asIndividualOperations()) {
                    if (!op.isNoOperation()) {
                        mStore.store(op, true);
                    }
                }
                mStore.Ack();
            } else if (m.getMessage().equals(new StateIdMessage(0, 0).getMessage())) {
                mServerStateId = ((StateIdMessage) m).getState();
            } else if (m.getMessage().equals("chat")) {
            	//show message here
            	ChatMessage cm = (ChatMessage)m;
            	Chat.getInstance().onReceiveMessage(cm);
            }

        }
        Console.log("I have this many vertices:");
        Console.log("" + mGraph.getVertexDrawables().size());

        Console.log("done handling messages");

    }

    private List<Message> parseMessages(List<JSONObject> objs) {
        try {
            return MessageFactory.makeOperationsFromJson(objs);
        } catch (JSONException e) {

        }
        return null;
    }

    private List<JSONObject> readObjects(String s) {
        List<JSONObject> objs = new ArrayList<JSONObject>();
        for (String message : s.split("\0")) {
            try {
                objs.add(JSONImplHolder.make(message));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return objs;
    }

    public static ClientOT getInstance() {
        if (sInstance == null) sInstance = new ClientOT();
        return sInstance;
    }

    public void setOperatingGraph(GraphManager2d graphManager) {
        Console.log("operating graph set!");
        mGraph = graphManager;
    }

    public void notifyRemoveEdge(Edge edge) {
    	DeleteEdgeOperation newop = new DeleteEdgeOperation((edge));
        mUnsentOps.add(newop);
        mStore.store(newop, false);
    }

    public void notifyRemoveVertex(Vertex vertex) {
    	DeleteNodeOperation newop = new DeleteNodeOperation(vertex);
        mUnsentOps.add(newop);
        mStore.store(newop, false);
    }

    public void notifyAddEdge(Vertex vertex, Vertex vertex2, VertexDirection fromto) {
    	AddEdgeOperation newop = new AddEdgeOperation(new Edge(vertex, vertex2, fromto));
        mUnsentOps.add(newop);
    	mStore.store(newop, false);

    }

    public void notifyAddVertex(Vertex v, int i, int j, int vertexSize) {
        Console.log("notified of adding vertex:" + v.getLabel());
        AddNodeOperation newop = new AddNodeOperation(v, i, j);
        mUnsentOps.add(newop);
        mStore.store(newop, false);

    }

}
