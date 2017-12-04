package com.bangma.qor.objects;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import com.badlogic.gdx.graphics.Texture;
import com.bangma.qor.math.Graph;
import com.bangma.qor.math.Position;

/**
 * The pathfinding bot is saved here, it extends a regular player for ease of use.
 * This bot implements Dijkstra's algorithm to find its way across the game board.
 * 
 * @author tim
 *
 */
public class RobotPlayer extends Player {
	private Graph graph;
	private final int[] winNodes = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
	private Deque<Integer> bestPath;
	/**
	 * Create an instance of the RobotPlayer class.
	 * 
	 * @param t the visible texture of the RobotPlayer
	 * @param x the x position for the RobotPlayer on the grid.
	 * @param y the y position for the RobotPlayer on the grid.
	 * @param g the graph the RobotPlayer is placed on, this is necessary for decision making.
	 * @param graphUpdater to modify the graph weights around the player
	 */
	public RobotPlayer(Texture t, int x, int y) { 
		super(t, x, y);
		Position pos = new Position(x, y);
        move(pos);
	}
	
	/**
	 * Run all of the necessary functions to update where the bot needs to go, and what it needs to do.
	 */
	public void update() {
		/* TODO currently the bot aims for one square, 
		 * make modifications so that it aims for any in the corresponding row. */
		int shortestDist = Graph.INF;
		this.bestPath = null;
		for (int node : winNodes) {
			System.out.println("Checking: " + graph.convertTupleToId(this.getGridPosition()) + " " + node);
			try {
				Deque<Integer> newPath = graph.findPath(graph.convertTupleToId(this.getGridPosition()), node);
				if (newPath != null && !newPath.isEmpty() && newPath.size() < shortestDist) {
					shortestDist = newPath.size();
					this.bestPath = newPath;
				}
			} catch (Exception e) { /* node is not accessible */ }
		}
	}
	
	/**
	 * this function moves the bot to its next calculated position.
	 */
	public void makeOwnMove() {
		if (this.bestPath != null) {
			for (Object node : this.bestPath.toArray()) {
				System.out.println(node);
			}
			this.bestPath.pop(); // don't move to the current node. (find path returns the whole path (w start and end))
			this.move(graph.convertIdToTuple(this.bestPath.pop()));
		} else {
			System.out.println("Something went wrong.");
		}
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
}
