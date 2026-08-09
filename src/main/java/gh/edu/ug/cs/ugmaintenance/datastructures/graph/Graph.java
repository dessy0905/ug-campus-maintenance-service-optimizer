package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom weighted graph for the campus route engine.
 *
 * <p>Vertices are identified by the integers {@code 0 .. vertexCount-1}. The
 * graph keeps <strong>both</strong> an adjacency list and an adjacency matrix
 * so the same structure can back every required representation:</p>
 * <ul>
 *   <li>adjacency list  - BFS/DFS traversal and Dijkstra relaxation;</li>
 *   <li>adjacency matrix - {@link #getWeight(int, int)} lookups and the
 *       O(V^2) array-based Dijkstra/Prim minimum scans.</li>
 * </ul>
 *
 * <p>Road distances and travel times on campus are never negative, so edge
 * weights are validated at insertion time. This guarantees Dijkstra's
 * precondition: no negative-weight edges can exist in the graph.</p>
 */
public class Graph {

    /** Sentinel returned by {@link #getWeight(int, int)} when no edge exists. */
    public static final double NO_EDGE = Double.POSITIVE_INFINITY;

    private final int vertexCount;
    private final boolean directed;
    private final List<List<Edge>> adjacency;
    private final double[][] matrix;
    private final List<Edge> edges;

    /**
     * Builds an undirected graph with the given number of vertices.
     */
    public Graph(int vertexCount) {
        this(vertexCount, false);
    }

    /**
     * Builds a graph with the given number of vertices.
     *
     * @param vertexCount number of vertices; must be greater than zero
     * @param directed    when true, edges are one-way
     */
    public Graph(int vertexCount, boolean directed) {
        if (vertexCount <= 0) {
            throw new IllegalArgumentException("Vertex count must be greater than zero.");
        }
        this.vertexCount = vertexCount;
        this.directed = directed;
        this.adjacency = new ArrayList<>(vertexCount);
        this.matrix = new double[vertexCount][vertexCount];
        this.edges = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            adjacency.add(new ArrayList<>());
            for (int j = 0; j < vertexCount; j++) {
                matrix[i][j] = NO_EDGE;
            }
        }
    }

    public void addEdge(int from, int to, double weight) {
        addEdge(new Edge(from, to, weight));
    }

    /**
     * Adds a weighted edge. In an undirected graph the reverse direction is
     * added implicitly. Adding the same edge more than once keeps both copies
     * in the adjacency/edge lists while the matrix retains the latest weight;
     * the seed data is unique, so this should not happen in practice.
     */
    public void addEdge(Edge edge) {
        validateVertex(edge.getFrom());
        validateVertex(edge.getTo());
        if (edge.getWeight() < 0.0) {
            throw new IllegalArgumentException("Edge weight cannot be negative: " + edge.getWeight());
        }

        adjacency.get(edge.getFrom()).add(edge);
        matrix[edge.getFrom()][edge.getTo()] = edge.getWeight();

        if (!directed) {
            Edge reverse = new Edge(edge.getTo(), edge.getFrom(), edge.getWeight());
            adjacency.get(edge.getTo()).add(reverse);
            matrix[edge.getTo()][edge.getFrom()] = edge.getWeight();
        }

        edges.add(edge);
    }

    /**
     * Returns the edges incident to the given vertex (read-only). In an
     * undirected graph the mirrored reverse edges have {@code getFrom() !=
     * vertex}, so use {@link Edge#getOther(int)} to obtain the far endpoint.
     */
    public List<Edge> getNeighbors(int vertex) {
        validateVertex(vertex);
        return Collections.unmodifiableList(adjacency.get(vertex));
    }

    /**
     * Returns the weight of the edge from {@code from} to {@code to}, or
     * {@link #NO_EDGE} when no such edge exists.
     */
    public double getWeight(int from, int to) {
        validateVertex(from);
        validateVertex(to);
        return matrix[from][to];
    }

    public boolean hasEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);
        return matrix[from][to] != NO_EDGE;
    }

    /**
     * Number of outgoing edges from a vertex (both directions in undirected
     * graphs).
     */
    public int getDegree(int vertex) {
        validateVertex(vertex);
        return adjacency.get(vertex).size();
    }

    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Number of unique edges added (each addEdge counts once, even in an
     * undirected graph where it is mirrored).
     */
    public int getEdgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    /**
     * The list of unique edges, as added (read-only). Kruskal's algorithm
     * consumes this.
     */
    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<List<Edge>> getAdjacencyList() {
        return Collections.unmodifiableList(adjacency);
    }

    /**
     * The adjacency matrix (read by convention — do not mutate): entry
     * {@code [from][to]} is the edge weight or {@link #NO_EDGE}.
     */
    public double[][] getAdjacencyMatrix() {
        return matrix;
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= vertexCount) {
            throw new IllegalArgumentException(
                    "Vertex out of range [0, " + (vertexCount - 1) + "]: " + vertex);
        }
    }

    @Override
    public String toString() {
        return "Graph{vertices=" + vertexCount
                + ", edges=" + edges.size()
                + ", directed=" + directed + "}";
    }
}
