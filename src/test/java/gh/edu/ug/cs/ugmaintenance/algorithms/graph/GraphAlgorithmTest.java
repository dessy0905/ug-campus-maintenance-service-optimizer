package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import java.util.Arrays;
import java.util.List;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Edge;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphAlgorithmTest {

    // ------------------------------------------------------------------
    // Test graphs
    // ------------------------------------------------------------------

    /**
     * Small undirected graph:
     * 0--1 (1), 1--2 (2), 2--3 (3), 0--2 (4), 0--3 (10).
     * Shortest 0 -> 3 is 0-1-2-3 = 6. MST weight is 1+2+3 = 6.
     */
    private Graph simpleGraph() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 2, 4);
        graph.addEdge(0, 3, 10);
        return graph;
    }

    /**
     * Directed graph (CLRS-style Dijkstra example):
     * s=0: t=10, y=5; y: t=3, z=2, x=9; t: z=1, x=2; z: x=4.
     * From 0: y=5, t=8, z=7, x=10 (path 0-2-1-4).
     */
    private Graph directedGraph() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 10); // s -> t
        graph.addEdge(0, 2, 5);  // s -> y
        graph.addEdge(2, 1, 3);  // y -> t
        graph.addEdge(2, 3, 2);  // y -> z
        graph.addEdge(1, 3, 1);  // t -> z
        graph.addEdge(1, 4, 2);  // t -> x
        graph.addEdge(3, 4, 4);  // z -> x
        graph.addEdge(2, 4, 9);  // y -> x
        return graph;
    }

    /**
     * Six-vertex undirected graph whose MST weight is 15
     * (edges 2-5:1, 0-1:2, 1-2:3, 5-3:4, 3-4:5).
     */
    private Graph mediumGraph() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        graph.addEdge(0, 2, 6);
        graph.addEdge(2, 3, 8);
        graph.addEdge(3, 4, 5);
        graph.addEdge(1, 4, 7);
        graph.addEdge(4, 5, 9);
        graph.addEdge(2, 5, 1);
        graph.addEdge(3, 5, 4);
        return graph;
    }

    // ------------------------------------------------------------------
    // Graph structure tests
    // ------------------------------------------------------------------

    @Test
    public void testUndirectedGraphIsSymmetric() {
        Graph graph = simpleGraph();
        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 0));
        assertEquals(1.0, graph.getWeight(1, 0), 1e-9);
        assertEquals(5, graph.getEdgeCount()); // each edge counted once
        assertEquals(3, graph.getDegree(0));
    }

    @Test
    public void testDirectedGraphIsOneWay() {
        Graph graph = directedGraph();
        assertTrue(graph.hasEdge(0, 1));
        assertFalse(graph.hasEdge(1, 0));
        assertEquals(Graph.NO_EDGE, graph.getWeight(1, 0), 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGraphZeroVerticesThrows() {
        new Graph(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeOutOfRangeThrows() {
        simpleGraph().addEdge(0, 99, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeNegativeWeightThrows() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, -0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWeightOutOfRangeThrows() {
        simpleGraph().getWeight(-1, 0);
    }

    // ------------------------------------------------------------------
    // Dijkstra tests
    // ------------------------------------------------------------------

    @Test
    public void testDijkstraSimplePath() {
        Dijkstra.ShortestPathResult result = Dijkstra.findShortestPaths(simpleGraph(), 0);
        assertEquals(6.0, result.distanceTo(3), 1e-9);
        assertEquals(Arrays.asList(0, 1, 2, 3), result.pathTo(3));
    }

    @Test
    public void testDijkstraDirectEdge() {
        assertEquals(1.0, Dijkstra.shortestDistance(simpleGraph(), 0, 1), 1e-9);
        assertEquals(Arrays.asList(0, 1), Dijkstra.findShortestPath(simpleGraph(), 0, 1));
    }

    @Test
    public void testDijkstraDirectedDistances() {
        Dijkstra.ShortestPathResult result = Dijkstra.findShortestPaths(directedGraph(), 0);
        assertEquals(5.0, result.distanceTo(2), 1e-9);  // y
        assertEquals(8.0, result.distanceTo(1), 1e-9);  // t via y
        assertEquals(7.0, result.distanceTo(3), 1e-9);  // z
        assertEquals(10.0, result.distanceTo(4), 1e-9); // x via t
        assertEquals(Arrays.asList(0, 2, 1, 4), result.pathTo(4));
    }

    @Test
    public void testDijkstraSameSourceAndTarget() {
        Dijkstra.ShortestPathResult result = Dijkstra.findShortestPaths(simpleGraph(), 2);
        assertEquals(0.0, result.distanceTo(2), 1e-9);
        assertEquals(Arrays.asList(2), result.pathTo(2));
    }

    @Test
    public void testDijkstraUnreachableTarget() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 2, 4);
        graph.addEdge(0, 3, 10);
        graph.addEdge(5, 4, 1); // separate component {4,5}
        Dijkstra.ShortestPathResult result = Dijkstra.findShortestPaths(graph, 0);
        assertEquals(Graph.NO_EDGE, result.distanceTo(5), 1e-9);
        assertTrue(result.pathTo(5).isEmpty());
    }

    @Test
    public void testDijkstraDistanceTableFormat() {
        String table = Dijkstra.findShortestPaths(simpleGraph(), 0).formatDistanceTable();
        assertTrue(table.contains("Vertex"));
        assertTrue(table.contains("Distance"));
        assertTrue(table.contains("0 -> 1 -> 2 -> 3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDijkstraSourceOutOfRangeThrows() {
        Dijkstra.findShortestPaths(simpleGraph(), 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDijkstraTargetOutOfRangeThrows() {
        Dijkstra.findShortestPath(simpleGraph(), 0, 9);
    }

    // ------------------------------------------------------------------
    // Prim tests
    // ------------------------------------------------------------------

    @Test
    public void testPrimSimpleGraph() {
        MstResult mst = Prim.minimumSpanningTree(simpleGraph());
        assertEquals(6.0, mst.getTotalWeight(), 1e-9);
        assertEquals(3, mst.getEdgeCount());
        assertTrue(containsEdge(mst, 0, 1, 1.0));
        assertTrue(containsEdge(mst, 1, 2, 2.0));
        assertTrue(containsEdge(mst, 2, 3, 3.0));
    }

    @Test
    public void testPrimMediumGraph() {
        MstResult mst = Prim.minimumSpanningTree(mediumGraph());
        assertEquals(15.0, mst.getTotalWeight(), 1e-9);
        assertEquals(5, mst.getEdgeCount());
        assertFalse(mst.getTrace().isEmpty());
    }

    @Test
    public void testPrimSingleVertex() {
        MstResult mst = Prim.minimumSpanningTree(new Graph(1));
        assertEquals(0.0, mst.getTotalWeight(), 1e-9);
        assertEquals(0, mst.getEdgeCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testPrimDisconnectedThrows() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);
        Prim.minimumSpanningTree(graph);
    }

    // ------------------------------------------------------------------
    // Kruskal tests
    // ------------------------------------------------------------------

    @Test
    public void testKruskalSimpleGraph() {
        MstResult mst = Kruskal.minimumSpanningTree(simpleGraph());
        assertEquals(6.0, mst.getTotalWeight(), 1e-9);
        assertEquals(3, mst.getEdgeCount());
        assertTrue(containsEdge(mst, 0, 1, 1.0));
        assertTrue(containsEdge(mst, 1, 2, 2.0));
        assertTrue(containsEdge(mst, 2, 3, 3.0));
    }

    @Test
    public void testKruskalConnectivityTrace() {
        // Triangle 0-1-2 (all weight 1) plus edge 2-3: the edge (1,2) is
        // considered after 0-1 and 0-2, so Kruskal must skip it as a cycle.
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 3, 1);

        MstResult mst = Kruskal.minimumSpanningTree(graph);
        assertEquals(3.0, mst.getTotalWeight(), 1e-9);
        assertEquals(3, mst.getEdgeCount());

        List<String> trace = mst.getTrace();
        assertTrue(trace.size() >= 4);
        assertEquals(3, trace.stream().filter(line -> line.startsWith("Accept")).count());
        assertEquals(1, trace.stream().filter(line -> line.startsWith("Skip")).count());
        assertTrue(trace.stream().anyMatch(line -> line.contains("components = 2")));
    }

    @Test
    public void testKruskalAndPrimAgree() {
        MstResult prim = Prim.minimumSpanningTree(mediumGraph());
        MstResult kruskal = Kruskal.minimumSpanningTree(mediumGraph());
        assertEquals(prim.getTotalWeight(), kruskal.getTotalWeight(), 1e-9);
        assertEquals(5, kruskal.getEdgeCount());
        assertEquals(prim.getEdgeCount(), kruskal.getEdgeCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testKruskalDisconnectedThrows() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);
        Kruskal.minimumSpanningTree(graph);
    }

    @Test
    public void testKruskalSingleVertex() {
        MstResult mst = Kruskal.minimumSpanningTree(new Graph(1));
        assertEquals(0.0, mst.getTotalWeight(), 1e-9);
        assertEquals(0, mst.getEdgeCount());
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private boolean containsEdge(MstResult mst, int from, int to, double weight) {
        for (Edge edge : mst.getEdges()) {
            boolean sameEndpoints =
                    (edge.getFrom() == from && edge.getTo() == to)
                            || (edge.getFrom() == to && edge.getTo() == from);
            if (sameEndpoints && Math.abs(edge.getWeight() - weight) < 1e-9) {
                return true;
            }
        }
        return false;
    }
}
