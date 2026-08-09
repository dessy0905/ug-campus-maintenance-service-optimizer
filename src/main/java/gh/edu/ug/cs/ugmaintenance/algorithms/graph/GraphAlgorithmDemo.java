package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import java.util.List;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import gh.edu.ug.cs.ugmaintenance.models.Road;
import gh.edu.ug.cs.ugmaintenance.repositories.RoadRepository;

/**
 * Console demonstration of the graph algorithms on the real UG campus data.
 *
 * <p>Reads the {@code roads} table from the database through
 * {@link RoadRepository}, builds the custom {@link Graph}, then runs
 * Dijkstra (shortest route between two locations), Prim (MST) and Kruskal
 * (MST with connectivity trace).</p>
 *
 * <p>Requires a running MySQL database seeded with the project schema and
 * CSV data. Run with:</p>
 * <pre>
 *   mvn compile exec:java -Dexec.mainClass=...
 * </pre>
 */
public class GraphAlgorithmDemo {

    public static void main(String[] args) {
        try {
            Graph graph = loadCampusGraph();
            System.out.println("Loaded campus graph: " + graph);

            if (graph.getVertexCount() >= 2) {
                int source = 0;
                int target = 1;
                runDijkstra(graph, source, target);
            } else {
                System.out.println("Not enough locations to run Dijkstra.");
            }

            runPrim(graph);
            runKruskal(graph);
        } catch (Throwable e) {
            // DatabaseConfig throws an Error from its static initializer when
            // application.properties is missing, so catch Throwable here to
            // keep the demo from crashing the JVM.
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            System.out.println("Demo could not run: " + cause.getMessage());
        }
    }

    /**
     * Loads every road from the database into a custom Graph. Location IDs in
     * the database are 1-based, so vertex {@code i} represents location
     * {@code i + 1}.
     */
    public static Graph loadCampusGraph() {
        List<Road> roads = new RoadRepository().findAll();
        if (roads.isEmpty()) {
            throw new IllegalStateException("No roads found in the database.");
        }

        int maxLocationId = 0;
        for (Road road : roads) {
            maxLocationId = Math.max(maxLocationId, Math.max(
                    road.getFromLocationId(), road.getToLocationId()));
        }

        Graph graph = new Graph(maxLocationId + 1);
        for (Road road : roads) {
            graph.addEdge(
                    road.getFromLocationId() - 1,
                    road.getToLocationId() - 1,
                    road.getDistanceKm());
        }
        return graph;
    }

    private static void runDijkstra(Graph graph, int source, int target) {
        System.out.println("\n=== Dijkstra: shortest route ===");
        Dijkstra.ShortestPathResult result = Dijkstra.findShortestPaths(graph, source);
        System.out.println(result.formatDistanceTable());
        System.out.println("Route from " + source + " to " + target + ": "
                + Dijkstra.findShortestPath(graph, source, target)
                + " (distance = " + result.distanceTo(target) + " km)");
    }

    private static void runPrim(Graph graph) {
        System.out.println("\n=== Prim: minimum spanning tree ===");
        MstResult mst = Prim.minimumSpanningTree(graph);
        System.out.println(mst);
    }

    private static void runKruskal(Graph graph) {
        System.out.println("\n=== Kruskal: minimum spanning tree ===");
        MstResult mst = Kruskal.minimumSpanningTree(graph);
        System.out.println(mst);
        System.out.println("Connectivity trace:");
        for (String step : mst.getTrace()) {
            System.out.println("  " + step);
        }
    }
}
