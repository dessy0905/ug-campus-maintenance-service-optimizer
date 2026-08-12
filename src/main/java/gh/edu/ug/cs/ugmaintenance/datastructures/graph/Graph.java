package gh.edu.ug.cs.ugmaintenance.datastructures.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashMap;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashSet;

public class Graph {

    /*
     * Weighted adjacency list.
     *
     * Example:
     *
     * 1 -> {2=400.0, 3=300.0}
     *
     * This means:
     * Location 1 is connected to Location 2 with
     * a distance of 400 metres.
     *
     * Location 1 is connected to Location 3 with
     * a distance of 300 metres.
     */
    private final HashMap<Integer, HashMap<Integer, Double>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    /*
     * Add a location to the graph.
     */
    public void addVertex(int locationId) {

        validateLocationId(locationId);

        if (!adjacencyList.containsKey(locationId)) {
            adjacencyList.put(locationId, new HashMap<>());
        }
    }

    /*
     * Add an UNWEIGHTED edge.
     *
     * This method is kept so your existing graph tests
     * can still work.
     *
     * Default weight = 1.0
     */
    public void addEdge(int fromLocationId, int toLocationId) {

        addEdge(fromLocationId, toLocationId, 1.0);
    }

    /*
     * Add a WEIGHTED edge.
     *
     * Example:
     *
     * graph.addEdge(1, 2, 400);
     *
     * means location 1 and location 2
     * are 400 metres apart.
     */
    public void addEdge(
            int fromLocationId,
            int toLocationId,
            double weight) {

        validateLocationId(fromLocationId);
        validateLocationId(toLocationId);

        if (fromLocationId == toLocationId) {
            throw new IllegalArgumentException(
                    "A location cannot have an edge to itself: "
                            + fromLocationId
            );
        }

        if (weight <= 0) {
            throw new IllegalArgumentException(
                    "Edge weight must be greater than zero."
            );
        }

        addVertex(fromLocationId);
        addVertex(toLocationId);

        /*
         * Because this is an undirected graph,
         * we store the edge in both directions.
         */
        adjacencyList
                .get(fromLocationId)
                .put(toLocationId, weight);

        adjacencyList
                .get(toLocationId)
                .put(fromLocationId, weight);
    }

    /*
     * Get neighbouring locations.
     */
    public HashSet<Integer> getNeighbours(int locationId) {

        validateLocationId(locationId);

        HashMap<Integer, Double> neighbours =
                adjacencyList.get(locationId);

        if (neighbours == null) {
            throw new IllegalArgumentException(
                    "Location does not exist in graph: "
                            + locationId
            );
        }

        HashSet<Integer> result = new HashSet<>();

        List<Integer> neighbourIds =
                neighbours.keySet();

        for (int i = 0; i < neighbourIds.size(); i++) {
            result.add(neighbourIds.get(i));
        }

        return result;
    }

    /*
     * Get the weight/distance between two locations.
     */
    public double getEdgeWeight(
            int fromLocationId,
            int toLocationId) {

        validateLocationId(fromLocationId);
        validateLocationId(toLocationId);

        if (!adjacencyList.containsKey(fromLocationId)) {
            throw new IllegalArgumentException(
                    "Location does not exist: "
                            + fromLocationId
            );
        }

        HashMap<Integer, Double> neighbours =
                adjacencyList.get(fromLocationId);

        Double weight = neighbours.get(toLocationId);

        if (weight == null) {
            throw new IllegalArgumentException(
                    "No edge exists between "
                            + fromLocationId
                            + " and "
                            + toLocationId
            );
        }

        return weight;
    }

    /*
     * Check whether a vertex exists.
     */
    public boolean containsVertex(int locationId) {

        validateLocationId(locationId);

        return adjacencyList.containsKey(locationId);
    }

    /*
     * Check whether an edge exists.
     */
    public boolean hasEdge(
            int fromLocationId,
            int toLocationId) {

        validateLocationId(fromLocationId);
        validateLocationId(toLocationId);

        if (!adjacencyList.containsKey(fromLocationId)) {
            return false;
        }

        return adjacencyList
                .get(fromLocationId)
                .containsKey(toLocationId);
    }

    /*
     * Remove an edge.
     */
    public boolean removeEdge(
            int fromLocationId,
            int toLocationId) {

        validateLocationId(fromLocationId);
        validateLocationId(toLocationId);

        if (!adjacencyList.containsKey(fromLocationId)
                || !adjacencyList.containsKey(toLocationId)) {

            return false;
        }

        Double removedFromFirst =
                adjacencyList
                        .get(fromLocationId)
                        .remove(toLocationId);

        Double removedFromSecond =
                adjacencyList
                        .get(toLocationId)
                        .remove(fromLocationId);

        return removedFromFirst != null
                || removedFromSecond != null;
    }

    /*
     * Remove a vertex and all edges connected to it.
     */
    public boolean removeVertex(int locationId) {

        validateLocationId(locationId);

        if (!adjacencyList.containsKey(locationId)) {
            return false;
        }

        HashMap<Integer, Double> neighbours =
                adjacencyList.get(locationId);

        List<Integer> neighbourList =
                neighbours.keySet();

        for (int i = 0; i < neighbourList.size(); i++) {

            Integer neighbourId =
                    neighbourList.get(i);

            HashMap<Integer, Double> neighbourMap =
                    adjacencyList.get(neighbourId);

            if (neighbourMap != null) {
                neighbourMap.remove(locationId);
            }
        }

        adjacencyList.remove(locationId);

        return true;
    }

    /*
     * Number of vertices.
     */
    public int size() {
        return adjacencyList.size();
    }

    /*
     * Check whether graph is empty.
     */
    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    /*
     * Remove everything from the graph.
     */
    public void clear() {
        adjacencyList.clear();
    }

    /*
     * Get all vertices.
     */
    public List<Integer> getVertices() {
        return adjacencyList.keySet();
    }

    /*
     * Get degree of a vertex.
     */
    public int getDegree(int locationId) {

        return getNeighbours(locationId).size();
    }

    /*
     * Display the weighted graph.
     */
    public void display() {

        System.out.println("Weighted Graph:");

        List<Integer> vertices =
                adjacencyList.keySet();

        for (int i = 0; i < vertices.size(); i++) {

            Integer vertex = vertices.get(i);

            HashMap<Integer, Double> neighbours =
                    adjacencyList.get(vertex);

            System.out.println(
                    vertex + " -> " + neighbours
            );
        }
    }

    /*
     * Validate location IDs.
     */
    private void validateLocationId(int locationId) {

        if (locationId < 0) {
            throw new IllegalArgumentException(
                    "Location ID cannot be negative: "
                            + locationId
            );
        }
    }
}