package gh.edu.ug.cs.ugmaintenance.datastructures.redblacktree;

/**
 * Demonstrates how the Red-Black Tree can be used
 * in the UG Campus Maintenance Service Optimizer.
 */
public class RedBlackTreeDemo {

    public static void main(String[] args) {

        RedBlackTree<Integer, String> maintenanceRequests =
                new RedBlackTree<>();


        // =====================================================
        // REGISTER MAINTENANCE REQUESTS
        // =====================================================

        maintenanceRequests.put(
                1042,
                "Broken pipe - Legon Hall"
        );

        maintenanceRequests.put(
                1015,
                "Electrical fault - Balme Library"
        );

        maintenanceRequests.put(
                1078,
                "Air conditioning fault - N-Block"
        );

        maintenanceRequests.put(
                1024,
                "Water leakage - Akuafo Hall"
        );

        maintenanceRequests.put(
                1060,
                "Electrical fault - Chemistry Lab"
        );

        maintenanceRequests.put(
                1100,
                "Broken window - Commonwealth Hall"
        );

        maintenanceRequests.put(
                1001,
                "Faulty light - Computer Science Department"
        );


        // =====================================================
        // DISPLAY TREE
        // =====================================================

        System.out.println(
                "=============================================="
        );

        System.out.println(
                " UG CAMPUS MAINTENANCE SERVICE OPTIMIZER"
        );

        System.out.println(
                " RED-BLACK TREE DEMONSTRATION"
        );

        System.out.println(
                "=============================================="
        );


        System.out.println(
                "\nRed-Black Tree Structure:"
        );

        maintenanceRequests.printTree();


        // =====================================================
        // TRAVERSALS
        // =====================================================

        System.out.println(
                "\nRequests sorted by ID:"
        );

        maintenanceRequests.inOrderTraversal();


        System.out.println(
                "\nPre-order traversal:"
        );

        maintenanceRequests.preOrderTraversal();


        System.out.println(
                "\nPost-order traversal:"
        );

        maintenanceRequests.postOrderTraversal();


        // =====================================================
        // SEARCH
        // =====================================================

        System.out.println(
                "\n----------------------------------------------"
        );

        System.out.println(
                "SEARCHING FOR MAINTENANCE REQUEST"
        );

        System.out.println(
                "----------------------------------------------"
        );

        int searchId = 1042;

        System.out.println(
                "Searching for Request ID: "
                        + searchId
        );

        String request =
                maintenanceRequests.get(searchId);

        if (request != null) {

            System.out.println(
                    "Request found: "
                            + request
            );

        } else {

            System.out.println(
                    "Request not found."
            );
        }


        // =====================================================
        // CONTAINS KEY
        // =====================================================

        System.out.println(
                "\nContains Request 1060: "
                        + maintenanceRequests
                        .containsKey(1060)
        );

        System.out.println(
                "Contains Request 5000: "
                        + maintenanceRequests
                        .containsKey(5000)
        );


        // =====================================================
        // MINIMUM / MAXIMUM
        // =====================================================

        System.out.println(
                "\nSmallest Request ID: "
                        + maintenanceRequests.minKey()
        );

        System.out.println(
                "Largest Request ID: "
                        + maintenanceRequests.maxKey()
        );


        // =====================================================
        // TREE INFORMATION
        // =====================================================

        System.out.println(
                "\nNumber of Requests: "
                        + maintenanceRequests.size()
        );

        System.out.println(
                "Tree Height: "
                        + maintenanceRequests.height()
        );


        // =====================================================
        // UPDATE EXISTING REQUEST
        // =====================================================

        System.out.println(
                "\n----------------------------------------------"
        );

        System.out.println(
                "UPDATING REQUEST 1042"
        );

        System.out.println(
                "----------------------------------------------"
        );

        maintenanceRequests.put(
                1042,
                "Broken pipe repaired - Legon Hall"
        );

        System.out.println(
                maintenanceRequests.get(1042)
        );

        System.out.println(
                "Size after update: "
                        + maintenanceRequests.size()
        );


        // =====================================================
        // DELETE REQUEST
        // =====================================================

        System.out.println(
                "\n----------------------------------------------"
        );

        System.out.println(
                "DELETING REQUEST 1024"
        );

        System.out.println(
                "----------------------------------------------"
        );

        String removedRequest =
                maintenanceRequests.remove(1024);

        System.out.println(
                "Removed: "
                        + removedRequest
        );


        System.out.println(
                "\nTree after deleting 1024:"
        );

        maintenanceRequests.printTree();


        System.out.println(
                "\nRemaining requests:"
        );

        maintenanceRequests.inOrderTraversal();


        System.out.println(
                "Number of Requests: "
                        + maintenanceRequests.size()
        );


        // =====================================================
        // DELETE ANOTHER REQUEST
        // =====================================================

        System.out.println(
                "\n----------------------------------------------"
        );

        System.out.println(
                "DELETING REQUEST 1015"
        );

        System.out.println(
                "----------------------------------------------"
        );

        removedRequest =
                maintenanceRequests.remove(1015);

        System.out.println(
                "Removed: "
                        + removedRequest
        );


        System.out.println(
                "\nTree after deletion:"
        );

        maintenanceRequests.printTree();


        System.out.println(
                "\nRemaining requests:"
        );

        maintenanceRequests.inOrderTraversal();


        // =====================================================
        // FINAL TREE INFORMATION
        // =====================================================

        System.out.println(
                "\nFinal Tree Height: "
                        + maintenanceRequests.height()
        );

        System.out.println(
                "Final Number of Requests: "
                        + maintenanceRequests.size()
        );


        System.out.println(
                "\n=============================================="
        );

        System.out.println(
                " RED-BLACK TREE DEMO COMPLETE"
        );

        System.out.println(
                "=============================================="
        );
    }
}