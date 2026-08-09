package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Edge;

/**
 * Outcome of a minimum-spanning-tree run (Prim or Kruskal): the chosen edges,
 * the total weight, and an optional step-by-step trace used as evidence in the
 * written report.
 */
public class MstResult {

    private final List<Edge> edges;
    private final double totalWeight;
    private final List<String> trace;

    public MstResult(List<Edge> edges, double totalWeight, List<String> trace) {
        this.edges = new ArrayList<>(edges);
        this.totalWeight = totalWeight;
        this.trace = new ArrayList<>(trace);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public List<String> getTrace() {
        return Collections.unmodifiableList(trace);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MST total weight = ").append(totalWeight).append('\n');
        for (Edge edge : edges) {
            sb.append("  ").append(edge).append('\n');
        }
        return sb.toString().trim();
    }
}
