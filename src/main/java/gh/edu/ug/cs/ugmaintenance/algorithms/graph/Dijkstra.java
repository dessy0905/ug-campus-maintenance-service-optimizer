package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Edge;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;

/**
 * Dijkstra's single-source shortest-path algorithm over the campus road graph.
 *
 * <p>The graph is weighted and may be directed or undirected. Because road
 * distances and travel times are never negative (enforced by {@link Graph}),
 * Dijkstra's greedy relaxation is correct.</p>
 *
 * <p>The core loop uses an array-based minimum scan (O(V^2 + E)) instead of a
 * built-in {@code PriorityQueue}, so every part of the assessed logic is
 * custom-built and easy to trace in the written report.</p>
 */
public class Dijkstra {

    /**
     * Runs Dijkstra from {@code source} and returns a result object holding
     * the final distance table and predecessor array (used to reconstruct
     * paths).
     */
    public static ShortestPathResult findShortestPaths(Graph graph, int source) {
        validateVertex(graph, source);

        int n = graph.getVertexCount();
        double[] distances = new double[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(distances, Graph.NO_EDGE);
        Arrays.fill(previous, -1);
        distances[source] = 0.0;

        for (int step = 0; step < n; step++) {
            int current = selectMinUnvisited(distances, visited);
            if (current == -1) {
                // Every remaining vertex is unreachable from the source.
                break;
            }
            visited[current] = true;

            for (Edge edge : graph.getNeighbors(current)) {
                int next = edge.getOther(current);
                double candidate = distances[current] + edge.getWeight();
                if (candidate < distances[next]) {
                    distances[next] = candidate;
                    previous[next] = current;
                }
            }
        }

        return new ShortestPathResult(graph, source, distances, previous);
    }

    private static int selectMinUnvisited(double[] distances, boolean[] visited) {
        int best = -1;
        double bestDistance = Graph.NO_EDGE;
        for (int v = 0; v < distances.length; v++) {
            if (!visited[v] && distances[v] < bestDistance) {
                bestDistance = distances[v];
                best = v;
            }
        }
        return best;
    }

    /**
     * Shortest distance from {@code source} to {@code target}, or
     * {@link Graph#NO_EDGE} when the target is unreachable.
     */
    public static double shortestDistance(Graph graph, int source, int target) {
        validateVertex(graph, target);
        return findShortestPaths(graph, source).distanceTo(target);
    }

    /**
     * Shortest path from {@code source} to {@code target} as an ordered list
     * of vertices, or an empty list when the target is unreachable.
     */
    public static List<Integer> findShortestPath(Graph graph, int source, int target) {
        validateVertex(graph, target);
        return findShortestPaths(graph, source).pathTo(target);
    }

    public static long timedShortestDistance(Graph graph, int source, int target) {
        long start = System.nanoTime();
        shortestDistance(graph, source, target);
        return System.nanoTime() - start;
    }

    private static void validateVertex(Graph graph, int vertex) {
        if (vertex < 0 || vertex >= graph.getVertexCount()) {
            throw new IllegalArgumentException(
                    "Vertex out of range [0, " + (graph.getVertexCount() - 1) + "]: " + vertex);
        }
    }

    /**
     * Outcome of one Dijkstra run: final distances, predecessors, path
     * reconstruction, and a formatted distance table for report evidence.
     */
    public static final class ShortestPathResult {

        private final Graph graph;
        private final int source;
        private final double[] distances;
        private final int[] previous;

        private ShortestPathResult(Graph graph, int source, double[] distances, int[] previous) {
            this.graph = graph;
            this.source = source;
            this.distances = distances;
            this.previous = previous;
        }

        public int getSource() {
            return source;
        }

        /** Copy of the final distance array (index = vertex). */
        public double[] getDistances() {
            return distances.clone();
        }

        /** Copy of the predecessor array (-1 = none). */
        public int[] getPrevious() {
            return previous.clone();
        }

        public double distanceTo(int vertex) {
            validateVertex(vertex);
            return distances[vertex];
        }

        /**
         * Reconstructs the shortest path to {@code vertex} by walking the
         * predecessor chain backwards. Empty list = unreachable.
         */
        public List<Integer> pathTo(int vertex) {
            validateVertex(vertex);
            if (distances[vertex] == Graph.NO_EDGE) {
                return new ArrayList<>();
            }

            List<Integer> path = new ArrayList<>();
            int current = vertex;
            while (current != -1) {
                path.add(current);
                if (current == source) {
                    break;
                }
                current = previous[current];
            }
            Collections.reverse(path);

            if (path.isEmpty() || path.get(0) != source) {
                return new ArrayList<>();
            }
            return path;
        }

        /**
         * Formatted distance table (vertex, distance, path) suitable for the
         * trace-table evidence required by the report.
         */
        public String formatDistanceTable() {
            StringBuilder table = new StringBuilder();
            table.append("Dijkstra from source ").append(source).append('\n');
            table.append(String.format("%-8s %-12s %s%n", "Vertex", "Distance", "Path"));
            for (int v = 0; v < distances.length; v++) {
                String distance = distances[v] == Graph.NO_EDGE
                        ? "inf"
                        : String.format("%.2f", distances[v]);
                table.append(String.format("%-8d %-12s %s%n", v, distance, formatPath(pathTo(v))));
            }
            return table.toString().trim();
        }

        private String formatPath(List<Integer> path) {
            if (path.isEmpty()) {
                return "unreachable";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) {
                    sb.append(" -> ");
                }
                sb.append(path.get(i));
            }
            return sb.toString();
        }

        private void validateVertex(int vertex) {
            if (vertex < 0 || vertex >= graph.getVertexCount()) {
                throw new IllegalArgumentException(
                        "Vertex out of range [0, " + (graph.getVertexCount() - 1) + "]: " + vertex);
            }
        }
    }
}
