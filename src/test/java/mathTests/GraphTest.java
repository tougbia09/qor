package mathTests;

import java.util.*;
import org.junit.*;
import com.bangma.qor.math.Graph;
import com.bangma.qor.math.Position;

import static org.junit.Assert.*;

public class GraphTest {
	
	private Graph g;
	private final int h = 3;
	private final int w = 3;
	
	/*
		TESTED WITH 3 x 3 GRID
		 - this grid contains all necessary 'edges cases' 

		  	  0   1   2
			+---+---+---+
		0	| 0 | 1 | 2 |
			+---+---+---+
		1	| 3 | 4 | 5 |
			+---+---+---+
		2	| 6 | 7 | 8 |
			+---+---+---+

	 */
	
	
	@Before public void before() {
		g = new Graph(h, w);
	}

    /**
     * Sanity check for Graph constructor.
     */
	@Test public void testGraphCreatesNewGraph() {
		assertSame(g.getClass(), Graph.class);
	}

    /**
     * Test height and width are assigned correctly.
     */
	@Test public void testHeightAndWidthAssignment() {
		assertSame(g.getHEIGHT(), h);
		assertSame(g.getWIDTH(), w);
	}

    /**
     * test that the correct number of nodes are created on init.
     */
	@Test public void testCorrectNumberOfNodes() {
		assertSame(g.getAllNodes().size(), h * w);
	}

    /**
     * Test specific neighbors for cells in the test graph
     * (see above)
     */
	@Test public void testTopLeftNeighbors() {
        Set<Integer> edges = g.getNeighbors(0);
		assertTrue(edges.contains(1));
		assertTrue(edges.contains(3));
		assertSame(edges.size(), 2);
	}

	/** See above */
	@Test public void testTopRightNeighbors() {
        Set<Integer> edges = g.getNeighbors(2);
		assertTrue(edges.contains(1));
		assertTrue(edges.contains(5));
		assertSame(edges.size(), 2);
	}

    /** See above */
	@Test public void testBottomLeftNeighbors() {
        Set<Integer> edges = g.getNeighbors(6);
		assertTrue(edges.contains(3));
		assertTrue(edges.contains(7));
		assertSame(edges.size(), 2);
	}

    /** See above */
	@Test public void testBottomRightNeighbors() {
		Set<Integer> edges = g.getNeighbors(8);
		assertTrue(edges.contains(5));
		assertTrue(edges.contains(7));
		assertSame(edges.size(), 2);
	}

    /** See above */
	@Test public void testTopCenterNeighbors() {
		Set<Integer> edges = g.getNeighbors(1);
		assertTrue(edges.contains(0));
		assertTrue(edges.contains(2));
		assertTrue(edges.contains(4));
		assertSame(edges.size(), 3);
	}

    /** See above */
	@Test public void testBottomCenterNeighbors() {
		Set<Integer> edges = g.getNeighbors(7);
		assertTrue(edges.contains(6));
		assertTrue(edges.contains(4));
		assertTrue(edges.contains(8));
		assertSame(edges.size(), 3);
	}

    /** See above */
	@Test public void testCenterLeftNeighbors() {
		Set<Integer> edges = g.getNeighbors(3);
		assertTrue(edges.contains(0));
		assertTrue(edges.contains(4));
		assertTrue(edges.contains(6));
		assertSame(edges.size(), 3);
	}

    /** See above */
	@Test public void testCenterRightNeighbors() {
		Set<Integer> edges = g.getNeighbors(5);
		assertTrue(edges.contains(2));
		assertTrue(edges.contains(4));
		assertTrue(edges.contains(8));
		assertSame(edges.size(), 3);
	}

    /** See above */
	@Test public void testCenterMiddleNeighbors() {
		Set<Integer> edges = g.getNeighbors(4);
		assertTrue(edges.contains(1));
		assertTrue(edges.contains(3));
		assertTrue(edges.contains(5));
		assertTrue(edges.contains(7));
		assertSame(edges.size(), 4);
	}

    /**
     * Test that weights are assigned to the graph properly
     */
	@Test public void testWeightAssignment() {
	    g.addConnection(0, 1, 10);
	    assertSame(g.getWeight(0, 1), 10);
    }
	
	/**
	 * assure that removing a neighbor will make it no longer
	 * show up in the neighbors set.
	 */
	@Test public void testRemoveNeighborWorks() {
		g.getNeighbors(0);
		g.removeNeighbor(0, 1);
		assertTrue(!g.getNeighbors(0).contains(1));
	}
	
	/**
	 * assure that a is removed as a neighbor from b, AND
	 * that b is removed as a neighbor from a. 
	 */
	@Test public void testRemoveNeighborRemovesBothNeighbors() {
		g.getNeighbors(0);
		g.removeNeighbor(0, 1);
		assertTrue(!g.getNeighbors(0).contains(1));
		assertTrue(!g.getNeighbors(1).contains(0));
	}
	
    /**
     * Sanity check for minInQueue.
     */
    @Test public void testMinInQueueReturnSingleItem() {
	    List<Integer> queue = new ArrayList<>();
	    queue.add(0);
	    assertSame(Graph.minInQueue(new int[] { 1 }, queue), 0);
    }

    /**
     * Test that if two values are the same it returns the first one.
     */
    @Test public void testMinInQueueReturnOrderFirst() {
        List<Integer> queue = new ArrayList<>();
        queue.add(0); queue.add(1);
        assertSame(Graph.minInQueue(new int[] { 1, 1 }, queue), 0);
    }

    /**
     * I only want to check for the smallest item if its 0 or 1.\
     * In the list provided index {2} is the smallest,
     * However, it should only check the first two items
     * (with indexes in the queue)
     */
    @Test public void testMinInQueueReturnMultiItem() {
        List<Integer> queue = new ArrayList<>();
        queue.add(0); queue.add(1);
        assertSame(Graph.minInQueue(new int[] { 4, 3, 1 }, queue), 1);
    }

    /**
     * Sanity / Regression check that the algorithm even runs.
     */
    @Test public void testAnalyzeGraphRuns() {
        g.analyzeGraph(0, 0);
    }

    @Test public void testAnalyzeGraphRunsWithInvalidNode() {
        g.analyzeGraph(0, -1);
    }

    @Test public void testDijkstrasWithSingleNode() {
    	Deque<Integer> path = g.findPath(0, 0);
        assertTrue(path.size() == 1);
        assertSame(path.pop(), 0);
    }

    @Test public void testDijkstrasWithTwoNodes() {
        Deque<Integer> path = g.findPath(0, 1);
        assertTrue(path.size() == 2);
        assertSame(path.pop(), 0);
        assertSame(path.pop(), 1);
    }

    @Test public void testAddNeigborsCreatesBothAssociations() {

    }

    /**
     * Test that dijkstra's is working as expected with a test graph.
     *
     *  [0]          [1]
     *     O-- 1 --O
     *     |       |
     *     2       1
     *     |       |
     *     O-- 2 --O
     *  [2]          [3]
     */
    @Test public void testDijkstrasWithSimpleGraph() {
        Graph graph = new Graph(2,2);

        graph.addConnection(0, 1, 1);
        graph.addConnection(1, 3, 1);
        graph.addConnection(0, 2, 1);
        graph.addConnection(2, 3, 1);

        assertSame("Node 0", graph.getNeighbors(0).size(), 2);
        assertSame("Node 1", graph.getNeighbors(1).size(), 2);
        assertSame("Node 2", graph.getNeighbors(2).size(), 2);
        assertSame("Node 3", graph.getNeighbors(3).size(), 2);


        Deque<Integer> path = graph.findPath(0, 3);

        assertSame(path.size(), 3);
        assertSame(path.pop(), 0);
        assertSame(path.pop(), 1);
        assertSame(path.pop(), 3);
    }
    @Test public void testIdConversionToTupleZero() {
        Position tuple = g.convertIdToTuple(0);
        assertSame(tuple.x, 0);
        assertSame(tuple.y, 0);
    }
    @Test public void testIdConversionToTupleTwo() {
        Position tuple = g.convertIdToTuple(2);
        assertSame(tuple.x, 2);
        assertSame(tuple.y, 0);
    }
    @Test public void testIdConversionToTupleFour() {
        Position tuple = g.convertIdToTuple(4);
        assertSame(tuple.x, 1);
        assertSame(tuple.y, 1);
    }
    @Test public void testTupleConversionToIdOne() {
        Position tuple = new Position(1, 0);
        assertSame(g.convertTupleToId(tuple), 1);
    }
    @Test public void testTupleConversionToIdThree() {
        Position tuple = new Position(0, 1);
        assertSame(g.convertTupleToId(tuple), 3);
    }
    @Test public void testTupleConversionToIdFive() {
        Position tuple = new Position(2, 1);
        assertSame(g.convertTupleToId(tuple), 5);
    }
    
    @Test public void testNeighborExistsReturnsTrueForExistingConnections() {
    	g.getNeighbors(0);
    	assertTrue(g.neighborExists(0, 1));
    }
    
    @Test public void testNeighborExistsReturnsFalseForNonExistantConnections() {
    	g.getNeighbors(0);
    	assertFalse(g.neighborExists(0, -1));
    	assertFalse(g.neighborExists(0, 4));
    }
}
