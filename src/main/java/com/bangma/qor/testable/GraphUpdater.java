package com.bangma.qor.testable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.bangma.qor.math.Graph;
import com.bangma.qor.math.Position;
import com.bangma.qor.math.WallVector;

/**
 * A testable utility for updating the graph with walls for the gameboard.
 * Also contains all logic regarding the ability to place walls.
 */
public class GraphUpdater {
	private Graph graph;
	private List<WallVector> walls;
	
	private Set<Integer> hiddenConnections;
	private List<Integer> hiddenInwardConnections;
	private int hiddenNode = -1;
	
	/* This is used to indicate that a connection is only unavailable because a player 
	 * is currently at that position. This is so when makeAvailable() is called it does
	 * not override a wall placement and allow a player to move there. makeAvailable() only
	 * changes connections that are PLAYER_INDICATOR to 1 (default, passable). makeUnavailable()
	 * only changes 1 (default) connections to PLAYER_INDICATOR. */
	public static final int PLAYER_INDICATOR = Graph.INF - 1;
	
	/**
	 * Create a new instance of GraphUpdater.
	 * @param graph the graph to manage.
	 */
	public GraphUpdater(Graph graph) {
		this.graph = graph;
		walls = new ArrayList<>();
		hiddenConnections = null;
		hiddenInwardConnections = null;
	}
	
	/**
	 * Validate whether a legally placeable wall is going to overlap another wall.
	 * This function does not check if a wall is legal, valid walls must be given
	 * for accurate results.
	 * 
	 * legal : 0,1
	 * illegal : 0,2 1,0 etc. 
	 * 
	 * @param newWall
	 * @return
	 */
    public boolean wallAllowed(WallVector newWall) {
    	// easy out, if the walls list is empty then you can always place a wall
    	if (walls.isEmpty()) return true;
    	
    	// makes the walls-on-same-axis check cleaner
    	int shifter = (newWall.o == 'v') ? graph.getWidth() : 1;
    	
    	// iterate through all the previously placed walls to check for possible issues.
    	for (WallVector w : this.walls) {

    		// if two walls are the same they are by definition overlapping
    		if (w.equals(newWall)) return false;
    		
    		// if the walls lie on the same axis, check if they are the same but moved over one square (either direction)
    		if ( w.o == newWall.o && (w.equals(newWall.sub(shifter)) || w.equals(newWall.add(shifter))) ) return false;
    		
    		// this is one equation : if HorizontalWall(b) == VerticalWall(a) then they are overlapping
    		if (newWall.o == 'h' && w.o == 'v' && newWall.b == w.a) return false;
    		if (newWall.o == 'v' && w.o == 'h' && newWall.a == w.b) return false;
    	}

    	// new wall violates no rules and can be placed.
    	return true;
    }
	
    /**
     * Remove all of a node's neighbors from the graph, so Dijkstra's won't move there.
     * With the ability to restore them later.
     * @param node
     */
    public void hideNode(int node) {
    	// if nothing else is hidden right now.
    	if (this.hiddenConnections == null && this.hiddenInwardConnections == null && hiddenNode == -1) {
			this.hiddenInwardConnections = new ArrayList<>();
			
    		// keep track of the hidden node
    		this.hiddenNode = node;
    		
    		// keep track of the node's current neighbors.
    		this.hiddenConnections = graph.getNeighbors(node);
    		
    		// clear the node's neighbors.
    		graph.setNeighbors(node, new HashSet<Integer>());
    		
    		// clear adjacent nodes of this node (as a neighbor)
    		Iterator<Integer> iter = this.hiddenConnections.iterator();
    		while(iter.hasNext()) {
    			int neighbor = iter.next();
    			
    			// if this neighbor has node as a neighbor
    			if (graph.getNeighbors(neighbor).contains(node)) {
    				
    				// save the nodeid of the neighbor so we can add the neighbor back later.
    				this.hiddenInwardConnections.add(neighbor);

    				// this is an awful name...
    				Set<Integer> neighborsNeighbors = graph.getNeighbors(neighbor);
    				
    				// remove node from the neighbor's neighbors.
    				neighborsNeighbors.remove(node);
    				
    				// set the neighbor's neighbors, to the new neighbor's neighbors.
    				graph.setNeighbors(neighbor, neighborsNeighbors);
    			}
    		}
    	} else {
    		System.out.println("WARNING: hide was called again before nodes were restores.");
    	}
    }
    
    /**
     * unhide a node (give it back its neighbors) this only works if its been hidden.
     * only one node may be hidden at a time.
     * @param node
     */
    public void showNode(int node) {
    	// if this is the node that is currently hidden
    	if (this.hiddenConnections != null && this.hiddenNode == node) {
    		// restore node's neighbors. (outwards)
    		graph.setNeighbors(node, hiddenConnections);
    		
    		// indicate that there is no longer a hidden node.
    		this.hiddenNode = -1;
    		
    		// add back node into node's neighbor's neighbors.
    		for (int neighbor : this.hiddenInwardConnections) {
    			Set<Integer> neighborsNeighbors = graph.getNeighbors(neighbor);
    			neighborsNeighbors.add(node);
    			graph.setNeighbors(neighbor, neighborsNeighbors);
    		}
    	} else {
    		System.out.println("WARNING: This node is not hidden.");
    	}
    }
    
    public void placeWall(WallVector w) { 
    	walls.add(w);
    	if (w.o == 'v') {
    		graph.removeNeighbor(w.a, w.a - 1);
    		graph.removeNeighbor(w.b, w.b - 1);
    	} else {
    		graph.removeNeighbor(w.a, w.a + 9);
    		graph.removeNeighbor(w.b, w.b + 9);
    	}
    	
	}
    
    public void 			placeWall(int a, int b, char o) { walls.add(new WallVector(a, b, o)); }
    
	public List<WallVector> getWalls() 						{ return walls; }
	public Graph 			getGraph() 						{ return graph; }
}
