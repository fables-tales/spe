package uk.me.graphe.client.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.me.graphe.client.Console;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;


public class Djikstra {
    private Map<String,ArrayList<ListItem>> matrix;
    private Node[] heap;
    private Map<String,Node> nlookup;
    private Map<String,Vertex> vlookup;
    private Map<ListItem,Edge> elookup;
    private ArrayList<Vertex> processedVertices;
    private ArrayList<Edge> processedEdges;
    private int noNodes;
    private boolean mFinished = false;
    String source, dest;
    
    public Djikstra (Graph graph, Vertex start, Vertex finish) {
        // Store the source and destination nodes
        source = start.getLabel();
        dest = finish.getLabel();
        List<Vertex> vertices = graph.getVertices();
        // Create lookup table for vertices from name
        vlookup = new HashMap<String,Vertex>();
        elookup = new HashMap<ListItem,Edge>();
        for (Vertex v : vertices)
            vlookup.put(v.getLabel(), v);
        List<Edge> edges = graph.getEdges();
        noNodes = vertices.size();
        matrix = new HashMap<String,ArrayList<ListItem>>();
        for (Edge e : edges)
            processLine(e);
        
        // Create an array of nodes (1 indexed for simplicity)
        heap = new Node[noNodes + 1];
        int i = 1;
        for (Vertex v : vertices) {
            heap[i] = new Node(v.getLabel(), i);
            nlookup.put(v.getLabel(),heap[i]);
            i++;
        }
        // Set source node to 0
        nlookup.get(source).distance(0);
        
        // Set source node at position 1
        swap (nlookup.get(source), heap[1]);
    }
    
    public boolean hasFinished() {
        return mFinished;
    }
    
    public void step() {
        if (!mFinished && noNodes > 0)
            dijkstra();
    }
    
    public void iterate() {
        while (noNodes > 0 && !mFinished)
            dijkstra();
    }
    
    public String getResult() throws Exception {
        // Print shortest path
        if (mFinished == true) {
            if (nlookup.get(dest).getDistance() != Integer.MAX_VALUE)
                return Integer.toString(nlookup.get(dest).getDistance());
            else
                return "No path exists";
        }
        else
            throw new Exception("Algorithm not done executing");
    }
    
    public List<Edge> getProcessedEdges() {
        return processedEdges;
    }
    
    public List<Vertex> getProcessedVertex() {
        return processedVertices;
    }
    private void processLine (Edge e) {
        // Initialise the adjacency matrix
        String tail = e.getFromVertex().getLabel();
        String head = e.getToVertex().getLabel();
        int weight = e.getWeight();
        ArrayList<ListItem> list = matrix.get(tail);
        ListItem listitem = new ListItem(head,tail,weight);
        if (list == null) {
            list = new ArrayList<ListItem>();
            list.add(listitem);
            matrix.put(tail, list);
        } else
            matrix.get(tail).add(listitem);
        elookup.put(listitem, e);
    }
    
    private void dijkstra() {
        Node currentNode;
        currentNode = extractmin();
        relaxadj (currentNode);
        processedVertices.add(vlookup.get(currentNode.getId()));
        if (currentNode.getId().equalsIgnoreCase(dest))
            mFinished = true;
    }

    private void relaxadj (Node node) {
        int distance;
        Node head;
        ArrayList<ListItem> edges = matrix.get(node.getId());
        // If edges is empty, then no edges leading from the node
        if (edges == null)
            return;
        for (int j = 0; j < edges.size(); j++) {;
            // For any edge adjacent to the current node 
            // Distance via new node
            ListItem e = edges.get(j);
            distance = node.getDistance() + e.getWeight();
            // Find the head in the array
            head = nlookup.get(e.getHead());
            if (head == null) { 
                return;
            }
            // If the distance to this node is short via new node, update the path to reflect this
            if (head.getDistance() > distance) {;
                decreasekey (head, distance);
            }
            // Mark the edge as processed
            processedEdges.add(elookup.get(e));
        }
    }
    
    private void swap (Node node1, Node node2) {
        Node temp = node1;
        int loc1 = node1.getLocation();
        int loc2 = node2.getLocation();
        heap[loc1] = node2;
        heap[loc1].location(loc1);
        heap[loc2] = temp;
        heap[loc2].location(loc2);
    }
    
    private void minheapify (Node i) {
        int loc = i.getLocation();
        // Get position of child nodes
        int left = 2*loc;
        int right = 2*loc + 1;
        Node smallest;
        // Check 
        if (left <= noNodes && heap[left].getDistance() < i.getDistance())
            smallest = heap[left];
        else
            smallest = i;
        if (right <= noNodes && heap[right].getDistance() < smallest.getDistance())
            smallest = heap[right];
        if (smallest.getId() != i.getId()) {
            swap (smallest, i);
            minheapify (i);
        }
    }
    
    private Node extractmin() {
        Node min = heap[1];
        swap(heap[1], heap[noNodes]);
        noNodes--;
        minheapify(heap[1]);
        return min;
    }
    
    private void decreasekey (Node node, int distance) {
        
        if (node.getDistance() < distance) {
            Console.log("Key is larger than current value, cannot decrease");
            return;
        }
        heap[node.getLocation()].distance(distance);
        // While i is not the root and is smaller than parents swap
        bubble(node);
    }
    
    private Node getparent (Node child) {
        int location = child.getLocation();
        if (location % 2 == 0)
            return heap[location/2];
        else
            return heap[(location-1)/2];
        
    }
    
    private void bubble (Node child) {
        // If child is root return
        if (child.getLocation() == 1)
            return;
        Node parent = getparent(child);
        if (parent.getDistance() > child.getDistance()) {
            swap(child, parent);
            bubble(child);
        }
    }
}
