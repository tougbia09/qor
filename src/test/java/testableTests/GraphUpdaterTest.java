package testableTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.bangma.qor.math.Graph;

import testable.GraphUpdater;

public class GraphUpdaterTest {
	private Graph graph;
	private GraphUpdater updater;
	
	@Before() public void setup() {
		graph = new Graph(9,9);
		updater = new GraphUpdater(graph);
	}
	
	@Test public void testGraphUpdaterInitialized() {
		assertNotNull(updater);
		assertSame(updater.getGraph(), graph);
	}
	
	@Test public void testRegularHorizontalWallAllowed() {
		assertTrue(updater.regularHorizontalWallAllowed(0));
	}
}
