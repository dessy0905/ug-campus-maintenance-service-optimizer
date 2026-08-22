package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrimTest {

    private Graph campusGraph;

    @BeforeEach
    public void setUp() {
        campusGraph = new Graph();
    }

    @Test
    public void testSmallCampusGraphMST() {
        /*
         * Same 5-location campus road network as in KruskalTest:
         * 1: Balme Library
         * 2: Legon Hall
         * 3: JQB
         * 4: CS Department
         * 5: Bush Canteen
         */
        campusGraph.addEdge(1, 2, 150.0);
        campusGraph.addEdge(1, 3, 200.0);
        campusGraph.addEdge(2, 3, 100.0);
        campusGraph.addEdge(2, 4, 300.0);
        campusGraph.addEdge(3, 4, 250.0);
        campusGraph.addEdge(4, 5, 180.0);
        campusGraph.addEdge(3, 5, 400.0);

        Prim prim = new Prim(campusGraph);
        Prim.Result result = prim.computeMST(1);

        result.display();

        assertEquals(5, result.getTotalVertices());
        assertEquals(5, result.getVisitedVertices());
        assertEquals(4, result.getEdgeCount()); // V - 1 = 4
        assertTrue(result.isFullyConnected());
        assertEquals(680.0, result.getTotalWeight(), 0.001);
    }

    @Test
    public void testDifferentStartingVerticesProduceSameTotalWeight() {
        campusGraph.addEdge(1, 2, 150.0);
        campusGraph.addEdge(1, 3, 200.0);
        campusGraph.addEdge(2, 3, 100.0);
        campusGraph.addEdge(2, 4, 300.0);
        campusGraph.addEdge(3, 4, 250.0);
        campusGraph.addEdge(4, 5, 180.0);
        campusGraph.addEdge(3, 5, 400.0);

        // Test starting from each vertex 1 through 5
        for (int startVertex = 1; startVertex <= 5; startVertex++) {
            Prim.Result result = Prim.findMinimumSpanningTree(campusGraph, startVertex);
            assertEquals(5, result.getTotalVertices());
            assertEquals(4, result.getEdgeCount());
            assertTrue(result.isFullyConnected());
            assertEquals(680.0, result.getTotalWeight(), 0.001, "Failed for startVertex " + startVertex);
        }
    }

    @Test
    public void testPrimAndKruskalEquivalence() {
        /*
         * Verify that Prim and Kruskal produce identical MST weights
         * on a more complex graph network.
         */
        campusGraph.addEdge(1, 2, 4.0);
        campusGraph.addEdge(1, 3, 8.0);
        campusGraph.addEdge(2, 3, 11.0);
        campusGraph.addEdge(2, 4, 8.0);
        campusGraph.addEdge(3, 5, 7.0);
        campusGraph.addEdge(3, 6, 1.0);
        campusGraph.addEdge(4, 5, 2.0);
        campusGraph.addEdge(4, 7, 7.0);
        campusGraph.addEdge(4, 8, 4.0);
        campusGraph.addEdge(5, 6, 6.0);
        campusGraph.addEdge(6, 8, 2.0);
        campusGraph.addEdge(7, 8, 14.0);
        campusGraph.addEdge(7, 9, 9.0);
        campusGraph.addEdge(8, 9, 10.0);

        Kruskal.Result kruskalResult = Kruskal.findMinimumSpanningTree(campusGraph);
        Prim.Result primResult = Prim.findMinimumSpanningTree(campusGraph, 1);

        assertEquals(kruskalResult.getTotalWeight(), primResult.getTotalWeight(), 0.001);
        assertEquals(kruskalResult.getEdgeCount(), primResult.getEdgeCount());
        assertEquals(8, primResult.getEdgeCount()); // 9 vertices => 8 edges
    }

    @Test
    public void testDisconnectedGraphHandling() {
        // Component 1: {1, 2}
        campusGraph.addEdge(1, 2, 50.0);

        // Component 2: {3, 4}
        campusGraph.addEdge(3, 4, 80.0);

        // Start from vertex 1 => only component 1 reachable
        Prim.Result result = Prim.findMinimumSpanningTree(campusGraph, 1);

        assertEquals(4, result.getTotalVertices());
        assertEquals(2, result.getVisitedVertices());
        assertEquals(1, result.getEdgeCount());
        assertFalse(result.isFullyConnected());
        assertEquals(50.0, result.getTotalWeight(), 0.001);
    }

    @Test
    public void testSingleVertexAndEmptyGraph() {
        // Empty graph
        Prim.Result emptyResult = Prim.findMinimumSpanningTree(campusGraph);
        assertEquals(0, emptyResult.getTotalVertices());
        assertEquals(0, emptyResult.getEdgeCount());
        assertEquals(0.0, emptyResult.getTotalWeight(), 0.001);

        // Single vertex graph
        campusGraph.addVertex(100);
        Prim.Result singleResult = Prim.findMinimumSpanningTree(campusGraph, 100);
        assertEquals(1, singleResult.getTotalVertices());
        assertEquals(1, singleResult.getVisitedVertices());
        assertEquals(0, singleResult.getEdgeCount());
        assertEquals(0.0, singleResult.getTotalWeight(), 0.001);
    }

    @Test
    public void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> new Prim(null));
        assertThrows(IllegalArgumentException.class, () -> Prim.findMinimumSpanningTree(null));
        assertThrows(IllegalArgumentException.class, () -> Prim.findMinimumSpanningTree(null, 1));

        campusGraph.addVertex(1);
        // Start vertex 999 does not exist
        assertThrows(IllegalArgumentException.class, () -> Prim.findMinimumSpanningTree(campusGraph, 999));
    }
}
