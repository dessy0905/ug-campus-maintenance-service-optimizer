package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.datastructures.queue.Queue;

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
        HashSet<Integer> visited = new HashSet<>();

        // Mark the starting vertex as visited
        visited.add(startVertex);
        queue.enqueue(startVertex);

        System.out.println("BFS Traversal:");

        while (!queue.isEmpty()) {

            int currentVertex = queue.dequeue();

            System.out.print(currentVertex + " ");

            // Visit all unvisited neighbours
            List<Integer> neighbours = graph.getNeighbours(currentVertex).toList();
            for (int i = 0; i < neighbours.size(); i++) {
                Integer neighbour = neighbours.get(i);

                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.enqueue(neighbour);
                }
            }
        }

        System.out.println();
    }
}
