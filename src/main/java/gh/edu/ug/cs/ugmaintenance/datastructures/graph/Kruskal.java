package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.disjointset.DisjointSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

/**
 * Custom Implementation of Kruskal's Minimum Spanning Tree (MST) Algorithm.
 *
 * <p>Kruskal's algorithm finds a minimum spanning tree for a connected,
 * weighted undirected graph. If the graph is disconnected, it finds a
 * Minimum Spanning Forest.</p>
 *
 * <p>Algorithmic steps:
 * <ol>
 *   <li>Extract all undirected edges from the custom {@link Graph}.</li>
 *   <li>Sort the edges in non-decreasing order of weight using custom QuickSort.</li>
 *   <li>Initialize a custom {@link DisjointSet} with all vertices as separate sets.</li>
 *   <li>Iterate through sorted edges; for each edge (u, v), if u and v are in different
 *       sets (no cycle), union their sets and add the edge to the MST.</li>
 *   <li>Terminate early once V - 1 edges have been added (or when all edges are evaluated).</li>
 * </ol>
 *
 * <p>Uses only custom data structures: {@link Graph}, {@link DisjointSet},
 * {@link DynamicArray}, {@link HashSet}, and {@link List}.</p>
 */
public class Kruskal {

    /**
     * Represents a weighted edge between two vertices for MST algorithms.
     */
    public static class Edge implements Comparable<Edge> {
        private final int source;
        private final int destination;
        private final double weight;

        public Edge(int source, int destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public int getSource() {
            return source;
        }

        public int getDestination() {
            return destination;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return source + " --(" + weight + "m)--> " + destination;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return (source == edge.source && destination == edge.destination && Double.compare(edge.weight, weight) == 0)
                    || (source == edge.destination && destination == edge.source && Double.compare(edge.weight, weight) == 0);
        }
    }

    /**
     * Result container holding the MST edges, total weight, and connectivity status.
     */
    public static class Result {
        private final DynamicArray<Edge> mstEdges;
        private final double totalWeight;
        private final int vertexCount;
        private final boolean fullyConnected;

        public Result(DynamicArray<Edge> mstEdges, double totalWeight, int vertexCount, boolean fullyConnected) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.vertexCount = vertexCount;
            this.fullyConnected = fullyConnected;
        }

        public DynamicArray<Edge> getMstEdges() {
            return mstEdges;
        }

        public double getTotalWeight() {
            return totalWeight;
        }

        public int getEdgeCount() {
            return mstEdges.size();
        }

        public int getVertexCount() {
            return vertexCount;
        }

        public boolean isFullyConnected() {
            return fullyConnected;
        }

        public void display() {
            System.out.println("=== Kruskal's Minimum Spanning Tree ===");
            System.out.println("Vertices in Graph: " + vertexCount);
            System.out.println("Edges in MST: " + mstEdges.size() + (vertexCount > 0 ? " (Expected: " + (vertexCount - 1) + ")" : ""));
            System.out.println("Total MST Weight: " + totalWeight + "m");
            System.out.println("Fully Connected: " + fullyConnected);
            System.out.println("Selected Edges:");
            for (int i = 0; i < mstEdges.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + mstEdges.get(i));
            }
        }
    }

    private final Graph graph;

    public Kruskal(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null for Kruskal's algorithm");
        }
        this.graph = graph;
    }

    /**
     * Computes the Minimum Spanning Tree using Kruskal's algorithm.
     *
     * @return {@link Result} containing MST edges, total weight, and connectivity
     */
    public Result computeMST() {
        return findMinimumSpanningTree(this.graph);
    }

    /**
     * Static utility method to compute the Minimum Spanning Tree of a graph.
     *
     * @param graph the graph to analyze
     * @return {@link Result} containing MST edges and total weight
     */
    public static Result findMinimumSpanningTree(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }

        List<Integer> vertices = graph.getVertices();
        int vCount = vertices.size();

        DynamicArray<Edge> mstEdges = new DynamicArray<>();
        double totalWeight = 0.0;

        // Base case: empty or single-vertex graph
        if (vCount <= 1) {
            return new Result(mstEdges, 0.0, vCount, true);
        }

        // Step 1: Collect all unique undirected edges
        DynamicArray<Edge> allEdges = getAllEdges(graph);

        // Step 2: Sort edges in ascending order of weight using custom QuickSort
        if (allEdges.size() > 1) {
            quickSort(allEdges, 0, allEdges.size() - 1);
        }

        // Step 3: Initialize custom DisjointSet for all vertices
        DisjointSet<Integer> disjointSet = new DisjointSet<>(vCount);
        for (int i = 0; i < vCount; i++) {
            disjointSet.makeSet(vertices.get(i));
        }

        // Step 4 & 5: Process edges in ascending order and avoid cycles
        for (int i = 0; i < allEdges.size(); i++) {
            Edge edge = allEdges.get(i);
            int u = edge.getSource();
            int v = edge.getDestination();

            // If u and v belong to different sets, union them and include the edge
            if (disjointSet.union(u, v)) {
                mstEdges.add(edge);
                totalWeight += edge.getWeight();

                // Optimization: MST of V vertices always has exactly V - 1 edges
                if (mstEdges.size() == vCount - 1) {
                    break;
                }
            }
        }

        boolean fullyConnected = (mstEdges.size() == vCount - 1);
        return new Result(mstEdges, totalWeight, vCount, fullyConnected);
    }

    /**
     * Extracts all unique undirected edges from the graph.
     *
     * @param graph the source graph
     * @return dynamic array of unique edges
     */
    public static DynamicArray<Edge> getAllEdges(Graph graph) {
        DynamicArray<Edge> edges = new DynamicArray<>();
        List<Integer> vertices = graph.getVertices();

        for (int i = 0; i < vertices.size(); i++) {
            int u = vertices.get(i);
            HashSet<Integer> neighbours = graph.getNeighbours(u);
            List<Integer> neighbourList = neighbours.toList();

            for (int j = 0; j < neighbourList.size(); j++) {
                int v = neighbourList.get(j);

                // To avoid duplicate undirected edges (u, v) and (v, u), only add when u < v
                if (u < v) {
                    double weight = graph.getEdgeWeight(u, v);
                    edges.add(new Edge(u, v, weight));
                }
            }
        }
        return edges;
    }

    /**
     * Custom QuickSort implementation to sort edges by weight in ascending order.
     */
    private static void quickSort(DynamicArray<Edge> edges, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(edges, low, high);
            quickSort(edges, low, pivotIndex - 1);
            quickSort(edges, pivotIndex + 1, high);
        }
    }

    /**
     * Partition step of QuickSort based on edge weights.
     */
    private static int partition(DynamicArray<Edge> edges, int low, int high) {
        Edge pivot = edges.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (edges.get(j).getWeight() <= pivot.getWeight()) {
                i++;
                swap(edges, i, j);
            }
        }

        swap(edges, i + 1, high);
        return i + 1;
    }

    /**
     * Swaps two elements in the dynamic array.
     */
    private static void swap(DynamicArray<Edge> edges, int i, int j) {
        Edge temp = edges.get(i);
        edges.set(i, edges.get(j));
        edges.set(j, temp);
    }
}
