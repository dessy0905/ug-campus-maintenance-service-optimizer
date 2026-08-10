package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import java.util.ArrayList;
import java.util.List;

import gh.edu.ug.cs.ugmaintenance.datastructures.disjointset.DisjointSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Edge;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;

/**
 * Kruskal's minimum-spanning-tree algorithm.
 *
 * <p>Sorts all edges by weight (with a self-contained insertion sort — no
 * built-in collection is used for assessed logic), then greedily adds each
 * edge that does not create a cycle. Cycle detection uses the custom
 * {@link DisjointSet} (union-find with path compression and union by rank).</p>
 *
 * <p>The returned {@link MstResult} includes a connectivity trace — one line
 * per edge stating whether it was accepted or skipped and how many components
 * remain — which is the required trace-table evidence for Kruskal.</p>
 */
public class Kruskal {

    /**
     * Computes the minimum spanning tree of a connected, undirected graph.
     *
     * @throws IllegalStateException when the graph is not connected (fewer
     *         than V-1 edges can be accepted)
     */
    public static MstResult minimumSpanningTree(Graph graph) {
        int n = graph.getVertexCount();

        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        insertionSortByWeight(sortedEdges);

        DisjointSet sets = new DisjointSet(n);
        List<Edge> mstEdges = new ArrayList<>();
        List<String> trace = new ArrayList<>();
        double totalWeight = 0.0;

        for (Edge edge : sortedEdges) {
            if (mstEdges.size() == n - 1) {
                break;
            }

            if (sets.isConnected(edge.getFrom(), edge.getTo())) {
                trace.add("Skip   " + edge
                        + " (would form a cycle; components = " + sets.countComponents() + ")");
                continue;
            }

            sets.union(edge.getFrom(), edge.getTo());
            mstEdges.add(edge);
            totalWeight += edge.getWeight();
            trace.add("Accept " + edge
                    + " (components = " + sets.countComponents() + ")");
        }

        if (mstEdges.size() < n - 1) {
            throw new IllegalStateException(
                    "Graph is not connected — no spanning tree exists.");
        }

        return new MstResult(mstEdges, totalWeight, trace);
    }

    public static long timedMinimumSpanningTree(Graph graph) {
        long start = System.nanoTime();
        minimumSpanningTree(graph);
        return System.nanoTime() - start;
    }

    /**
     * Stable insertion sort over the edge list, ascending by weight (ties
     * broken by endpoint id so the trace is deterministic).
     */
    private static void insertionSortByWeight(List<Edge> edges) {
        for (int i = 1; i < edges.size(); i++) {
            Edge key = edges.get(i);
            int j = i - 1;
            while (j >= 0 && compareEdges(edges.get(j), key) > 0) {
                edges.set(j + 1, edges.get(j));
                j--;
            }
            edges.set(j + 1, key);
        }
    }

    private static int compareEdges(Edge a, Edge b) {
        int byWeight = Double.compare(a.getWeight(), b.getWeight());
        if (byWeight != 0) {
            return byWeight;
        }
        int byFrom = Integer.compare(a.getFrom(), b.getFrom());
        if (byFrom != 0) {
            return byFrom;
        }
        return Integer.compare(a.getTo(), b.getTo());
    }
}
