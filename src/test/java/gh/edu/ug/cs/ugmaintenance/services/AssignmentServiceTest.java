package gh.edu.ug.cs.ugmaintenance.services;

import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

public class AssignmentServiceTest {

    public static void main(String[] args) {

        System.out.println("=== UG Maintenance Assignment Service Test ===");
        System.out.println();

        AssignmentService assignmentService = new AssignmentService();

        testFindNearestTechnician(assignmentService);
        testAutoAssignNearestTechnician(assignmentService);
        testAutoAssignNonPendingRequest(assignmentService);

        System.out.println();
        System.out.println("=== All AssignmentService tests completed! ===");
    }

    /*
     * Pending plumbing request at location 3 should resolve
     * to the closest available plumber via Dijkstra routing.
     */
    private static void testFindNearestTechnician(
            AssignmentService assignmentService) {

        System.out.println("--- Test 1: Find Nearest Technician for Request ---");

        int requestId = 1;

        try {

            Optional<Technician> nearest =
                    assignmentService.findNearestTechnician(requestId);

            if (nearest.isEmpty()) {
                System.out.println("No nearest technician found.");
                System.out.println("Find nearest technician test failed.");
                System.out.println();
                return;
            }

            Technician technician = nearest.get();

            System.out.println(
                    "Nearest technician for request "
                            + requestId
                            + ": "
                            + technician.getFullName()
                            + " (id="
                            + technician.getTechnicianId()
                            + ", location="
                            + technician.getLocationId()
                            + ", category="
                            + technician.getCategoryId()
                            + ")"
            );

            if (technician.getCategoryId() == 1) {
                System.out.println("Find nearest technician test passed.");
            } else {
                System.out.println("Find nearest technician test failed.");
            }

        } catch (Exception e) {
            System.out.println("Find nearest technician test failed.");
            e.printStackTrace();
        }

        System.out.println();
    }

    /*
     * Pending carpentry request at location 2 should be assigned
     * to the nearest available carpenter.
     */
    private static void testAutoAssignNearestTechnician(
            AssignmentService assignmentService) {

        System.out.println("--- Test 2: Auto-Assign Nearest Technician ---");

        int requestId = 5;

        try {

            Optional<ServiceRequest> before =
                    new ServiceRequestService().getRequestById(requestId);

            if (before.isEmpty()) {
                System.out.println("Request not found.");
                System.out.println("Auto-assign test failed.");
                System.out.println();
                return;
            }

            if (before.get().getStatus() != RequestStatus.PENDING) {
                System.out.println(
                        "Request "
                                + requestId
                                + " is already "
                                + before.get().getStatus()
                                + "; skipping assignment."
                );
                System.out.println("Auto-assign test skipped.");
                System.out.println();
                return;
            }

            Optional<Technician> recommended =
                    assignmentService.findNearestTechnician(requestId);

            if (recommended.isEmpty()) {
                System.out.println("No suitable technician available.");
                System.out.println("Auto-assign test failed.");
                System.out.println();
                return;
            }

            boolean assigned =
                    assignmentService.autoAssignNearestTechnician(requestId);

            Optional<ServiceRequest> after =
                    new ServiceRequestService().getRequestById(requestId);

            System.out.println(
                    "Recommended technician: "
                            + recommended.get().getFullName()
                            + " (id="
                            + recommended.get().getTechnicianId()
                            + ")"
            );
            System.out.println("Assignment succeeded: " + assigned);
            System.out.println(
                    "Request status after assignment: "
                            + after.get().getStatus()
            );

            if (assigned
                    && after.get().getStatus() == RequestStatus.ASSIGNED) {
                System.out.println("Auto-assign test passed.");
            } else {
                System.out.println("Auto-assign test failed.");
            }

        } catch (Exception e) {
            System.out.println("Auto-assign test failed.");
            e.printStackTrace();
        }

        System.out.println();
    }

    /*
     * Requests that are not pending must be rejected for auto-assignment.
     */
    private static void testAutoAssignNonPendingRequest(
            AssignmentService assignmentService) {

        System.out.println("--- Test 3: Reject Auto-Assign for Non-Pending Request ---");

        int requestId = 2;

        try {

            assignmentService.autoAssignNearestTechnician(requestId);

            System.out.println("Auto-assign non-pending test failed.");

        } catch (IllegalStateException e) {

            System.out.println(
                    "Non-pending request correctly rejected."
            );
            System.out.println("Auto-assign non-pending test passed.");

        } catch (Exception e) {

            System.out.println("Auto-assign non-pending test failed.");
            e.printStackTrace();
        }

        System.out.println();
    }
}
