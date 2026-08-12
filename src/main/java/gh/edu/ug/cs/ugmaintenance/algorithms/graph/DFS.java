package gh.edu.ug.cs.ugmaintenance.algorithms.graph;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashSet;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.datastructures.stack.Stack;

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
        HashSet<Integer> visited = new HashSet<>();

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
            List<Integer> neighbours = graph.getNeighbours(currentVertex).toList();
            for (int i = 0; i < neighbours.size(); i++) {
                Integer neighbour = neighbours.get(i);

                if (!visited.contains(neighbour)) {
                    stack.push(neighbour);
                }
            }
        }

        System.out.println();
    }
}
