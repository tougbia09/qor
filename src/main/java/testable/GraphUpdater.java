package testable;

import com.bangma.qor.math.Graph;

/**
 * A testable utility for updating the graph with walls for the gameboard.
 * Also contains all logic regarding the ability to place walls.
 */
public class GraphUpdater {
	private Graph 	graph;
	private int 	width;
	/**
	 * Create a new instance of GraphUpdater.
	 * @param graph the graph to manage.
	 */
	public GraphUpdater(Graph graph) {
		this.graph = graph;
		width = graph.getWIDTH();
	}
	
    /**
     * The logic for whether a wall can be placed at the current position.
     * see: wall-autofill-theory image in project folder for diagram.
     * 
     * @param n the numeric n of the cell
     * @param x the x position in the grid ( 0 init )
     * @param y the y position in the grid ( 0 init )
     * @param o the orientation of the wall ( h / v )
     * @return true if a wall can be placed, false otherwise.
     */
    public boolean wallAllowed(int n, int x, int y, char o) {
    	if (o == 'h' && x != 8) {
			if (!regularHorizontalWallAllowed(n)) return false;
		} else if (o == 'v' && y == 8) {
			if (
				!graph.neighborExists(n, n - 1) ||
				!graph.neighborExists(n, n - 9) ||
				!graph.neighborExists(n-1, n-1 - 9)
			) { return false; }
		} else {
			if (
				!graph.neighborExists(n, n - 1) ||
				!graph.neighborExists(n, n + 9) ||
				!graph.neighborExists(n-1, n-1 -+9)
			) { return false; }
		}
    	return true;
    }
    
    /**
     * Check if a wall can be placed horizontally to the right from this
     * position. Check if all neighbors still exist in this space, to
     * avoid overlapping walls. 
     * 
     * @param n the node id of the wall to check
     * @return true if possible to place, false otherwise.
     */
    public boolean regularHorizontalWallAllowed(int n) {
    	return graph.neighborExists(n, n + 1) 
			&& graph.neighborExists(n, n + width) 
			&& graph.neighborExists(n+1, n+1 + width);
    }

	public Graph getGraph() {
		return graph;
	}
}
