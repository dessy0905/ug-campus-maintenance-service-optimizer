package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import gh.edu.ug.cs.ugmaintenance.datastructures.stack.Stack;
import gh.edu.ug.cs.ugmaintenance.datastructures.hashtable.Set;

public class DFS {

    /**
     * Performs Depth-First Search starting from the given vertex.
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

        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new Set<>();

        // Start the traversal
        stack.push(startVertex);

        System.out.println("DFS Traversal:");

        while (!stack.isEmpty()) {

            int currentVertex = stack.pop();

            // Skip vertices that have already been visited
            if (visited.contains(currentVertex)) {
                continue;
            }

            visited.add(currentVertex);

            System.out.print(currentVertex + " ");

            // Add unvisited neighbours to the stack
            for (Integer neighbour : graph.getNeighbours(currentVertex).toList()) {

                if (!visited.contains(neighbour)) {
                    stack.push(neighbour);
                }
            }
        }

        System.out.println();
    }
}
