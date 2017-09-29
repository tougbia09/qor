package testableTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.bangma.qor.math.Graph;
import com.bangma.qor.math.Tuple;

import testable.GraphUpdater;

public class GraphUpdaterTest {
	private Graph graph;
	private GraphUpdater updater;
	
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
		graph = new Graph(3,3);
		updater = new GraphUpdater(graph);
	}
	
	@Test public void testGraphUpdaterInitialized() {
		assertNotNull(updater);
		assertSame(updater.getGraph(), graph);
	}
	
	@Test public void testRegularHorizontalWallAllowed() {
		for (int n : new int[] { 0, 1, 3, 4 }) 		assertTrue(updater.regularHorizontalWallAllowed(n));
		for (int n : new int[] { 2, 5, 8, 6, 7 }) 	assertFalse(updater.regularHorizontalWallAllowed(n));
	}
	@Test public void testEdgeVerticalWallAllowed() {
		for (int n : new int[] { 4, 5, 7, 8 }) 		assertTrue(updater.edgeVerticalWallAllowed(n));
		for (int n : new int[] { 0, 1, 2, 3, 6 }) 	assertFalse(updater.edgeVerticalWallAllowed(n));
	}
	@Test public void testEdgeHorizontalWallAllowed() {
		for (int n : new int[] { 1, 2, 4, 5 }) 		assertTrue(updater.edgeHorizontalWallAllowed(n));
		for (int n : new int[] { 0, 3, 6, 7, 8 }) 	assertFalse(updater.edgeHorizontalWallAllowed(n));
	}
	@Test public void testWallAllowed() {
		for (int n : new int[] { 0, 1, 2, 3, 4, 5 }) { 
			Tuple<Integer> pos = graph.convertIdToTuple(n);
			assertTrue(updater.wallAllowed(n, pos.x(), pos.y(), 'h')); 
		}
		for (int n : new int[] { 6, 7, 8 }) { 
			Tuple<Integer> pos = graph.convertIdToTuple(n);
			assertFalse(updater.wallAllowed(n, pos.x(), pos.y(), 'h')); 
		}
		for (int n : new int[] { 1, 2, 4, 5, 7, 8 }) { 
			Tuple<Integer> pos = graph.convertIdToTuple(n);
			assertTrue(updater.wallAllowed(n, pos.x(), pos.y(), 'v')); 
		}
		for (int n : new int[] { 0, 3, 6 }) { 
			Tuple<Integer> pos = graph.convertIdToTuple(n);
			assertFalse(updater.wallAllowed(n, pos.x(), pos.y(), 'v')); 
		}
	}
}
