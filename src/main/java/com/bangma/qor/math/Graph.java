package com.bangma.qor.math;

import java.util.*;

/**
 * This is the abstract class to represent the graphs used in
 * the graph theory calculations. Its default behaviour is to
 * create a grid patter @param width wide, and @param height
 * high.
 */
public class Graph {
    private static final int INF = Integer.MAX_VALUE;
    private static final int NULL = -1;

    private final int width;
    private final int height;

    private int[] dist;
    private int[] prev;

    private Set<Integer> allNodes;
    private Map<String, Integer> allWeights;
    private Map<Integer, Set<Integer>> allNeighbors;

    /**
     * @param width width in nodes of the graph.
     * @param height height in nodes of the graph.
     */
    public Graph(int width, int height) {
        this.width      = width;
        this.height     = height;

        this.allNodes   = new HashSet<>();
        this.dist       = new int[this.width * this.height];
        this.prev       = new int[this.width * this.height];

        // assign the node ids
        for (int i = 0, l = this.width * this.height; i < l; i++) {
            this.allNodes.add(i);
        }

        this.allNeighbors   = new HashMap<>();
        this.allWeights     = new HashMap<>();
    }

    /**
     * Convert an (x,y) coordinates pair to a location on the graph.
     * @param tuple an (x,y) pair
     * @return node id
     */
    public int convertTupleToId(Position tuple) {
        return tuple.x + tuple.y*this.width;
    }

    /**
     * Convert a node id to an (x,y) coordinate pair.
     * @param id
     * @return
     */
    public Position convertIdToTuple(int id) {
        int x = id % this.width;
        return new Position(x, (id - x) / this.width);
    }

    /**
     * Returns the weight between two nodes. If the edge exists and
     * has a weight, it will return that value. It the edge exists
     * but there is no entry for weight, it will default to a weight
     * of {1}. This is for the grid based design of the project. To
     * avoid this, create the weights ahead of time. If there is no
     * neighbor, then this method returns Graph.NULL (-1).
     * @param nodeOne source node
     * @param nodeTwo neighbor node
     * @return the weight between the nodes, or Graph.NULL (-1)
     */
    public int getWeight(int nodeOne, int nodeTwo) {
        String key = graphHash(nodeOne, nodeTwo);
        if (this.allWeights.containsKey(key)) {
            return this.allWeights.get(key);
        } else if (this.allNeighbors.get(nodeOne).contains(nodeOne)) {
            this.addConnection(nodeOne, nodeTwo, 1);
            return 1;
        } else {
            return NULL;
        }
    }

    /**
     * Set the weight between two nodes.
     * @param nodeOne source node
     * @param nodeTwo destination node
     * @param weight weight to assign
     */
    public void addConnection(int nodeOne, int nodeTwo, int weight) {
        this.addNeighbor(nodeOne, nodeTwo);
        this.allWeights.put(graphHash(nodeOne, nodeTwo), weight);
    }
    /**
     * This method defaults to a basid grid pattern (like a chess board)
     * with a default weight of {1}. To avoid this functionality create
     * the edges ahead of time. If weights are already set it will not
     * create them.
     *
     * @param node the node to check
     * @return a list of existing neighbors ( or generated neighbors )
     */
    public Set<Integer> getNeighbors(int node) {
        if (this.allNeighbors.containsKey(node)) {
            return this.allNeighbors.get(node);
        } else {
            Set<Integer> neighbors = new HashSet<>();

            // invalid node
            if (node > this.width * this.height - 1 || node < 0) return neighbors;
            // node above
            if (node > this.width - 1) neighbors.add(node - this.width);
            // node below
            if (node < this.width * (this.height - 1)) neighbors.add(node + this.width);
            // node right
            if (node % this.width != this.width - 1) neighbors.add(node + 1);
            // node left
            if (node % this.width != 0) neighbors.add(node - 1);

            this.allNeighbors.put(node, neighbors);
            return neighbors;
        }
    }
    
    /**
     * Check if a there is a connection between two nodes.
     * Using ids to check with.
     * @param nodeA node to check
     * @param nodeB node to check
     * @return true if a connection exists, false otherwise
     */
    public boolean neighborExists(int nodeA, int nodeB) {
    	if (this.allNeighbors.get(nodeA) == null) getNeighbors(nodeA);
    	return this.allNeighbors.get(nodeA).contains(nodeB);
    }
    
    /**
     * disconnect two nodes if they are neighbors. This will remove
     * neighbors from both nodeA, and nodeB.
     * @param nodeA node to check
     * @param nodeB neighbor to remove
     */
    public void removeNeighbor(int nodeA, int nodeB) {
    	Set<Integer> neighbors = (this.allNeighbors.get(nodeA) == null) 
			? getNeighbors(nodeA) 
			: this.allNeighbors.get(nodeA);
			
    	neighbors.remove(nodeB);
    	this.allNeighbors.put(nodeA, neighbors);
    	neighbors = this.getNeighbors(nodeB);
    	neighbors.remove(nodeA);
    	this.allNeighbors.put(nodeB, neighbors);
    }

    /**
     * This function is used to access a hash map entry that is defined
     * by two variables.
     * @param n1 node one
     * @param n2 node two
     * @return a string hash in the format "n1,n2".
     */
    private static String graphHash(int n1, int n2) {
        return n1 + "," + n2;
    }

    /**
     * Dijkstra's Algorithm
     * See: https://en.wikipedia.org/wiki/Dijkstra's_algorithm
     * @param source starting node
     * @param destination finishing node
     */
    public void analyzeGraph(int source, int destination) {
        List<Integer> queue = new ArrayList<>();
        queue.addAll(this.allNodes);

        for (int i = 0, l = this.allNodes.size(); i < l; i++) {
            this.dist[i] = INF;
            this.prev[i] = NULL;
        }
        this.dist[source] = 0;

        while (!queue.isEmpty()) {
            int u = minInQueue(this.dist, queue);
            queue.remove(queue.indexOf(u));

            if (u == destination) return; // if we made it.

            for (int v : this.getNeighbors(u)) {
                int alt = this.dist[u] + getWeight(u, v);
                if (alt < dist[v]) {
                    dist[v] = alt;
                    prev[v] = u;
                }
            }
        }
    }

    /**
     * runs {analyzeGraph(int)} and then creates a stack
     * of nodes that represents the shortest path.
     * @param source the starting node
     * @param destination the destination to get to.
     * @return a Stack representing the path.
     */
    public Deque<Integer> findPath(int source, int destination) {
        analyzeGraph(source, destination);
        Deque<Integer> path = new ArrayDeque<>();
        int u = destination;
        while(prev[u] != NULL) {
            path.push(u);
            u = prev[u];
        }
        path.push(u);
        return path;
    }

    /**
     * returns the index of the minimum value in list
     *      (if the index is in the queue)
     * @param list to check
     * @param queue checks for item's presence in the queue
     * @return index of minimum value
     */
    public static int minInQueue(int[] list, List<Integer> queue) {
        int min = INF;
        int index = NULL;
        for (int i = 0, l = list.length; i < l; i++) {
            if (list[i] < min && queue.contains(i)) {
                min = list[i];
                index = i;
            }
        }
        return index;
    }

    /**
     * Add a neighbor node. In this implementation neighbors
     * are only ever two way, meaning there is no one way
     * paths. So when you add neighbor {nodeOne} to {nodeTwo}
     * you will also have to add neighbor {nodeTwo} to
     * {nodeOne}. Also Sets do not allow duplicate entries,
     * they are used to make sure no neighbor is entered twice.
     *
     * @param nodeOne source node
     * @param nodeTwo destination node
     */
    private void addNeighbor(int nodeOne, int nodeTwo)   {
        if (!this.allNodes.contains(nodeOne) || !this.allNodes.contains(nodeTwo)) return;

        Set<Integer> nodeOneNeighbors = this.allNeighbors.get(nodeOne);
        Set<Integer> nodeTwoNeighbors = this.allNeighbors.get(nodeTwo);

        if (nodeOneNeighbors == null) nodeOneNeighbors = new HashSet<>();
        if (nodeTwoNeighbors == null) nodeTwoNeighbors = new HashSet<>();

        nodeOneNeighbors.add(nodeTwo);
        nodeTwoNeighbors.add(nodeOne);

        this.allNeighbors.put(nodeOne, nodeOneNeighbors);
        this.allNeighbors.put(nodeTwo, nodeTwoNeighbors);

    }

    public int              getWIDTH()      { return width; }
    public int              getHEIGHT()     { return height; }
    public Set<Integer>     getAllNodes()   { return allNodes; }
}
