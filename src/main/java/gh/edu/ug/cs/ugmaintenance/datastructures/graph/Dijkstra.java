package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashMap;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

public class Dijkstra {

    /*
     * Represents infinity.
     *
     * A very large value is used instead of Double.POSITIVE_INFINITY
     * so that the algorithm remains easy to understand.
     */
    private static final double INFINITY = Double.MAX_VALUE;

    private final Graph graph;

    public Dijkstra(Graph graph) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "Graph cannot be null."
            );
        }

        this.graph = graph;
    }

    /**
     * Finds the shortest distances from a starting location
     * to every other location in the graph.
     *
     * Returns:
     *
     * locationId -> shortest distance from start
     */
    public HashMap<Integer, Double> shortestDistances(
            int startLocationId) {

        if (!graph.containsVertex(startLocationId)) {
            throw new IllegalArgumentException(
                    "Start location does not exist: "
                            + startLocationId
            );
        }

        /*
         * Stores the shortest known distance to each vertex.
         *
         * Example:
         *
         * 1 -> 0
         * 2 -> 400
         * 3 -> 300
         * 4 -> 900
         */
        HashMap<Integer, Double> distances =
                new HashMap<>();

        /*
         * Keeps track of vertices whose shortest distance
         * has already been finalized.
         */
        HashSet<Integer> visited =
                new HashSet<>();

        /*
         * Initially every vertex has infinite distance.
         */
        List<Integer> vertices =
                graph.getVertices();

        for (int i = 0; i < vertices.size(); i++) {

            Integer vertex = vertices.get(i);

            distances.put(vertex, INFINITY);
        }

        /*
         * Distance from the starting vertex to itself is 0.
         */
        distances.put(startLocationId, 0.0);

        /*
         * Continue until every reachable vertex has been processed.
         */
        while (visited.size() < graph.size()) {

            Integer currentVertex =
                    findClosestUnvisitedVertex(
                            distances,
                            visited
                    );

            /*
             * If there is no reachable unvisited vertex,
             * the remaining vertices are disconnected.
             */
            if (currentVertex == null) {
                break;
            }

            /*
             * Mark the current vertex as finalized.
             */
            visited.add(currentVertex);

            /*
             * Examine all neighbours.
             */
            HashSet<Integer> neighbours =
                    graph.getNeighbours(currentVertex);

            List<Integer> neighbourList =
                    neighbours.toList();

            for (int i = 0; i < neighbourList.size(); i++) {

                Integer neighbour =
                        neighbourList.get(i);

                /*
                 * Already finalized vertices do not
                 * need to be processed again.
                 */
                if (visited.contains(neighbour)) {
                    continue;
                }

                /*
                 * Distance from the current vertex
                 * to this neighbour.
                 */
                double edgeWeight =
                        graph.getEdgeWeight(
                                currentVertex,
                                neighbour
                        );

                /*
                 * Candidate distance:
                 *
                 * distance(start → current)
                 * +
                 * distance(current → neighbour)
                 */
                double candidateDistance =
                        distances.get(currentVertex)
                                + edgeWeight;

                /*
                 * If this route is shorter than the
                 * currently known route, update it.
                 */
                if (candidateDistance
                        < distances.get(neighbour)) {

                    distances.put(
                            neighbour,
                            candidateDistance
                    );
                }
            }
        }

        return distances;
    }

    /**
     * Finds the unvisited vertex with the smallest
     * currently known distance.
     */
    private Integer findClosestUnvisitedVertex(
            HashMap<Integer, Double> distances,
            HashSet<Integer> visited) {

        List<Integer> vertices =
                distances.keySet();

        Integer closestVertex = null;

        double shortestDistance = INFINITY;

        for (int i = 0; i < vertices.size(); i++) {

            Integer vertex = vertices.get(i);

            if (visited.contains(vertex)) {
                continue;
            }

            double distance =
                    distances.get(vertex);

            if (distance < shortestDistance) {

                shortestDistance = distance;

                closestVertex = vertex;
            }
        }

        return closestVertex;
    }
}
