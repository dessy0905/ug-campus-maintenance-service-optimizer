package gh.edu.ug.cs.ugmaintenance.datastructures.bst;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree for managing campus maintenance service requests.
 *
 * The tree is ordered using the service request ID.
 *
 * Left subtree  -> smaller request IDs
 * Right subtree -> larger request IDs
 */
public class BinarySearchTree {

    private ServiceRequestNode root;
    private int size;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    // =====================================================
    // INSERT
    // =====================================================

    /**
     * Inserts a new service request into the BST.
     *
     * @return true if insertion was successful
     * @return false if the request ID already exists
     */
    public boolean insert(
            int requestId,
            int userId,
            int locationId,
            int categoryId,
            String requestTitle,
            String description,
            String urgencyLevel,
            String status,
            LocalDateTime requestDate,
            LocalDateTime completionDate) {

        if (root == null) {

            root = new ServiceRequestNode(
                    requestId,
                    userId,
                    locationId,
                    categoryId,
                    requestTitle,
                    description,
                    urgencyLevel,
                    status,
                    requestDate,
                    completionDate
            );

            size++;
            return true;
        }

        return insertHelper(
                root,
                requestId,
                userId,
                locationId,
                categoryId,
                requestTitle,
                description,
                urgencyLevel,
                status,
                requestDate,
                completionDate
        );
    }

    private boolean insertHelper(
            ServiceRequestNode node,
            int requestId,
            int userId,
            int locationId,
            int categoryId,
            String requestTitle,
            String description,
            String urgencyLevel,
            String status,
            LocalDateTime requestDate,
            LocalDateTime completionDate) {

        // Go left if the new request ID is smaller
        if (requestId < node.getRequestId()) {

            if (node.left == null) {

                node.left = new ServiceRequestNode(
                        requestId,
                        userId,
                        locationId,
                        categoryId,
                        requestTitle,
                        description,
                        urgencyLevel,
                        status,
                        requestDate,
                        completionDate
                );

                size++;
                return true;
            }

            return insertHelper(
                    node.left,
                    requestId,
                    userId,
                    locationId,
                    categoryId,
                    requestTitle,
                    description,
                    urgencyLevel,
                    status,
                    requestDate,
                    completionDate
            );
        }

        // Go right if the new request ID is larger
        if (requestId > node.getRequestId()) {

            if (node.right == null) {

                node.right = new ServiceRequestNode(
                        requestId,
                        userId,
                        locationId,
                        categoryId,
                        requestTitle,
                        description,
                        urgencyLevel,
                        status,
                        requestDate,
                        completionDate
                );

                size++;
                return true;
            }

            return insertHelper(
                    node.right,
                    requestId,
                    userId,
                    locationId,
                    categoryId,
                    requestTitle,
                    description,
                    urgencyLevel,
                    status,
                    requestDate,
                    completionDate
            );
        }

        // Duplicate request ID
        System.out.println(
                "Duplicate request ID: " + requestId
        );

        return false;
    }

    // =====================================================
    // SEARCH
    // =====================================================

    /**
     * Searches for a service request using its ID.
     *
     * Average time complexity: O(log n)
     * Worst-case time complexity: O(n)
     */
    public ServiceRequestNode search(int requestId) {

        ServiceRequestNode current = root;

        while (current != null) {

            // Request found
            if (requestId == current.getRequestId()) {
                return current;
            }

            // Search left subtree
            if (requestId < current.getRequestId()) {
                current = current.left;
            }

            // Search right subtree
            else {
                current = current.right;
            }
        }

        // Request not found
        return null;
    }

    // =====================================================
    // UPDATE
    // =====================================================

    /**
     * Updates information about an existing service request.
     *
     * The request ID cannot be changed because it determines
     * the position of the node in the BST.
     */
    public boolean update(
            int requestId,
            String requestTitle,
            String description,
            String urgencyLevel,
            String status,
            LocalDateTime completionDate) {

        ServiceRequestNode request = search(requestId);

        if (request == null) {
            return false;
        }

        request.setRequestTitle(requestTitle);
        request.setDescription(description);
        request.setUrgencyLevel(urgencyLevel);
        request.setStatus(status);
        request.setCompletionDate(completionDate);

        return true;
    }

    // =====================================================
    // DELETE
    // =====================================================

    /**
     * Deletes a service request from the BST.
     *
     * Handles:
     *
     * 1. Leaf node
     * 2. Node with one child
     * 3. Node with two children
     */
    public boolean delete(int requestId) {

        if (search(requestId) == null) {

            System.out.println(
                    "Request #" + requestId + " not found."
            );

            return false;
        }

        root = deleteHelper(root, requestId);

        size--;

        return true;
    }

    private ServiceRequestNode deleteHelper(
            ServiceRequestNode node,
            int requestId) {

        if (node == null) {
            return null;
        }

        // Search left subtree
        if (requestId < node.getRequestId()) {

            node.left = deleteHelper(
                    node.left,
                    requestId
            );

        }

        // Search right subtree
        else if (requestId > node.getRequestId()) {

            node.right = deleteHelper(
                    node.right,
                    requestId
            );

        }

        // Node found
        else {

            // =================================================
            // CASE 1: LEAF NODE
            // =================================================

            if (node.left == null &&
                    node.right == null) {

                return null;
            }

            // =================================================
            // CASE 2: ONLY RIGHT CHILD
            // =================================================

            if (node.left == null) {
                return node.right;
            }

            // =================================================
            // CASE 2: ONLY LEFT CHILD
            // =================================================

            if (node.right == null) {
                return node.left;
            }

            // =================================================
            // CASE 3: TWO CHILDREN
            // =================================================

            /*
             * Find the smallest node in the right subtree.
             *
             * This is called the in-order successor.
             */
            ServiceRequestNode successor =
                    findMinNode(node.right);

            /*
             * Copy all information from the successor
             * into the current node.
             */
            node.copyFrom(successor);

            /*
             * Remove the duplicate successor node
             * from the right subtree.
             */
            node.right = deleteHelper(
                    node.right,
                    successor.getRequestId()
            );
        }

        return node;
    }

    // =====================================================
    // FIND MINIMUM
    // =====================================================

    /**
     * Returns the service request with the smallest ID.
     */
    public ServiceRequestNode findMin() {

        if (root == null) {
            return null;
        }

        return findMinNode(root);
    }

    private ServiceRequestNode findMinNode(
            ServiceRequestNode node) {

        ServiceRequestNode current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    // =====================================================
    // FIND MAXIMUM
    // =====================================================

    /**
     * Returns the service request with the largest ID.
     */
    public ServiceRequestNode findMax() {

        if (root == null) {
            return null;
        }

        ServiceRequestNode current = root;

        while (current.right != null) {
            current = current.right;
        }

        return current;
    }

    // =====================================================
    // IN-ORDER TRAVERSAL
    // =====================================================

    /**
     * Visits nodes in ascending request ID order.
     */
    public void inOrder() {
        inOrderHelper(root);
    }

    private void inOrderHelper(
            ServiceRequestNode node) {

        if (node == null) {
            return;
        }

        inOrderHelper(node.left);

        System.out.println(node);

        inOrderHelper(node.right);
    }

    // =====================================================
    // PRE-ORDER TRAVERSAL
    // =====================================================

    /**
     * Visits the root before its subtrees.
     */
    public void preOrder() {
        preOrderHelper(root);
    }

    private void preOrderHelper(
            ServiceRequestNode node) {

        if (node == null) {
            return;
        }

        System.out.println(node);

        preOrderHelper(node.left);

        preOrderHelper(node.right);
    }

    // =====================================================
    // POST-ORDER TRAVERSAL
    // =====================================================

    /**
     * Visits the subtrees before the root.
     */
    public void postOrder() {
        postOrderHelper(root);
    }

    private void postOrderHelper(
            ServiceRequestNode node) {

        if (node == null) {
            return;
        }

        postOrderHelper(node.left);

        postOrderHelper(node.right);

        System.out.println(node);
    }

    // =====================================================
    // GET REQUESTS IN SORTED ORDER
    // =====================================================

    /**
     * Returns all service requests sorted by request ID.
     */
    public List<ServiceRequestNode> getRequestsInOrder() {

        List<ServiceRequestNode> requests =
                new ArrayList<>();

        collectInOrder(root, requests);

        return requests;
    }

    private void collectInOrder(
            ServiceRequestNode node,
            List<ServiceRequestNode> requests) {

        if (node == null) {
            return;
        }

        collectInOrder(node.left, requests);

        requests.add(node);

        collectInOrder(node.right, requests);
    }

    // =====================================================
    // SIZE
    // =====================================================

    /**
     * Returns the number of service requests
     * currently stored in the BST.
     */
    public int size() {
        return size;
    }

    // =====================================================
    // IS EMPTY
    // =====================================================

    /**
     * Checks whether the BST contains no requests.
     */
    public boolean isEmpty() {
        return root == null;
    }

    // =====================================================
    // CLEAR
    // =====================================================

    /**
     * Removes all service requests from the BST.
     */
    public void clear() {

        root = null;
        size = 0;
    }
}