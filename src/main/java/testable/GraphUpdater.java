package testable;

import java.util.ArrayList;
import java.util.List;

import com.bangma.qor.math.Graph;
import com.bangma.qor.math.WallVector;

/**
 * A testable utility for updating the graph with walls for the gameboard.
 * Also contains all logic regarding the ability to place walls.
 */
public class GraphUpdater {
	private Graph 	graph;
	private int 	width;
	private List<WallVector> walls;
	
	/**
	 * Create a new instance of GraphUpdater.
	 * @param graph the graph to manage.
	 */
	public GraphUpdater(Graph graph) {
		this.graph = graph;
		width = graph.getWidth();
		walls = new ArrayList<>();
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
