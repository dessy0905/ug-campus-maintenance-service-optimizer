package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import gh.edu.ug.cs.ugmaintenance.datastructures.queue.Queue;
import gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.Set;

public class BFS {

    /**
     * Performs Breadth-First Search starting from the given vertex.
     *
     * @param graph the graph to search
     * @param startVertex the vertex where the search begins
     */
    public static void traverse(Graph graph, int startVertex) {

        if (!graph.containsVertex(startVertex)) {
            throw new IllegalArgumentException(
                    "Start vertex does not exist: " + startVertex
            );
        }

        Queue<Integer> queue = new Queue<>();
        Set<Integer> visited = new Set<>();

        // Mark the starting vertex as visited
        visited.add(startVertex);
        queue.enqueue(startVertex);

        System.out.println("BFS Traversal:");

        while (!queue.isEmpty()) {

            int currentVertex = queue.dequeue();

            System.out.print(currentVertex + " ");

            // Visit all unvisited neighbours
            for (Integer neighbour : graph.getNeighbours(currentVertex).toList()) {

                if (!visited.contains(neighbour)) {

                    visited.add(neighbour);
                    queue.enqueue(neighbour);
                }
            }
        }

        System.out.println();
    }
}
