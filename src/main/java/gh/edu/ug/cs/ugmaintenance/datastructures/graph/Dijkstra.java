package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashMap;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.LinkedList;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

public class Dijkstra {

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
     */
    public HashMap<Integer, Double> shortestDistances(
            int startLocationId) {

        if (!graph.containsVertex(startLocationId)) {
            throw new IllegalArgumentException(
                    "Start location does not exist: "
                            + startLocationId
            );
        }

        HashMap<Integer, Double> distances =
                new HashMap<>();

        HashSet<Integer> visited =
                new HashSet<>();

        List<Integer> vertices =
                graph.getVertices();

        for (int i = 0; i < vertices.size(); i++) {

            Integer vertex = vertices.get(i);

            distances.put(vertex, INFINITY);
        }

        distances.put(startLocationId, 0.0);

        while (visited.size() < graph.size()) {

            Integer currentVertex =
                    findClosestUnvisitedVertex(
                            distances,
                            visited
                    );

            if (currentVertex == null) {
                break;
            }

            visited.add(currentVertex);

            HashSet<Integer> neighbours =
                    graph.getNeighbours(currentVertex);

            List<Integer> neighbourList =
                    neighbours.toList();

            for (int i = 0; i < neighbourList.size(); i++) {

                Integer neighbour =
                        neighbourList.get(i);

                if (visited.contains(neighbour)) {
                    continue;
                }

                double edgeWeight =
                        graph.getEdgeWeight(
                                currentVertex,
                                neighbour
                        );

                double candidateDistance =
                        distances.get(currentVertex)
                                + edgeWeight;

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
     * Finds the shortest route from a starting location
     * to a destination location.
     *
     * Returns the locations in order.
     *
     * Example:
     *
     * 1 -> 2 -> 4
     */
    public List<Integer> shortestPath(
            int startLocationId,
            int destinationLocationId) {

        if (!graph.containsVertex(startLocationId)) {
            throw new IllegalArgumentException(
                    "Start location does not exist: "
                            + startLocationId
            );
        }

        if (!graph.containsVertex(destinationLocationId)) {
            throw new IllegalArgumentException(
                    "Destination location does not exist: "
                            + destinationLocationId
            );
        }

        /*
         * Stores the shortest known distance
         * to every location.
         */
        HashMap<Integer, Double> distances =
                new HashMap<>();

        /*
         * Stores the previous location used to
         * reach each location on the shortest path.
         *
         * Example:
         *
         * predecessor[2] = 1
         * predecessor[4] = 2
         *
         * This gives:
         *
         * 1 -> 2 -> 4
         */
        HashMap<Integer, Integer> predecessors =
                new HashMap<>();

        /*
         * Keeps track of finalized vertices.
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
         * Starting location has distance zero.
         */
        distances.put(startLocationId, 0.0);

        /*
         * Dijkstra's algorithm.
         */
        while (visited.size() < graph.size()) {

            Integer currentVertex =
                    findClosestUnvisitedVertex(
                            distances,
                            visited
                    );

            /*
             * No more reachable vertices.
             */
            if (currentVertex == null) {
                break;
            }

            visited.add(currentVertex);

            /*
             * We have reached the destination.
             *
             * Because Dijkstra has finalized this
             * vertex, its shortest distance is known.
             */
            if (currentVertex == destinationLocationId) {
                break;
            }

            HashSet<Integer> neighbours =
                    graph.getNeighbours(currentVertex);

            List<Integer> neighbourList =
                    neighbours.toList();

            for (int i = 0; i < neighbourList.size(); i++) {

                Integer neighbour =
                        neighbourList.get(i);

                if (visited.contains(neighbour)) {
                    continue;
                }

                double edgeWeight =
                        graph.getEdgeWeight(
                                currentVertex,
                                neighbour
                        );

                double candidateDistance =
                        distances.get(currentVertex)
                                + edgeWeight;

                /*
                 * Found a shorter route.
                 */
                if (candidateDistance
                        < distances.get(neighbour)) {

                    distances.put(
                            neighbour,
                            candidateDistance
                    );

                    /*
                     * Remember how we reached
                     * this neighbour.
                     */
                    predecessors.put(
                            neighbour,
                            currentVertex
                    );
                }
            }
        }

        /*
         * If the destination still has infinite
         * distance, there is no route.
         */
        if (distances.get(destinationLocationId)
                == INFINITY) {

            return new LinkedList<>();
        }

        /*
         * Reconstruct the path by walking backwards
         * from destination to start.
         */
        List<Integer> reversedPath =
                new LinkedList<>();

        Integer current =
                destinationLocationId;

        reversedPath.add(current);

        while (current != startLocationId) {

            Integer predecessor =
                    predecessors.get(current);

            if (predecessor == null) {
                return new LinkedList<>();
            }

            reversedPath.add(predecessor);

            current = predecessor;
        }

        /*
         * Reverse the path so that it goes:
         *
         * start -> destination
         */
        List<Integer> path =
                new LinkedList<>();

        for (int i = reversedPath.size() - 1;
             i >= 0;
             i--) {

            path.add(reversedPath.get(i));
        }

        return path;
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