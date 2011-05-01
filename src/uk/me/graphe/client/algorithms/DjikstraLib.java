package uk.me.graphe.client.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class DjikstraLib {
  /*  public static void main(String[] args) throws IOException {
        if (args.length >= 3) {
            String file = args[0];
            int nodeFrom = Integer.parseInt(args[1]) - 1;
            int nodeTo = Integer.parseInt(args[2]) - 1;
            int[] nodesEdges, edges, backwardsEdges;
            int shortestPath;
            int[][] adjacencyLists, backwardsAdjacencyLists;

            // read the file
            int[][] results = readFile(file);
            // parse out counts of nodes and edges
            nodesEdges = results[0];
            // parse the edges for the entire graph
            edges = results[1];
            backwardsEdges = edgeFilp(edges);
            // compute the adjacency lists for each node
            adjacencyLists = makeAdjacencyLists(edges, nodesEdges[0]);
            backwardsAdjacencyLists = makeAdjacencyLists(backwardsEdges,
                    nodesEdges[0]);

            // compute the shortest path
            shortestPath = shortestDjikstrasHeap(edges, backwardsEdges,
                    nodesEdges, nodeFrom, nodeTo, adjacencyLists,
                    backwardsAdjacencyLists);
            System.out.println(shortestPath);
        } else {
            System.err.println("wrong number of args");
            System.exit(1);
        }

    }

    private static int[] edgeFilp(int[] edges) {
        int[] newEdges = new int[edges.length];
        for (int i = 2; i < edges.length; i += 3) {
            newEdges[i - 2] = edges[i - 1];
            newEdges[i - 1] = edges[i - 2];
            newEdges[i] = edges[i];
        }

        return newEdges;

    }

    private static int[] getNearestNode(int numNodes, int[] dist) {
        // because we're using a heap priority queue, the nearest node is the
        // zeroth node but we need to remove it and heapify
        int node = dist[0];
        int weight = dist[1];

        if (numNodes-- > 1) {
            int n2 = 2 * numNodes;
            dist[0] = dist[n2];
            dist[1] = dist[n2 + 1];
            heapify(dist, 0, numNodes - 1);
        } else {
            dist[0] = -1;
            dist[1] = Integer.MIN_VALUE;
        }

        return new int[] { node, weight };
    }

    private static void heapify(int[] dist, int i, int j) {
        // my eyes are on fire, trying to heapify a 0 indexed array of pairs
        // where the second element is the key. Why has satan unleashed this
        // stampede of ponies on my face
        int min = i;
        int base = 2 * i;
        int left = base + 1;
        int right = base + 2;
        if (left < j && dist[2 * left + 1] < dist[base + 1]) min = left;
        else if (right < j && dist[2 * right + 1] < dist[2 * min + 1])
            min = right;

        if (min != i) {
            swapIndicies(dist, base, min);
            heapify(dist, min, j);
        }
    }

    private static void swapIndicies(int distances[], int i, int min) {
        int tmp;
        tmp = distances[i];
        distances[i] = distances[2 * min];
        distances[2 * min] = tmp;

        tmp = distances[i + 1];
        distances[i + 1] = distances[2 * min + 1];
        distances[2 * min + 1] = tmp;
    }

    private static void initDistance(int nodeFrom, int[] distQueue, int[] dist) {
        // fill all the dsitances of all the nodes to infinity
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(distQueue, Integer.MAX_VALUE);
        // distance to start node is zero
        dist[nodeFrom] = 0;

        // fill the queue of distances with the nodes and their weights

        // construct a binary heap, in this case put the start node
        // at the root and set all children to infintity
        distQueue[0] = nodeFrom;
        distQueue[1] = 0;
    }

    private static void initialiseNeighbors(int[] edges, int[][] neighbors,
            int i, int fromEdge) {
        neighbors[fromEdge] = new int[3];
        neighbors[fromEdge][0] = 1;
        neighbors[fromEdge][1] = edges[i + 1];
        neighbors[fromEdge][2] = edges[i + 2];
    }

    private static int[][] makeAdjacencyLists(int[] edges, int nNodes) {
        int[][] neighbors = new int[nNodes][];

        // the zeroth index for neighbors is reserved for how many outgoing
        // neighbors
        // each node has
        for (int i = 0; i < edges.length; i += 3) {
            int fromEdge = edges[i];
            if (neighbors[fromEdge] == null) {
                initialiseNeighbors(edges, neighbors, i, fromEdge);
            } else {
                // resize if we're too small
                int[] nfe = neighbors[fromEdge];
                if (nfe.length >= nfe[0] * 2 - 1) {
                    resizeArray(neighbors, fromEdge, nfe);
                    nfe = neighbors[fromEdge];
                }

                updateNeighbor(edges, i, nfe);

            }

        }

        // if we've got uninitialised neighbor lists because a node has no
        // neighbors, initialise them
        for (int i = 0; i < nNodes; i++) {
            if (neighbors[i] == null) {
                neighbors[i] = new int[0];
            }
        }

        return neighbors;
    }

    private static int[][] readFile(String file) throws FileNotFoundException,
            IOException {
        // determine the number of edges
        File f = new File(file);
        BufferedReader br = new BufferedReader(new FileReader(f));
        int[] edgeNodes = new int[2];
        int[] edges = null;
        final int firstDigit = 2;
        int i = 0;

        while (br.ready()) {
            String s = br.readLine();
            if (s.charAt(0) == 'p') {
                edges = getEdgeCount(edgeNodes, s);
            } else if (s.charAt(0) == 'a') {
                i = addEdge(edges, s, firstDigit, i);
            }

        }

        return new int[][] { edgeNodes, edges };
    }

    private static int addEdge(int[] edges, String s, final int firstDigit,
            int i) {
        String edgeRepr = s.trim();
        int space1 = edgeRepr.indexOf(' ', firstDigit);
        int space2 = edgeRepr.indexOf(' ', space1 + 1);
        edges[i] = Integer.parseInt(edgeRepr.substring(firstDigit, space1)) - 1;
        edges[i + 1] = Integer.parseInt(edgeRepr.substring(space1 + 1, space2)) - 1;
        edges[i + 2] = Integer.parseInt(edgeRepr.substring(space2 + 1, edgeRepr
                .length()));
        i += 3;
        return i;
    }

    private static int[] getEdgeCount(int[] edgeNodes, String s2) {
        int[] edges;
        String s = s2.trim();
        int space1 = s.indexOf(" ");
        int space2 = s.indexOf(" ", space1 + 1);
        int space3 = s.indexOf(" ", space2 + 1);
        edgeNodes[0] = Integer.parseInt(s.substring(space2 + 1, space3));
        edgeNodes[1] = Integer.parseInt(s.substring(space3 + 1, s.length()));
        edges = new int[edgeNodes[1] * 3];
        return edges;
    }

    private static void resizeArray(int[][] neighbors, int fromEdge, int[] nfe) {
        int nfeLength = nfe.length;
        int[] newArray = new int[nfeLength * 2 + 1];
        // copy the array into the bigger one
        for (int j = 0; j < nfeLength; j++) {
            newArray[j] = nfe[j];
        }

        neighbors[fromEdge] = newArray;
    }

    private static int shortestDjikstrasHeap(int[] edges, int[] backwardsEdges,
            int[] nodesEdges, int nodeFrom, int nodeTo, int[][] adjacencyLists,
            int[][] backwardsAdjacencyLists) {

        int numNodes = nodesEdges[0];
        int[] fDistQueue = new int[numNodes * 2];
        int[] fDist = new int[numNodes];
        int[] fPrevious = new int[numNodes];
        int fQueueSize = 1;
        int[] bDistQueue = new int[numNodes * 2];
        int[] bDist = new int[numNodes];
        int[] bPrevious = new int[numNodes];
        int bQueueSize = 1;
        // List<Integer> fSettled = new ArrayList<Integer>();
        // List<Integer> bSettled = new ArrayList<Integer>();

        initDistance(nodeFrom, fDistQueue, fDist);
        initDistance(nodeTo, bDistQueue, bDist);

        // fill the array of previous values to uninitialised
        Arrays.fill(fPrevious, -1);

        int firstSettled = -1;

        while (fQueueSize != 0 && bQueueSize != 0 && firstSettled == -1) {
            // find the nearest node, queueSize-- because this function removes
            // one item from the queue. nearestnode is a pair of node,weight
            int[] fNearestNode = getNearestNode(fQueueSize--, fDistQueue);
            // get all the neighbors of the nearest node
            int[] fNeighbors = adjacencyLists[fNearestNode[0]];

            fQueueSize = updateNeighbors(fDistQueue, fDist, fPrevious,
                    fQueueSize, fNearestNode, fNeighbors);
            int[] bNearestNode = getNearestNode(bQueueSize--, bDistQueue);
            // get all the neighbors of the nearest node
            int[] bNeighbors = adjacencyLists[bNearestNode[0]];

            bQueueSize = updateNeighbors(bDistQueue, bDist, bPrevious,
                    bQueueSize, bNearestNode, bNeighbors);

            if (fNearestNode[0] == bNearestNode[0])
                firstSettled = fNearestNode[0];

        }

        return fDist[firstSettled] + bDist[firstSettled];
    }

    private static int updateNeighbors(int[] distQueue, int[] dist,
            int[] previous, int queueSize, int[] nearestNode, int[] neighbors) {
        int alt;
        int toNode, weight, nLength = (neighbors[0]) * 2;

        for (int i = 1; i < nLength; i += 2) {
            // System.out.println(neighbors[i]);
            toNode = neighbors[i];
            weight = neighbors[i + 1];
            alt = nearestNode[1] + weight;
            queueSize = updateDistance(distQueue, dist, previous, alt,
                    nearestNode, toNode, queueSize);
        }
        return queueSize;
    }

    private static int updateDistance(int[] distQueue, int[] dist,
            int[] previous, int alt, int[] nearestNode, int toNode,
            int queueSize) {

        // if we're shorter
        if (alt < dist[toNode]) {
            enqueue(toNode, alt, distQueue, queueSize++);
            dist[toNode] = alt;
            previous[toNode] = nearestNode[0];
        }

        return queueSize;
    }

    private static void enqueue(int toNode, int alt, int[] distQueue, int nNodes) {
        distQueue[nNodes * 2] = toNode;
        distQueue[nNodes * 2 + 1] = Integer.MIN_VALUE;
        increaseKey(distQueue, nNodes, alt);

    }

    private static void increaseKey(int[] distQueue, int i, int alt) {
        distQueue[2 * i + 1] = alt;
        int parentIndex = i & (~1);
        while (i > 0 && distQueue[parentIndex + 1] > distQueue[2 * i + 1]) {
            // swap the items around
            alt = distQueue[parentIndex];
            distQueue[parentIndex] = distQueue[2 * i];
            distQueue[2 * i] = alt;
            // swap the weights around
            alt = distQueue[parentIndex + 1];
            distQueue[parentIndex + 1] = distQueue[2 * i + 1];
            distQueue[2 * i + 1] = alt;

            // recompute indicies
            i = parentIndex;
            parentIndex = i & (~1);
        }
    }

    private static void updateNeighbor(int[] edges, int i, int[] nfe) {
        // increment the count of neighbors, and put the "to" edge in
        // the neighbor, this also puts the weight in the neighbors
        // array
        nfe[nfe[0] * 2 + 1] = edges[i + 1];
        nfe[nfe[0] * 2 + 2] = edges[i + 2];
        nfe[0] += 1;
    }*/

}
