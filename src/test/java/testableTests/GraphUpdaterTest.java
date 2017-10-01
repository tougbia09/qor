package testableTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.bangma.qor.math.Graph;
import com.bangma.qor.math.WallVector;

import testable.GraphUpdater;

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
}
