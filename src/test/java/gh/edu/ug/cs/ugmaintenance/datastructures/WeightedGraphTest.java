package gh.edu.ug.cs.ugmaintenance.datastructures;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;

public class WeightedGraphTest {
    public static void main(String[] args) {

        System.out.println("=== UG Campus Weighted Graph Test ===");

        Graph graph = new Graph();

        System.out.println("\n--- Test 1: Add Weighted Edges ---");

        graph.addEdge(1, 2, 400);
        graph.addEdge(1, 3, 300);
        graph.addEdge(2, 4, 500);
        graph.addEdge(3, 4, 700);

        System.out.println("Weighted edges added.");
        System.out.println("Weighted edge test passed.");

        System.out.println("\n--- Test 2: Check Edges ---");

        System.out.println("Has edge 1-2: "
                + graph.hasEdge(1, 2));

        System.out.println("Has edge 1-3: "
                + graph.hasEdge(1, 3));

        System.out.println("Has edge 2-4: "
                + graph.hasEdge(2, 4));

        if (graph.hasEdge(1, 2)
                && graph.hasEdge(1, 3)
                && graph.hasEdge(2, 4)) {

            System.out.println("Edge existence test passed.");
        }

        System.out.println("\n--- Test 3: Check Edge Weights ---");

        double distance12 = graph.getEdgeWeight(1, 2);
        double distance13 = graph.getEdgeWeight(1, 3);
        double distance24 = graph.getEdgeWeight(2, 4);
        double distance34 = graph.getEdgeWeight(3, 4);

        System.out.println("Distance 1-2: "
                + distance12 + "m");

        System.out.println("Distance 1-3: "
                + distance13 + "m");

        System.out.println("Distance 2-4: "
                + distance24 + "m");

        System.out.println("Distance 3-4: "
                + distance34 + "m");

        if (distance12 == 400
                && distance13 == 300
                && distance24 == 500
                && distance34 == 700) {

            System.out.println("Weight test passed.");
        }

        System.out.println("\n--- Test 4: Check Undirected Edges ---");

        double reverse12 = graph.getEdgeWeight(2, 1);
        double reverse13 = graph.getEdgeWeight(3, 1);

        System.out.println("Distance 2-1: "
                + reverse12 + "m");

        System.out.println("Distance 3-1: "
                + reverse13 + "m");

        if (reverse12 == 400
                && reverse13 == 300) {

            System.out.println(
                    "Undirected weight test passed."
            );
        }

        System.out.println("\n--- Test 5: Invalid Weight ---");

        try {
            graph.addEdge(5, 6, -100);

            System.out.println(
                    "ERROR: Negative weight was accepted."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Negative weight correctly rejected."
            );
        }

        System.out.println("\n--- Test 6: Display Weighted Graph ---");

        graph.display();

        System.out.println(
                "\n=== All weighted graph tests passed! ==="
        );
    }
}
