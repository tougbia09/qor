package com.bangma.qor.objects;

import java.util.Deque;
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
	private Deque<Integer> path;
	private final Player playerOne;
	
	/**
	 * Create an instance of the RobotPlayer class.
	 * 
	 * @param t the visible texture of the RobotPlayer
	 * @param x the x position for the RobotPlayer on the grid.
	 * @param y the y position for the RobotPlayer on the grid.
	 * @param g the graph the RobotPlayer is placed on, this is necessary for decision making.
	 */
	public RobotPlayer(Texture t, int x, int y, Player p1) { 
		super(t, x, y);
		Position pos = new Position(x, y);
        move(pos);
        this.playerOne = p1;
	}
	
	/**
	 * Run all of the necessary functions to update where the bot needs to go, and what it needs to do.
	 */
	public void update() {
		/* TODO currently the bot aims for one square, 
		 * make modifications so that it aims for any in the corresponding row. */
		path = graph.findPath(graph.convertTupleToId(this.getGridPosition()), 4);
		Deque<Integer> playerOnePath = graph.findPath(graph.convertTupleToId(playerOne.getGridPosition()), 76);
		
		if (playerOnePath.size() < path.size()) {
			System.out.println("OH NO!!!");
		}
	}
	
	/**
	 * this function moves the bot to its next calculated position.
	 */
	public void makeOwnMove() {
		path.pop(); // don't move to the current node. (find path returns the whole path (w start and end))
		this.move(graph.convertIdToTuple(path.pop()));
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
}
