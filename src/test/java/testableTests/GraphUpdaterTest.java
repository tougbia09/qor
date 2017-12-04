package testableTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.bangma.qor.math.Graph;
import com.bangma.qor.math.WallVector;
import com.bangma.qor.testable.GraphUpdater;

public class GraphUpdaterTest {
	private Graph graph;
	private GraphUpdater updater;
	
	private final int graphWidth = 3;
	
	private final int[] legalRegularHorizontalWalls = new int[] { 0, 1, 3, 4 };
	private final int[] legalRegularVerticalWalls 	= new int[] { 1, 2, 4, 5 };
	private final int[] legalEdgeHorizontalWalls 	= new int[] { 2, 5 };
	private final int[] legalEdgeVerticalWalls 		= new int[] { 7, 8 };
	
	
	/* TESTED WITH 3 x 3 GRID 		
	 * ALL WALLS ARE PLACED ABOVE / LEFT OF THE SOURCE NODE
	 * 
	 * 	  +---+---+---+
	 * 2  | 6 | 7 | 8 |
	 * 	  +---+---+---+
	 * 1  | 3 | 4 | 5 |
	 * 	  +---+---+---+
	 * 0  | 0 | 1 | 2 |
	 * 	  +---+---+---+
	 * 	    0   1   2
	 */
	
	@Before() public void setup() {
		graph = new Graph(graphWidth, graphWidth);
		updater = new GraphUpdater(graph);
	}
	
	@Test public void testGraphUpdaterInitialized() {
		assertNotNull(updater);
		assertSame(updater.getGraph(), graph);
	}
	
	@Test public void testWallAllowedOnEmptyGraph() {
		for (int i : legalRegularHorizontalWalls) {
			assertTrue(updater.wallAllowed(new WallVector(i, i + 1, 'h')));
		}
		for (int i : legalRegularVerticalWalls) {
			assertTrue(updater.wallAllowed(new WallVector(i, i + graphWidth, 'v')));
		}
		for (int i : legalEdgeHorizontalWalls) {
			assertTrue(updater.wallAllowed(new WallVector(i, i - 1, 'h')));
		}
		for (int i : legalEdgeVerticalWalls) {
			assertTrue(updater.wallAllowed(new WallVector(i, i - graphWidth, 'v')));
		}
	}
	
	@Test public void testWallAllowedWithHorizontalWallPlaced() {
		updater.placeWall(0, 1, 'h');
		assertFalse(updater.wallAllowed(new WallVector(1, 4, 'v')));
		
		assertTrue(updater.wallAllowed(new WallVector(4, 7, 'v')));
		assertTrue(updater.wallAllowed(new WallVector(2, 5, 'v')));
		assertTrue(updater.wallAllowed(new WallVector(5, 8, 'v')));
	}
	
	@Test public void testWallAllowedWithVerticalWallPlaced() {
		updater.placeWall(1, 4, 'v');
		assertFalse(updater.wallAllowed(new WallVector(0, 1, 'h')));
		
		assertTrue(updater.wallAllowed(new WallVector(1, 2, 'h')));
		assertTrue(updater.wallAllowed(new WallVector(4, 5, 'h')));
		assertTrue(updater.wallAllowed(new WallVector(6, 7, 'h')));
	}
	
	@Test public void testHideNodeRemovesAllNeighborsCorner() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(0);
		assertTrue(g.getNeighbors(0).isEmpty());
		assertTrue(!g.neighborExists(0, 1)); // moving outwards
		assertTrue(!g.neighborExists(0, 3));
		
		assertTrue(!g.neighborExists(1, 0)); // moving inwards
		assertTrue(!g.neighborExists(3, 0));
	}
	
	@Test public void testHideNodeRemovesAllNeighborsCenter() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(4);
		assertTrue(g.getNeighbors(4).isEmpty());
		assertTrue(!g.neighborExists(4, 1)); // moving outwards
		assertTrue(!g.neighborExists(4, 3));
		assertTrue(!g.neighborExists(4, 5));
		assertTrue(!g.neighborExists(4, 7));
		
		assertTrue(!g.neighborExists(1, 4)); // moving inwards
		assertTrue(!g.neighborExists(3, 4));
		assertTrue(!g.neighborExists(5, 4));
		assertTrue(!g.neighborExists(7, 4));
	}
	
	@Test public void testHideNodeRemovesAllNeighborsWall() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(3);
		assertTrue(g.getNeighbors(3).isEmpty());
		assertTrue(!g.neighborExists(3, 0)); // moving outwards
		assertTrue(!g.neighborExists(3, 4));
		assertTrue(!g.neighborExists(3, 6));
		
		assertTrue(!g.neighborExists(0, 3)); // moving inwards
		assertTrue(!g.neighborExists(4, 3));
		assertTrue(!g.neighborExists(6, 3));
	}
	
	@Test public void testShowNodeRestoresAllNeighborsCorner() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(0);
		gu.showNode(0);
		
		assertTrue(!g.getNeighbors(0).isEmpty());
		assertTrue(g.neighborExists(0, 1)); // moving outwards
		assertTrue(g.neighborExists(0, 3));
		
		assertTrue(g.neighborExists(1, 0)); // moving inwards
		assertTrue(g.neighborExists(3, 0));
		
	}
	
	@Test public void testShowNodeRestoresAllNeighborsCenter() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(4);
		gu.showNode(4);
		assertTrue(!g.getNeighbors(4).isEmpty());
		assertTrue(g.neighborExists(4, 1)); // moving outwards
		assertTrue(g.neighborExists(4, 3));
		assertTrue(g.neighborExists(4, 5));
		assertTrue(g.neighborExists(4, 7));
		
		assertTrue(g.neighborExists(1, 4)); // moving inwards
		assertTrue(g.neighborExists(3, 4));
		assertTrue(g.neighborExists(5, 4));
		assertTrue(g.neighborExists(7, 4));
	}
	
	@Test public void testShowNodeRestoresAllNeighborsWall() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(3);
		gu.showNode(3);
		assertTrue(!g.getNeighbors(3).isEmpty());
		assertTrue(g.neighborExists(3, 0)); // moving outwards
		assertTrue(g.neighborExists(3, 4));
		assertTrue(g.neighborExists(3, 6));
		
		assertTrue(g.neighborExists(0, 3)); // moving inwards
		assertTrue(g.neighborExists(4, 3));
		assertTrue(g.neighborExists(6, 3));
	}
	@Test public void testShowNodeRestoresAllNeighborsWallAfterIncorrectHideShowCall() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(3);
		gu.showNode(9); // this is incorrect, and shouldn't change any data.
		gu.showNode(3);
		assertTrue(!g.getNeighbors(3).isEmpty());
		assertTrue(g.neighborExists(3, 0)); // moving outwards
		assertTrue(g.neighborExists(3, 4));
		assertTrue(g.neighborExists(3, 6));
		
		assertTrue(g.neighborExists(0, 3)); // moving inwards
		assertTrue(g.neighborExists(4, 3));
		assertTrue(g.neighborExists(6, 3));
	}
	@Test public void testShowNodeRestoresAllNeighborsWallAfterIncorrectShowNodeCall() {
		Graph g = new Graph(3,3);
		GraphUpdater gu = new GraphUpdater(g);
		
		gu.hideNode(3);
		gu.hideNode(5);
		gu.showNode(3);
		assertTrue(!g.getNeighbors(3).isEmpty());
		assertTrue(g.neighborExists(3, 0)); // moving outwards
		assertTrue(g.neighborExists(3, 4));
		assertTrue(g.neighborExists(3, 6));
		
		assertTrue(g.neighborExists(0, 3)); // moving inwards
		assertTrue(g.neighborExists(4, 3));
		assertTrue(g.neighborExists(6, 3));
	}
}
