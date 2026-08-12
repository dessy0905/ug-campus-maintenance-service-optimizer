package gh.edu.ug.cs.ugmaintenance.datastructures.bst;

import java.time.LocalDateTime;
import java.util.List;

public class BinarySearchTreeTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println(" UG CAMPUS MAINTENANCE SERVICE OPTIMIZER");
        System.out.println("       BINARY SEARCH TREE TEST SUITE");
        System.out.println("==============================================");

        testInsertion();
        testSearch();
        testUpdate();
        testTraversals();
        testDeleteLeaf();
        testDeleteOneChild();
        testDeleteTwoChildren();
        testDuplicateInsertion();
        testMinimumMaximum();
        testSize();
        testEmptyTree();
        testDeleteNonexistent();
        testSortedRequests();
        testClear();

        System.out.println("\n==============================================");
        System.out.println("          ALL BST TESTS COMPLETED");
        System.out.println("==============================================");
    }

    // =====================================================
    // INSERTION
    // =====================================================

    private static void testInsertion() {

        System.out.println("\n--- INSERTION TEST ---");

        BinarySearchTree bst = createSampleTree();

        check(
                "Three service requests inserted",
                bst.size() == 3
        );

        check(
                "BST is not empty",
                !bst.isEmpty()
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    private static void testSearch() {

        System.out.println("\n--- SEARCH TEST ---");

        BinarySearchTree bst = createSampleTree();

        ServiceRequestNode result =
                bst.search(180);

        check(
                "Existing request #180 found",
                result != null
        );

        check(
                "Correct request ID returned",
                result != null
                        && result.getRequestId() == 180
        );

        result = bst.search(999);

        check(
                "Nonexistent request #999 returns null",
                result == null
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    private static void testUpdate() {

        System.out.println("\n--- UPDATE TEST ---");

        BinarySearchTree bst = createSampleTree();

        boolean updated = bst.update(
                180,
                "Electrical Fault",
                "Power socket is damaged",
                "High",
                "In Progress",
                null
        );

        check(
                "Existing request updated",
                updated
        );

        ServiceRequestNode request =
                bst.search(180);

        check(
                "Request status updated",
                request != null
                        && request.getStatus()
                        .equals("In Progress")
        );

        check(
                "Request urgency updated",
                request != null
                        && request.getUrgencyLevel()
                        .equals("High")
        );

        boolean failedUpdate = bst.update(
                999,
                "Unknown",
                "Unknown request",
                "Low",
                "Pending",
                null
        );

        check(
                "Updating nonexistent request fails",
                !failedUpdate
        );
    }

    // =====================================================
    // TRAVERSALS
    // =====================================================

    private static void testTraversals() {

        System.out.println("\n--- TRAVERSAL TEST ---");

        BinarySearchTree bst = createSampleTree();

        System.out.println("\nIn-order:");
        bst.inOrder();

        System.out.println("\nPre-order:");
        bst.preOrder();

        System.out.println("\nPost-order:");
        bst.postOrder();

        check(
                "Traversal returns all requests",
                bst.getRequestsInOrder().size() == 3
        );
    }

    // =====================================================
    // DELETE LEAF
    // =====================================================

    private static void testDeleteLeaf() {

        System.out.println("\n--- DELETE LEAF TEST ---");

        BinarySearchTree bst = createSampleTree();

        boolean deleted = bst.delete(180);

        check(
                "Leaf request #180 deleted",
                deleted
        );

        check(
                "Deleted request cannot be found",
                bst.search(180) == null
        );

        check(
                "Tree size reduced to 2",
                bst.size() == 2
        );
    }

    // =====================================================
    // DELETE ONE CHILD
    // =====================================================

    private static void testDeleteOneChild() {

        System.out.println("\n--- DELETE ONE-CHILD TEST ---");

        BinarySearchTree bst = new BinarySearchTree();

        bst.insert(
                230,
                10,
                1,
                2,
                "Broken pipe",
                "Pipe leaking",
                "High",
                "Pending",
                LocalDateTime.now(),
                null
        );

        bst.insert(
                180,
                11,
                2,
                3,
                "Electrical fault",
                "Socket damaged",
                "Medium",
                "Assigned",
                LocalDateTime.now(),
                null
        );

        bst.insert(
                200,
                12,
                3,
                4,
                "Heating problem",
                "Heater not working",
                "Low",
                "Pending",
                LocalDateTime.now(),
                null
        );

        boolean deleted = bst.delete(180);

        check(
                "One-child request #180 deleted",
                deleted
        );

        check(
                "Child request #200 remains",
                bst.search(200) != null
        );

        check(
                "Tree contains 2 requests",
                bst.size() == 2
        );
    }

    // =====================================================
    // DELETE TWO CHILDREN
    // =====================================================

    private static void testDeleteTwoChildren() {

        System.out.println("\n--- DELETE TWO-CHILD TEST ---");

        BinarySearchTree bst = createSampleTree();

        boolean deleted = bst.delete(230);

        check(
                "Root request #230 deleted",
                deleted
        );

        check(
                "Request #230 no longer exists",
                bst.search(230) == null
        );

        check(
                "Request #180 remains",
                bst.search(180) != null
        );

        check(
                "Request #350 remains",
                bst.search(350) != null
        );

        check(
                "Tree contains 2 requests",
                bst.size() == 2
        );
    }

    // =====================================================
    // DUPLICATES
    // =====================================================

    private static void testDuplicateInsertion() {

        System.out.println("\n--- DUPLICATE INSERTION TEST ---");

        BinarySearchTree bst =
                new BinarySearchTree();

        boolean firstInsert = bst.insert(
                230,
                10,
                1,
                2,
                "Plumbing problem",
                "Broken pipe",
                "High",
                "Pending",
                LocalDateTime.now(),
                null
        );

        boolean duplicateInsert = bst.insert(
                230,
                11,
                2,
                3,
                "Electrical problem",
                "Broken socket",
                "Low",
                "Pending",
                LocalDateTime.now(),
                null
        );

        check(
                "First insertion succeeds",
                firstInsert
        );

        check(
                "Duplicate request ID rejected",
                !duplicateInsert
        );

        check(
                "Duplicate does not increase size",
                bst.size() == 1
        );
    }

    // =====================================================
    // MINIMUM / MAXIMUM
    // =====================================================

    private static void testMinimumMaximum() {

        System.out.println("\n--- MINIMUM / MAXIMUM TEST ---");

        BinarySearchTree bst =
                new BinarySearchTree();

        bst.insert(
                230, 10, 1, 2,
                "Plumbing", "Broken pipe",
                "High", "Pending",
                LocalDateTime.now(), null
        );

        bst.insert(
                180, 11, 2, 3,
                "Electrical", "Socket fault",
                "Medium", "Assigned",
                LocalDateTime.now(), null
        );

        bst.insert(
                350, 12, 3, 4,
                "AC Repair", "AC not cooling",
                "Low", "Pending",
                LocalDateTime.now(), null
        );

        bst.insert(
                120, 13, 4, 5,
                "Carpentry", "Broken desk",
                "High", "Assigned",
                LocalDateTime.now(), null
        );

        bst.insert(
                400, 14, 5, 6,
                "Generator", "Generator fault",
                "Critical", "Pending",
                LocalDateTime.now(), null
        );

        ServiceRequestNode min =
                bst.findMin();

        ServiceRequestNode max =
                bst.findMax();

        check(
                "Minimum request is #120",
                min != null
                        && min.getRequestId() == 120
        );

        check(
                "Maximum request is #400",
                max != null
                        && max.getRequestId() == 400
        );
    }

    // =====================================================
    // SIZE
    // =====================================================

    private static void testSize() {

        System.out.println("\n--- SIZE TEST ---");

        BinarySearchTree bst =
                new BinarySearchTree();

        check(
                "New BST has size 0",
                bst.size() == 0
        );

        bst.insert(
                230, 10, 1, 2,
                "Plumbing", "Broken pipe",
                "High", "Pending",
                LocalDateTime.now(), null
        );

        bst.insert(
                180, 11, 2, 3,
                "Electrical", "Socket fault",
                "Medium", "Assigned",
                LocalDateTime.now(), null
        );

        check(
                "BST has size 2",
                bst.size() == 2
        );
    }

    // =====================================================
    // EMPTY TREE
    // =====================================================

    private static void testEmptyTree() {

        System.out.println("\n--- EMPTY TREE TEST ---");

        BinarySearchTree bst =
                new BinarySearchTree();

        check(
                "New BST is empty",
                bst.isEmpty()
        );

        check(
                "Search empty BST returns null",
                bst.search(100) == null
        );

        check(
                "Minimum of empty BST is null",
                bst.findMin() == null
        );

        check(
                "Maximum of empty BST is null",
                bst.findMax() == null
        );
    }

    // =====================================================
    // DELETE NONEXISTENT
    // =====================================================

    private static void testDeleteNonexistent() {

        System.out.println("\n--- DELETE NONEXISTENT TEST ---");

        BinarySearchTree bst =
                createSampleTree();

        boolean deleted =
                bst.delete(999);

        check(
                "Deleting nonexistent request fails",
                !deleted
        );

        check(
                "Tree remains unchanged",
                bst.size() == 3
        );
    }

    // =====================================================
    // SORTED REQUESTS
    // =====================================================

    private static void testSortedRequests() {

        System.out.println("\n--- SORTED REQUEST TEST ---");

        BinarySearchTree bst =
                new BinarySearchTree();

        bst.insert(
                230, 10, 1, 2,
                "Plumbing", "Broken pipe",
                "High", "Pending",
                LocalDateTime.now(), null
        );

        bst.insert(
                180, 11, 2, 3,
                "Electrical", "Socket fault",
                "Medium", "Assigned",
                LocalDateTime.now(), null
        );

        bst.insert(
                350, 12, 3, 4,
                "AC Repair", "AC issue",
                "Low", "Pending",
                LocalDateTime.now(), null
        );

        bst.insert(
                120, 13, 4, 5,
                "Carpentry", "Broken desk",
                "High", "Assigned",
                LocalDateTime.now(), null
        );

        List<ServiceRequestNode> requests =
                bst.getRequestsInOrder();

        check(
                "Requests returned in ascending ID order",
                requests.get(0).getRequestId() == 120
                        && requests.get(1).getRequestId() == 180
                        && requests.get(2).getRequestId() == 230
                        && requests.get(3).getRequestId() == 350
        );
    }

    // =====================================================
    // CLEAR
    // =====================================================

    private static void testClear() {

        System.out.println("\n--- CLEAR TEST ---");

        BinarySearchTree bst =
                createSampleTree();

        bst.clear();

        check(
                "Clear removes all requests",
                bst.size() == 0
        );

        check(
                "BST is empty after clear",
                bst.isEmpty()
        );
    }

    // =====================================================
    // CREATE SAMPLE TREE
    // =====================================================

    private static BinarySearchTree createSampleTree() {

        BinarySearchTree bst =
                new BinarySearchTree();

        bst.insert(
                230,
                10,
                1,
                2,
                "Plumbing Problem",
                "Broken water pipe",
                "High",
                "Pending",
                LocalDateTime.now(),
                null
        );

        bst.insert(
                180,
                11,
                2,
                3,
                "Electrical Fault",
                "Damaged electrical socket",
                "Medium",
                "Assigned",
                LocalDateTime.now(),
                null
        );

        bst.insert(
                350,
                12,
                3,
                4,
                "AC Repair",
                "Air conditioner not cooling",
                "Low",
                "Pending",
                LocalDateTime.now(),
                null
        );

        return bst;
    }

    // =====================================================
    // PASS / FAIL
    // =====================================================

    private static void check(
            String testName,
            boolean condition) {

        if (condition) {
            System.out.println(
                    "PASS: " + testName
            );
        } else {
            System.out.println(
                    "FAIL: " + testName
            );
        }
    }
}