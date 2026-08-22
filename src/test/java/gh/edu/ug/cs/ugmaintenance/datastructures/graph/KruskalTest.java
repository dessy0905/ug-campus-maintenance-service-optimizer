package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KruskalTest {

    private Graph campusGraph;

    @BeforeEach
    public void setUp() {
        campusGraph = new Graph();
    }

    @Test
    public void testSmallCampusGraphMST() {
        /*
         * Campus Location IDs:
         * 1: Balme Library
         * 2: Legon Hall
         * 3: JQB
         * 4: CS Department
         * 5: Bush Canteen
         *
         * Edges with distances in metres:
         * (1, 2) = 150m
         * (1, 3) = 200m
         * (2, 3) = 100m
         * (2, 4) = 300m
         * (3, 4) = 250m
         * (4, 5) = 180m
         * (3, 5) = 400m
         */
        campusGraph.addEdge(1, 2, 150.0);
        campusGraph.addEdge(1, 3, 200.0);
        campusGraph.addEdge(2, 3, 100.0);
        campusGraph.addEdge(2, 4, 300.0);
        campusGraph.addEdge(3, 4, 250.0);
        campusGraph.addEdge(4, 5, 180.0);
        campusGraph.addEdge(3, 5, 400.0);

        Kruskal kruskal = new Kruskal(campusGraph);
        Kruskal.Result result = kruskal.computeMST();

        // Print details to standard output for visual verification in oral defence
        result.display();

        // Total vertices = 5 => MST must have 5 - 1 = 4 edges
        assertEquals(5, result.getVertexCount());
        assertEquals(4, result.getEdgeCount());
        assertTrue(result.isFullyConnected());

        /*
         * Expected MST Edges:
         * 1. (2, 3) = 100m
         * 2. (1, 2) = 150m
         * 3. (4, 5) = 180m
         * 4. (3, 4) = 250m
         * Total Weight = 100 + 150 + 180 + 250 = 680m
         */
        assertEquals(680.0, result.getTotalWeight(), 0.001);
    }

    @Test
    public void testDiamondGraphMST() {
        /*
         * Diamond Graph:
         *   (1)---10---(2)
         *    | \      / |
         *    |  \    /  |
         *   30   5  40  20
         *    |    \/    |
         *    |    /\    |
         *   (3)---15---(4)
         */
        campusGraph.addEdge(1, 2, 10.0);
        campusGraph.addEdge(2, 4, 20.0);
        campusGraph.addEdge(4, 3, 15.0);
        campusGraph.addEdge(3, 1, 30.0);
        campusGraph.addEdge(1, 4, 5.0);
        campusGraph.addEdge(2, 3, 40.0);

        Kruskal.Result result = Kruskal.findMinimumSpanningTree(campusGraph);

        assertEquals(4, result.getVertexCount());
        assertEquals(3, result.getEdgeCount()); // V - 1 = 3
        assertTrue(result.isFullyConnected());

        // Selected edges should be: (1,4): 5, (1,2): 10, (4,3): 15 => Total = 30.0
        assertEquals(30.0, result.getTotalWeight(), 0.001);
    }

    @Test
    public void testDisconnectedCampusGraph() {
        // Cluster 1: 1 and 2
        campusGraph.addEdge(1, 2, 100.0);

        // Cluster 2: 3 and 4
        campusGraph.addEdge(3, 4, 200.0);

        Kruskal.Result result = Kruskal.findMinimumSpanningTree(campusGraph);

        assertEquals(4, result.getVertexCount());
        // Disconnected graph produces a spanning forest with 2 edges (< 4 - 1)
        assertEquals(2, result.getEdgeCount());
        assertFalse(result.isFullyConnected());
        assertEquals(300.0, result.getTotalWeight(), 0.001);
    }

    @Test
    public void testSingleVertexAndEmptyGraph() {
        // Empty graph
        Kruskal.Result emptyResult = Kruskal.findMinimumSpanningTree(campusGraph);
        assertEquals(0, emptyResult.getVertexCount());
        assertEquals(0, emptyResult.getEdgeCount());
        assertEquals(0.0, emptyResult.getTotalWeight(), 0.001);

        // Single vertex
        campusGraph.addVertex(10);
        Kruskal.Result singleResult = Kruskal.findMinimumSpanningTree(campusGraph);
        assertEquals(1, singleResult.getVertexCount());
        assertEquals(0, singleResult.getEdgeCount());
        assertEquals(0.0, singleResult.getTotalWeight(), 0.001);
    }

    @Test
    public void testNullGraphValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Kruskal(null));
        assertThrows(IllegalArgumentException.class, () -> Kruskal.findMinimumSpanningTree(null));
    }
}
