package gh.edu.ug.cs.ugmaintenance.services;

import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.Technician;

public class RouteServiceTest {

    public static void main(String[] args) {

        System.out.println("=== UG Maintenance Route Service Test ===");
        System.out.println();

        RouteService routeService = new RouteService();

        testShortestRoute(routeService);
        testRouteDistance(routeService);
        testSameLocation(routeService);
        testInvalidLocation(routeService);
        testNearestTechnician(routeService);
        testNamedRoute(routeService);

        System.out.println();
        System.out.println("=== All RouteService tests completed! ===");
    }

    /*
     * Test 1:
     * Find the shortest route between two actual
     * campus locations.
     */
    private static void testShortestRoute(RouteService routeService) {

        System.out.println("--- Test 1: Find Shortest Route ---");

        int startLocation = 1;
        int endLocation = 4;

        try {

            List<Integer> route =
                    routeService.findShortestRoute(
                            startLocation,
                            endLocation
                    );

            System.out.println(
                    "Route from location "
                            + startLocation
                            + " to location "
                            + endLocation
            );

            if (route.isEmpty()) {

                System.out.println(
                        "No route found."
                );

            } else {

                System.out.print("Shortest route: ");
                printRoute(route);

                List<String> routeNames =
                        routeService.findShortestRouteNames(
                                startLocation,
                                endLocation
                        );

                System.out.print("Route names: ");
                printNamedRoute(routeNames);

                System.out.println(
                        "Number of locations in route: "
                        + route.size()
                );

                System.out.println(
                        "Shortest route test passed."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Shortest route test failed."
            );

            e.printStackTrace();
        }

        System.out.println();
    }

    /*
     * Test 2:
     * Calculate the total distance of the shortest route.
     */
    private static void testRouteDistance(
            RouteService routeService) {

        System.out.println("--- Test 2: Calculate Route Distance ---");

        int startLocation = 1;
        int endLocation = 4;

        try {

            double distance =
                    routeService.calculateRouteDistance(
                            startLocation,
                            endLocation
                    );

            System.out.println(
                    "Distance from location "
                            + startLocation
                            + " to location "
                            + endLocation
                            + ": "
                            + distance
                            + " km"
            );

            if (distance >= 0) {

                System.out.println(
                        "Route distance test passed."
                );

            } else {

                System.out.println(
                        "No route exists between the locations."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Route distance test failed."
            );

            e.printStackTrace();
        }

        System.out.println();
    }

    /*
     * Test 3:
     * Starting and ending at the same location
     * should produce a route containing only that location
     * and a distance of 0.
     */
    private static void testSameLocation(
            RouteService routeService) {

        System.out.println("--- Test 3: Same Start and End Location ---");

        int location = 1;

        try {

            List<Integer> route =
                    routeService.findShortestRoute(
                            location,
                            location
                    );

            double distance =
                    routeService.calculateRouteDistance(
                            location,
                            location
                    );

            System.out.print("Route: ");
                printRoute(route);

            System.out.println(
                    "Distance: " + distance + " km"
            );

            if (route.size() == 1
                    && route.get(0) == location
                    && distance == 0.0) {

                System.out.println(
                        "Same location test passed."
                );

            } else {

                System.out.println(
                        "Same location test failed."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Same location test failed."
            );

            e.printStackTrace();
        }

        System.out.println();
    }

    /*
     * Test 4:
     * Invalid location IDs should be rejected.
     */
    private static void testInvalidLocation(
            RouteService routeService) {

        System.out.println("--- Test 4: Invalid Location ---");

        try {

            routeService.findShortestRoute(
                    0,
                    4
            );

            System.out.println(
                    "Invalid location test failed."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid location correctly rejected."
            );

            System.out.println(
                    "Invalid location test passed."
            );
        }

        System.out.println();
    }

    /*
     * Test 5:
     * The nearest available plumber to location 2 should be
     * the technician already stationed there.
     */
    private static void testNearestTechnician(RouteService routeService) {

        System.out.println("--- Test 5: Find Nearest Technician ---");

        int requestLocation = 2;
        int plumbingCategory = 1;

        try {

            Optional<Technician> nearest =
                    routeService.findNearestTechnician(
                            requestLocation,
                            plumbingCategory
                    );

            if (nearest.isEmpty()) {
                System.out.println("No available technician found.");
                System.out.println("Nearest technician test failed.");
                System.out.println();
                return;
            }

            Technician technician = nearest.get();

            System.out.println(
                    "Nearest technician: "
                            + technician.getFullName()
                            + " (id="
                            + technician.getTechnicianId()
                            + ", location="
                            + routeService.getLocationName(
                                    technician.getLocationId())
                            + ")"
            );

            if (technician.getLocationId() == requestLocation
                    && technician.getCategoryId() == plumbingCategory) {

                System.out.println("Nearest technician test passed.");

            } else {

                System.out.println("Nearest technician test failed.");
            }

        } catch (Exception e) {

            System.out.println("Nearest technician test failed.");
            e.printStackTrace();
        }

        System.out.println();
    }

    /*
     * Test 6:
     * Named routes should resolve campus location IDs
     * to readable location names from the database.
     */
    private static void testNamedRoute(RouteService routeService) {

        System.out.println("--- Test 6: Resolve Route Location Names ---");

        int startLocation = 1;
        int endLocation = 4;

        try {

            List<String> routeNames =
                    routeService.findShortestRouteNames(
                            startLocation,
                            endLocation
                    );

            System.out.print("Named route: ");
            printNamedRoute(routeNames);

            if (routeNames.size() == 2
                    && !routeNames.get(0).startsWith("Location #")
                    && !routeNames.get(1).startsWith("Location #")) {

                System.out.println("Named route test passed.");

            } else {

                System.out.println("Named route test failed.");
            }

        } catch (Exception e) {

            System.out.println("Named route test failed.");
            e.printStackTrace();
        }

        System.out.println();
    }

    private static void printRoute(List<Integer> route) {

        System.out.print("[");

        for (int i = 0; i < route.size(); i++) {

            System.out.print(route.get(i));

            if (i < route.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println("]");
    }

    private static void printNamedRoute(List<String> route) {

        System.out.print("[");

        for (int i = 0; i < route.size(); i++) {

            System.out.print(route.get(i));

            if (i < route.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println("]");
    }
}