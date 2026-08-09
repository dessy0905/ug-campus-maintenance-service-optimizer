package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

/**
 * A weighted edge between two vertices of the campus road graph.
 *
 * <p>In an undirected graph the edge is stored once here (from {@code from} to
 * {@code to}) while the adjacency list mirrors it in both directions.</p>
 */
public class Edge {

    private final int from;
    private final int to;
    private final double weight;

    public Edge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    /**
     * Returns the endpoint opposite to {@code vertex}. This is the safe way
     * to iterate a neighbour list: in an undirected graph the mirrored
     * reverse edges have {@code getFrom() != vertex}, so callers should use
     * {@code edge.getOther(vertex)} instead of {@code getTo()}.
     *
     * @param vertex one of the two endpoints
     * @return the other endpoint
     * @throws IllegalArgumentException when {@code vertex} is not an endpoint
     */
    public int getOther(int vertex) {
        if (vertex == from) {
            return to;
        }
        if (vertex == to) {
            return from;
        }
        throw new IllegalArgumentException(
                "Vertex " + vertex + " is not an endpoint of " + this);
    }

    @Override
    public String toString() {
        return "Edge(" + from + " -> " + to + ", w=" + weight + ")";
    }
}
