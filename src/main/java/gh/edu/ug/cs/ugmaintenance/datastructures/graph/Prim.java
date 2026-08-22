package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.datastructures.queue.PriorityQueue;

/**
 * Custom Implementation of Prim's Minimum Spanning Tree (MST) Algorithm.
 *
 * <p>Prim's algorithm starts from an initial vertex and grows the MST one vertex
 * at a time by always selecting the minimum-weight edge that connects a vertex
 * in the MST to a vertex outside the MST.</p>
 *
 * <p>Algorithmic steps:
 * <ol>
 *   <li>Select a starting vertex and mark it as visited.</li>
 *   <li>Add all incident edges of the visited vertex to the custom {@link PriorityQueue}.</li>
 *   <li>Extract the minimum-weight edge from the priority queue.</li>
 *   <li>If the edge leads to an unvisited vertex, add it to the MST, mark the vertex as visited,
 *       and push its incident edges into the priority queue.</li>
 *   <li>Repeat until all reachable vertices are visited or the queue is empty.</li>
 * </ol>
 *
 * <p>Uses only custom data structures: {@link Graph}, {@link PriorityQueue},
 * {@link DynamicArray}, {@link HashSet}, and {@link List}.</p>
 */
public class Prim {

    /**
     * Edge representation tailored for Prim's algorithm with min-priority ordering.
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

        /**
         * Compares edges for priority queue ordering.
         * Note: The project's PriorityQueue is a max-heap where higher compareTo values
         * have higher priority. Therefore, smaller weights return a positive value to ensure
         * min-heap behavior (minimum weight extracted first).
         */
        @Override
        public int compareTo(Edge other) {
            return Double.compare(other.weight, this.weight);
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
     * Result container holding the MST edges, total weight, visited vertices count, and connectivity.
     */
    public static class Result {
        private final DynamicArray<Edge> mstEdges;
        private final double totalWeight;
        private final int totalVertices;
        private final int visitedVertices;
        private final boolean fullyConnected;

        public Result(DynamicArray<Edge> mstEdges, double totalWeight, int totalVertices, int visitedVertices, boolean fullyConnected) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.totalVertices = totalVertices;
            this.visitedVertices = visitedVertices;
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

        public int getTotalVertices() {
            return totalVertices;
        }

        public int getVisitedVertices() {
            return visitedVertices;
        }

        public boolean isFullyConnected() {
            return fullyConnected;
        }

        public void display() {
            System.out.println("=== Prim's Minimum Spanning Tree ===");
            System.out.println("Total Vertices in Graph: " + totalVertices);
            System.out.println("Visited Vertices: " + visitedVertices);
            System.out.println("Edges in MST: " + mstEdges.size() + (totalVertices > 0 ? " (Expected: " + (totalVertices - 1) + ")" : ""));
            System.out.println("Total MST Weight: " + totalWeight + "m");
            System.out.println("Fully Connected: " + fullyConnected);
            System.out.println("Selected Edges:");
            for (int i = 0; i < mstEdges.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + mstEdges.get(i));
            }
        }
    }

    private final Graph graph;

    public Prim(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null for Prim's algorithm");
        }
        this.graph = graph;
    }

    /**
     * Computes the MST starting from an automatically selected first vertex.
     *
     * @return {@link Result} containing MST edges and total weight
     */
    public Result computeMST() {
        if (graph.isEmpty()) {
            return new Result(new DynamicArray<>(), 0.0, 0, 0, true);
        }
        int startVertex = graph.getVertices().get(0);
        return computeMST(startVertex);
    }

    /**
     * Computes the MST starting from the given start vertex.
     *
     * @param startVertex the vertex where Prim's algorithm begins
     * @return {@link Result} containing MST edges and total weight
     */
    public Result computeMST(int startVertex) {
        return findMinimumSpanningTree(this.graph, startVertex);
    }

    /**
     * Static utility to compute MST starting from a default vertex.
     *
     * @param graph the source graph
     * @return {@link Result} containing MST edges and total weight
     */
    public static Result findMinimumSpanningTree(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        if (graph.isEmpty()) {
            return new Result(new DynamicArray<>(), 0.0, 0, 0, true);
        }
        int startVertex = graph.getVertices().get(0);
        return findMinimumSpanningTree(graph, startVertex);
    }

    /**
     * Static utility to compute MST starting from a specified vertex.
     *
     * @param graph the source graph
     * @param startVertex the starting vertex ID
     * @return {@link Result} containing MST edges and total weight
     */
    public static Result findMinimumSpanningTree(Graph graph, int startVertex) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }

        int totalVertices = graph.size();
        if (totalVertices == 0) {
            return new Result(new DynamicArray<>(), 0.0, 0, 0, true);
        }

        if (!graph.containsVertex(startVertex)) {
            throw new IllegalArgumentException("Start vertex does not exist in graph: " + startVertex);
        }

        // Single vertex base case
        if (totalVertices == 1) {
            return new Result(new DynamicArray<>(), 0.0, 1, 1, true);
        }

        DynamicArray<Edge> mstEdges = new DynamicArray<>();
        double totalWeight = 0.0;

        HashSet<Integer> visited = new HashSet<>();
        PriorityQueue<Edge> minPriorityQueue = new PriorityQueue<>();

        // Step 1: Start from the specified vertex
        visited.add(startVertex);
        addOutgoingEdges(graph, startVertex, visited, minPriorityQueue);

        // Step 2: Greedily pick the minimum edge connecting visited to unvisited
        while (!minPriorityQueue.isEmpty() && visited.size() < totalVertices) {
            Edge candidate = minPriorityQueue.poll();

            int u = candidate.getSource();
            int v = candidate.getDestination();

            // If both vertices are already in MST, this edge creates a cycle
            if (visited.contains(u) && visited.contains(v)) {
                continue;
            }

            // Identify the newly reached unvisited vertex
            int newVertex = visited.contains(u) ? v : u;

            // Include edge in MST
            visited.add(newVertex);
            mstEdges.add(candidate);
            totalWeight += candidate.getWeight();

            // Add outgoing edges from the new vertex to the priority queue
            addOutgoingEdges(graph, newVertex, visited, minPriorityQueue);
        }

        boolean fullyConnected = (visited.size() == totalVertices) && (mstEdges.size() == totalVertices - 1);
        return new Result(mstEdges, totalWeight, totalVertices, visited.size(), fullyConnected);
    }

    /**
     * Enqueues all incident edges of a vertex that lead to unvisited vertices.
     */
    private static void addOutgoingEdges(Graph graph, int vertex, HashSet<Integer> visited, PriorityQueue<Edge> pq) {
        HashSet<Integer> neighbours = graph.getNeighbours(vertex);
        List<Integer> neighbourList = neighbours.toList();

        for (int i = 0; i < neighbourList.size(); i++) {
            int neighbour = neighbourList.get(i);
            if (!visited.contains(neighbour)) {
                double weight = graph.getEdgeWeight(vertex, neighbour);
                pq.offer(new Edge(vertex, neighbour, weight));
            }
        }
    }
}
