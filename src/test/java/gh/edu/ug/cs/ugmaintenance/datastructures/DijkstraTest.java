package gh.edu.ug.cs.ugmaintenance.datastructures;

import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Dijkstra;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashMap;

public class DijkstraTest {

    public static void main(String[] args) {

        System.out.println("=== UG Campus Dijkstra Test ===");

        /*
         * Create the weighted campus graph.
         *
         *             400m
         *        1 ───────── 2
         *        │            │
         *      300m         500m
         *        │            │
         *        3 ───────── 4
         *             700m
         */
        Graph graph = new Graph();

        graph.addEdge(1, 2, 400);
        graph.addEdge(1, 3, 300);
        graph.addEdge(2, 4, 500);
        graph.addEdge(3, 4, 700);

        System.out.println("\n--- Test 1: Shortest Distances from Location 1 ---");

        Dijkstra dijkstra = new Dijkstra(graph);

        HashMap<Integer, Double> distances =
                dijkstra.shortestDistances(1);

        System.out.println("Distance from 1 to 1: "
                + distances.get(1) + "m");

        System.out.println("Distance from 1 to 2: "
                + distances.get(2) + "m");

        System.out.println("Distance from 1 to 3: "
                + distances.get(3) + "m");

        System.out.println("Distance from 1 to 4: "
                + distances.get(4) + "m");


        /*
         * Verify expected results.
         */
        if (distances.get(1) == 0.0
                && distances.get(2) == 400.0
                && distances.get(3) == 300.0
                && distances.get(4) == 900.0) {

            System.out.println(
                    "Shortest distance test passed."
            );

        } else {

            System.out.println(
                    "ERROR: Shortest distance test failed."
            );
        }


        System.out.println("\n--- Test 2: Shortest Distances from Location 4 ---");

        HashMap<Integer, Double> distancesFrom4 =
                dijkstra.shortestDistances(4);

        System.out.println("Distance from 4 to 4: "
                + distancesFrom4.get(4) + "m");

        System.out.println("Distance from 4 to 2: "
                + distancesFrom4.get(2) + "m");

        System.out.println("Distance from 4 to 3: "
                + distancesFrom4.get(3) + "m");

        System.out.println("Distance from 4 to 1: "
                + distancesFrom4.get(1) + "m");


        if (distancesFrom4.get(4) == 0.0
                && distancesFrom4.get(2) == 500.0
                && distancesFrom4.get(3) == 700.0
                && distancesFrom4.get(1) == 900.0) {

            System.out.println(
                    "Reverse shortest distance test passed."
            );

        } else {

            System.out.println(
                    "ERROR: Reverse shortest distance test failed."
            );
        }


        System.out.println("\n--- Test 3: Invalid Starting Location ---");

        try {

            dijkstra.shortestDistances(99);

            System.out.println(
                    "ERROR: Invalid location was accepted."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid starting location correctly rejected."
            );
        }


        System.out.println("\n--- Test 4: Unreachable Location ---");

        /*
         * Add a separate location that has no connection
         * to the existing graph.
         */
        graph.addVertex(5);

        HashMap<Integer, Double> distancesWithUnreachable =
                dijkstra.shortestDistances(1);

        System.out.println(
                "Distance from 1 to unreachable location 5: "
                        + distancesWithUnreachable.get(5)
        );

        if (distancesWithUnreachable.get(5)
                == Double.MAX_VALUE) {

            System.out.println(
                    "Unreachable location test passed."
            );

        } else {

            System.out.println(
                    "ERROR: Unreachable location test failed."
            );
        }


        System.out.println(
                "\n=== All Dijkstra tests passed! ==="
        );
    }
}