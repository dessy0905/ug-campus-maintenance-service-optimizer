package gh.edu.ug.cs.ugmaintenance.datastructures;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;

public class GraphTest {

    public static void main(String[] args) {

        System.out.println("=== UG Campus Maintenance Graph Test ===");
        System.out.println();

        testAddVertices();
        testAddEdges();
        testNeighbours();
        testHasEdge();
        testDegree();
        testRemoveEdge();
        testRemoveVertex();

        System.out.println();
        System.out.println("All graph tests passed!");
    }

    //  Vertex Tests 
    public static void testAddVertices() {

        System.out.println("--- Test 1: Add Vertices ---");

        Graph graph = new Graph();

        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        System.out.println("Number of vertices: " + graph.size());

        if (graph.size() != 3) {
            throw new RuntimeException("Vertex count test failed");
        }

        if (!graph.containsVertex(1)) {
            throw new RuntimeException("Vertex 1 should exist");
        }

        if (!graph.containsVertex(2)) {
            throw new RuntimeException("Vertex 2 should exist");
        }

        if (!graph.containsVertex(3)) {
            throw new RuntimeException("Vertex 3 should exist");
        }

        System.out.println("Vertex test passed.");
        System.out.println();
    }

    //  Edge Tests 
    public static void testAddEdges() {

        System.out.println("--- Test 2: Add Edges ---");

        Graph graph = new Graph();

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);

        System.out.println("Graph after adding edges:");
        graph.display();

        if (!graph.hasEdge(1, 2)) {
            throw new RuntimeException("Edge 1-2 should exist");
        }

        if (!graph.hasEdge(2, 1)) {
            throw new RuntimeException("Graph should be undirected: edge 2-1 should exist");
        }

        if (!graph.hasEdge(1, 3)) {
            throw new RuntimeException("Edge 1-3 should exist");
        }

        if (!graph.hasEdge(4, 5)) {
            throw new RuntimeException("Edge 4-5 should exist");
        }

        System.out.println("Edge test passed.");
        System.out.println();
    }

    //  Neighbour Tests
    public static void testNeighbours() {

        System.out.println("--- Test 3: Get Neighbours ---");

        Graph graph = new Graph();

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);

        System.out.println("Neighbours of 1: "
                + graph.getNeighbours(1).toList());

        System.out.println("Neighbours of 2: "
                + graph.getNeighbours(2).toList());

        if (!graph.getNeighbours(1).contains(2)) {
            throw new RuntimeException("1 should be connected to 2");
        }

        if (!graph.getNeighbours(1).contains(3)) {
            throw new RuntimeException("1 should be connected to 3");
        }

        if (!graph.getNeighbours(2).contains(4)) {
            throw new RuntimeException("2 should be connected to 4");
        }

        System.out.println("Neighbour test passed.");
        System.out.println();
    }

    //  hasEdge Tests 
    public static void testHasEdge() {

        System.out.println("--- Test 4: Check Edges ---");

        Graph graph = new Graph();

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        System.out.println("Has edge 1-2: " + graph.hasEdge(1, 2));
        System.out.println("Has edge 2-3: " + graph.hasEdge(2, 3));
        System.out.println("Has edge 1-3: " + graph.hasEdge(1, 3));

        if (!graph.hasEdge(1, 2)) {
            throw new RuntimeException("Edge 1-2 should exist");
        }

        if (!graph.hasEdge(2, 3)) {
            throw new RuntimeException("Edge 2-3 should exist");
        }

        if (graph.hasEdge(1, 3)) {
            throw new RuntimeException("Edge 1-3 should not exist");
        }

        System.out.println("hasEdge test passed.");
        System.out.println();
    }

    //  Degree Tests
    public static void testDegree() {

        System.out.println("--- Test 5: Vertex Degree ---");

        Graph graph = new Graph();

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);

        System.out.println("Degree of vertex 1: " + graph.getDegree(1));

        if (graph.getDegree(1) != 3) {
            throw new RuntimeException("Vertex 1 should have degree 3");
        }

        System.out.println("Degree test passed.");
        System.out.println();
    }

    // Remove Edge Tests 
    public static void testRemoveEdge() {

        System.out.println("--- Test 6: Remove Edge ---");

        Graph graph = new Graph();

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        System.out.println("Before removing edge 1-2:");
        graph.display();

        boolean removed = graph.removeEdge(1, 2);

        System.out.println("Edge 1-2 removed: " + removed);

        if (!removed) {
            throw new RuntimeException("Edge 1-2 should have been removed");
        }

        if (graph.hasEdge(1, 2)) {
            throw new RuntimeException("Edge 1-2 should no longer exist");
        }

        if (graph.hasEdge(2, 1)) {
            throw new RuntimeException(
                    "Because the graph is undirected, edge 2-1 should also be removed"
            );
        }

        System.out.println("After removing edge 1-2:");
        graph.display();

        System.out.println("Remove edge test passed.");
        System.out.println();
    }

    //  Remove Vertex Tests 
    public static void testRemoveVertex() {

        System.out.println("--- Test 7: Remove Vertex ---");

        Graph graph = new Graph();

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);

        System.out.println("Before removing vertex 1:");
        graph.display();

        boolean removed = graph.removeVertex(1);

        System.out.println("Vertex 1 removed: " + removed);

        if (!removed) {
            throw new RuntimeException("Vertex 1 should have been removed");
        }

        if (graph.containsVertex(1)) {
            throw new RuntimeException("Vertex 1 should no longer exist");
        }

        if (graph.hasEdge(2, 1)) {
            throw new RuntimeException(
                    "Edges connected to removed vertex should also be removed"
            );
        }

        if (graph.hasEdge(3, 1)) {
            throw new RuntimeException(
                    "Edges connected to removed vertex should also be removed"
            );
        }

        System.out.println("After removing vertex 1:");
        graph.display();

        System.out.println("Remove vertex test passed.");
        System.out.println();
    }
}
