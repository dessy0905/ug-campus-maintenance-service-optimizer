package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Edge;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;

/**
 * Prim's minimum-spanning-tree algorithm (array-based O(V^2 + E)).
 *
 * <p>Requires a connected, undirected graph. Starting from vertex 0, the
 * algorithm repeatedly grows the tree by adding the cheapest edge that links
 * the tree to a vertex outside it. The parent pointers recorded while growing
 * the tree form the MST edge list.</p>
 *
 * <p>No built-in priority queue is used: the next vertex is chosen with a
 * plain array scan so the assessed core logic is fully custom.</p>
 */
public class Prim {

    /**
     * Computes the minimum spanning tree of a connected, undirected graph.
     *
     * @throws IllegalStateException when the graph is not connected (no
     *         spanning tree exists)
     */
    public static MstResult minimumSpanningTree(Graph graph) {
        int n = graph.getVertexCount();

        double[] key = new double[n];
        int[] parent = new int[n];
        boolean[] inMst = new boolean[n];

        Arrays.fill(key, Graph.NO_EDGE);
        Arrays.fill(parent, -1);
        key[0] = 0.0;

        List<Edge> mstEdges = new ArrayList<>();
        List<String> trace = new ArrayList<>();
        double totalWeight = 0.0;

        for (int step = 0; step < n; step++) {
            int current = selectMinNotInMst(key, inMst);
            if (current == -1) {
                throw new IllegalStateException(
                        "Graph is not connected — no spanning tree exists.");
            }

            inMst[current] = true;

            if (parent[current] != -1) {
                Edge treeEdge = new Edge(parent[current], current, key[current]);
                mstEdges.add(treeEdge);
                totalWeight += key[current];
                trace.add("Add " + treeEdge + " (tree size = " + mstEdges.size() + ")");
            }

            for (Edge edge : graph.getNeighbors(current)) {
                int next = edge.getOther(current);
                if (!inMst[next] && edge.getWeight() < key[next]) {
                    key[next] = edge.getWeight();
                    parent[next] = current;
                }
            }
        }

        return new MstResult(mstEdges, totalWeight, trace);
    }

    private static int selectMinNotInMst(double[] key, boolean[] inMst) {
        int best = -1;
        double bestKey = Graph.NO_EDGE;
        for (int v = 0; v < key.length; v++) {
            if (!inMst[v] && key[v] < bestKey) {
                bestKey = key[v];
                best = v;
            }
        }
        return best;
    }

    public static long timedMinimumSpanningTree(Graph graph) {
        long start = System.nanoTime();
        minimumSpanningTree(graph);
        return System.nanoTime() - start;
    }
}
