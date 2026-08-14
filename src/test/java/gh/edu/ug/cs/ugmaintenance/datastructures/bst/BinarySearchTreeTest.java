package gh.edu.ug.cs.ugmaintenance.datastructures.bst;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

public class BinarySearchTreeTest {

    // =====================================================
    // INSERTION
    // =====================================================

    @Test
    void testInsertion() {
        BinarySearchTree bst = createSampleTree();

        assertEquals(3, bst.size());
        assertFalse(bst.isEmpty());
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Test
    void testSearch() {
        BinarySearchTree bst = createSampleTree();

        ServiceRequestNode result = bst.search(180);

        assertNotNull(result);
        assertEquals(180, result.getRequestId());

        assertNull(bst.search(999));
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Test
    void testUpdate() {
        BinarySearchTree bst = createSampleTree();

        boolean updated = bst.update(
                180,
                "Electrical Fault",
                "Power socket is damaged",
                "High",
                "In Progress",
                null
        );

        assertTrue(updated);

        ServiceRequestNode request = bst.search(180);

        assertNotNull(request);
        assertEquals("In Progress", request.getStatus());
        assertEquals("High", request.getUrgencyLevel());

        boolean failedUpdate = bst.update(
                999,
                "Unknown",
                "Unknown request",
                "Low",
                "Pending",
                null
        );

        assertFalse(failedUpdate);
    }

    // =====================================================
    // TRAVERSALS
    // =====================================================

    @Test
    void testTraversals() {
        BinarySearchTree bst = createSampleTree();

        // inOrder/preOrder/postOrder print to stdout rather than
        // returning data, so this just confirms they run cleanly
        // over a populated tree without throwing.
        assertDoesNotThrow(bst::inOrder);
        assertDoesNotThrow(bst::preOrder);
        assertDoesNotThrow(bst::postOrder);

        assertEquals(3, bst.getRequestsInOrder().size());
    }

    // =====================================================
    // DELETE LEAF
    // =====================================================

    @Test
    void testDeleteLeaf() {
        BinarySearchTree bst = createSampleTree();

        boolean deleted = bst.delete(180);

        assertTrue(deleted);
        assertNull(bst.search(180));
        assertEquals(2, bst.size());
    }

    // =====================================================
    // DELETE ONE CHILD
    // =====================================================

    @Test
    void testDeleteOneChild() {
        BinarySearchTree bst = new BinarySearchTree();

        bst.insert(
                230, 10, 1, 2,
                "Broken pipe", "Pipe leaking",
                "High", "Pending",
                LocalDateTime.now(), null
        );

        bst.insert(
                180, 11, 2, 3,
                "Electrical fault", "Socket damaged",
                "Medium", "Assigned",
                LocalDateTime.now(), null
        );

        bst.insert(
                200, 12, 3, 4,
                "Heating problem", "Heater not working",
                "Low", "Pending",
                LocalDateTime.now(), null
        );

        boolean deleted = bst.delete(180);

        assertTrue(deleted);
        assertNotNull(bst.search(200));
        assertEquals(2, bst.size());
    }

    // =====================================================
    // DELETE TWO CHILDREN
    // =====================================================

    @Test
    void testDeleteTwoChildren() {
        BinarySearchTree bst = createSampleTree();

        boolean deleted = bst.delete(230);

        assertTrue(deleted);
        assertNull(bst.search(230));
        assertNotNull(bst.search(180));
        assertNotNull(bst.search(350));
        assertEquals(2, bst.size());
    }

    // =====================================================
    // DUPLICATES
    // =====================================================

    @Test
    void testDuplicateInsertion() {
        BinarySearchTree bst = new BinarySearchTree();

        boolean firstInsert = bst.insert(
                230, 10, 1, 2,
                "Plumbing problem", "Broken pipe",
                "High", "Pending",
                LocalDateTime.now(), null
        );

        boolean duplicateInsert = bst.insert(
                230, 11, 2, 3,
                "Electrical problem", "Broken socket",
                "Low", "Pending",
                LocalDateTime.now(), null
        );

        assertTrue(firstInsert);
        assertFalse(duplicateInsert);
        assertEquals(1, bst.size());
    }

    // =====================================================
    // MINIMUM / MAXIMUM
    // =====================================================

    @Test
    void testMinimumMaximum() {
        BinarySearchTree bst = new BinarySearchTree();

        bst.insert(230, 10, 1, 2, "Plumbing", "Broken pipe",
                "High", "Pending", LocalDateTime.now(), null);
        bst.insert(180, 11, 2, 3, "Electrical", "Socket fault",
                "Medium", "Assigned", LocalDateTime.now(), null);
        bst.insert(350, 12, 3, 4, "AC Repair", "AC not cooling",
                "Low", "Pending", LocalDateTime.now(), null);
        bst.insert(120, 13, 4, 5, "Carpentry", "Broken desk",
                "High", "Assigned", LocalDateTime.now(), null);
        bst.insert(400, 14, 5, 6, "Generator", "Generator fault",
                "Critical", "Pending", LocalDateTime.now(), null);

        ServiceRequestNode min = bst.findMin();
        ServiceRequestNode max = bst.findMax();

        assertNotNull(min);
        assertEquals(120, min.getRequestId());

        assertNotNull(max);
        assertEquals(400, max.getRequestId());
    }

    // =====================================================
    // SIZE
    // =====================================================

    @Test
    void testSize() {
        BinarySearchTree bst = new BinarySearchTree();

        assertEquals(0, bst.size());

        bst.insert(230, 10, 1, 2, "Plumbing", "Broken pipe",
                "High", "Pending", LocalDateTime.now(), null);
        bst.insert(180, 11, 2, 3, "Electrical", "Socket fault",
                "Medium", "Assigned", LocalDateTime.now(), null);

        assertEquals(2, bst.size());
    }

    // =====================================================
    // EMPTY TREE
    // =====================================================

    @Test
    void testEmptyTree() {
        BinarySearchTree bst = new BinarySearchTree();

        assertTrue(bst.isEmpty());
        assertNull(bst.search(100));
        assertNull(bst.findMin());
        assertNull(bst.findMax());
    }

    // =====================================================
    // DELETE NONEXISTENT
    // =====================================================

    @Test
    void testDeleteNonexistent() {
        BinarySearchTree bst = createSampleTree();

        boolean deleted = bst.delete(999);

        assertFalse(deleted);
        assertEquals(3, bst.size());
    }

    // =====================================================
    // SORTED REQUESTS
    // =====================================================

    @Test
    void testSortedRequests() {
        BinarySearchTree bst = new BinarySearchTree();

        bst.insert(230, 10, 1, 2, "Plumbing", "Broken pipe",
                "High", "Pending", LocalDateTime.now(), null);
        bst.insert(180, 11, 2, 3, "Electrical", "Socket fault",
                "Medium", "Assigned", LocalDateTime.now(), null);
        bst.insert(350, 12, 3, 4, "AC Repair", "AC issue",
                "Low", "Pending", LocalDateTime.now(), null);
        bst.insert(120, 13, 4, 5, "Carpentry", "Broken desk",
                "High", "Assigned", LocalDateTime.now(), null);

        List<ServiceRequestNode> requests = bst.getRequestsInOrder();

        assertEquals(120, requests.get(0).getRequestId());
        assertEquals(180, requests.get(1).getRequestId());
        assertEquals(230, requests.get(2).getRequestId());
        assertEquals(350, requests.get(3).getRequestId());
    }

    // =====================================================
    // CLEAR
    // =====================================================

    @Test
    void testClear() {
        BinarySearchTree bst = createSampleTree();

        bst.clear();

        assertEquals(0, bst.size());
        assertTrue(bst.isEmpty());
    }

    // =====================================================
    // CREATE SAMPLE TREE
    // =====================================================

    private static BinarySearchTree createSampleTree() {
        BinarySearchTree bst = new BinarySearchTree();

        bst.insert(
                230, 10, 1, 2,
                "Plumbing Problem", "Broken water pipe",
                "High", "Pending",
                LocalDateTime.now(), null
        );

        bst.insert(
                180, 11, 2, 3,
                "Electrical Fault", "Damaged electrical socket",
                "Medium", "Assigned",
                LocalDateTime.now(), null
        );

        bst.insert(
                350, 12, 3, 4,
                "AC Repair", "Air conditioner not cooling",
                "Low", "Pending",
                LocalDateTime.now(), null
        );

        return bst;
    }
}