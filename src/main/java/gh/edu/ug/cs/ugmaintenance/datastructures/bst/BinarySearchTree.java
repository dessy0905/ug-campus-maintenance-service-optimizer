package gh.edu.ug.cs.ugmaintenance.datastructures.bst;

import java.time.LocalDateTime;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.LinkedList;

/**
 * Binary Search Tree for storing campus maintenance
 * service requests.
 *
 * The request ID is used as the BST key.
 *
 * Smaller request IDs are stored in the left subtree.
 * Larger request IDs are stored in the right subtree.
 * Duplicate request IDs are not allowed.
 */
public class BinarySearchTree {

    // =====================================================
    // FIELDS
    // =====================================================

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

    public boolean insert(
            int requestId,
            int userId,
            int locationId,
            int technicianId,
            String issueType,
            String description,
            String urgencyLevel,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        ServiceRequestNode newNode =
                new ServiceRequestNode(
                        requestId,
                        userId,
                        locationId,
                        technicianId,
                        issueType,
                        description,
                        urgencyLevel,
                        status,
                        createdAt,
                        updatedAt
                );

        // If tree is empty, new node becomes root.
        if (root == null) {
            root = newNode;
            size++;
            return true;
        }

        ServiceRequestNode current = root;

        while (true) {

            // Duplicate IDs are not allowed.
            if (requestId == current.getRequestId()) {
                return false;
            }

            // Smaller IDs go left.
            if (requestId < current.getRequestId()) {

                if (current.getLeft() == null) {
                    current.setLeft(newNode);
                    size++;
                    return true;
                }

                current = current.getLeft();

            } else {

                // Larger IDs go right.
                if (current.getRight() == null) {
                    current.setRight(newNode);
                    size++;
                    return true;
                }

                current = current.getRight();
            }
        }
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public ServiceRequestNode search(int requestId) {

        ServiceRequestNode current = root;

        while (current != null) {

            if (requestId == current.getRequestId()) {
                return current;
            }

            if (requestId < current.getRequestId()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        return null;
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public boolean update(
            int requestId,
            String issueType,
            String description,
            String urgencyLevel,
            String status,
            LocalDateTime updatedAt) {

        ServiceRequestNode request =
                search(requestId);

        // Request does not exist.
        if (request == null) {
            return false;
        }

        request.setIssueType(issueType);
        request.setDescription(description);
        request.setUrgencyLevel(urgencyLevel);
        request.setStatus(status);
        request.setUpdatedAt(updatedAt);

        return true;
    }

    // =====================================================
    // DELETE
    // =====================================================

    public boolean delete(int requestId) {

        ServiceRequestNode parent = null;
        ServiceRequestNode current = root;

        // Find the node.
        while (current != null &&
                current.getRequestId() != requestId) {

            parent = current;

            if (requestId < current.getRequestId()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        // Request was not found.
        if (current == null) {
            return false;
        }

        // -------------------------------------------------
        // CASE 1: LEAF NODE
        // -------------------------------------------------

        if (current.getLeft() == null &&
                current.getRight() == null) {

            if (parent == null) {
                // The root is the only node.
                root = null;
            } else if (parent.getLeft() == current) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        }

        // -------------------------------------------------
        // CASE 2: ONLY RIGHT CHILD
        // -------------------------------------------------

        else if (current.getLeft() == null) {

            if (parent == null) {
                root = current.getRight();
            } else if (parent.getLeft() == current) {
                parent.setLeft(current.getRight());
            } else {
                parent.setRight(current.getRight());
            }
        }

        // -------------------------------------------------
        // CASE 3: ONLY LEFT CHILD
        // -------------------------------------------------

        else if (current.getRight() == null) {

            if (parent == null) {
                root = current.getLeft();
            } else if (parent.getLeft() == current) {
                parent.setLeft(current.getLeft());
            } else {
                parent.setRight(current.getLeft());
            }
        }

        // -------------------------------------------------
        // CASE 4: TWO CHILDREN
        // -------------------------------------------------

        else {

            /*
             * Find the in-order successor.
             *
             * The in-order successor is the smallest
             * node in the right subtree.
             */
            ServiceRequestNode successorParent = current;
            ServiceRequestNode successor =
                    current.getRight();

            while (successor.getLeft() != null) {
                successorParent = successor;
                successor = successor.getLeft();
            }

            // Copy successor's data into current node.
            copyData(successor, current);

            // Remove successor from its old position.
            if (successorParent.getLeft() == successor) {
                successorParent.setLeft(
                        successor.getRight()
                );
            } else {
                successorParent.setRight(
                        successor.getRight()
                );
            }
        }

        size--;
        return true;
    }

    // =====================================================
    // COPY NODE DATA
    // =====================================================

    private void copyData(
            ServiceRequestNode source,
            ServiceRequestNode destination) {

        destination.setRequestId(
                source.getRequestId()
        );

        destination.setUserId(
                source.getUserId()
        );

        destination.setLocationId(
                source.getLocationId()
        );

        destination.setTechnicianId(
                source.getTechnicianId()
        );

        destination.setIssueType(
                source.getIssueType()
        );

        destination.setDescription(
                source.getDescription()
        );

        destination.setUrgencyLevel(
                source.getUrgencyLevel()
        );

        destination.setStatus(
                source.getStatus()
        );

        destination.setCreatedAt(
                source.getCreatedAt()
        );

        destination.setUpdatedAt(
                source.getUpdatedAt()
        );
    }

    // =====================================================
    // IN-ORDER TRAVERSAL
    // =====================================================

    public void inOrder() {
        inOrderRecursive(root);
    }

    private void inOrderRecursive(
            ServiceRequestNode node) {

        if (node == null) {
            return;
        }

        inOrderRecursive(node.getLeft());

        System.out.println(node);

        inOrderRecursive(node.getRight());
    }

    // =====================================================
    // PRE-ORDER TRAVERSAL
    // =====================================================

    public void preOrder() {
        preOrderRecursive(root);
    }

    private void preOrderRecursive(
            ServiceRequestNode node) {

        if (node == null) {
            return;
        }

        System.out.println(node);

        preOrderRecursive(node.getLeft());

        preOrderRecursive(node.getRight());
    }

    // =====================================================
    // POST-ORDER TRAVERSAL
    // =====================================================

    public void postOrder() {
        postOrderRecursive(root);
    }

    private void postOrderRecursive(
            ServiceRequestNode node) {

        if (node == null) {
            return;
        }

        postOrderRecursive(node.getLeft());

        postOrderRecursive(node.getRight());

        System.out.println(node);
    }

    // =====================================================
    // GET REQUESTS IN ORDER
    // =====================================================

    public List<ServiceRequestNode> getRequestsInOrder() {

        // IMPORTANT:
        // List is an interface, so we instantiate
        // the concrete LinkedList implementation.
        List<ServiceRequestNode> requests =
                new LinkedList<>();

        addInOrder(root, requests);

        return requests;
    }

    private void addInOrder(
            ServiceRequestNode node,
            List<ServiceRequestNode> requests) {

        if (node == null) {
            return;
        }

        addInOrder(node.getLeft(), requests);

        requests.add(node);

        addInOrder(node.getRight(), requests);
    }

    // =====================================================
    // FIND MINIMUM
    // =====================================================

    public ServiceRequestNode findMin() {

        if (root == null) {
            return null;
        }

        ServiceRequestNode current = root;

        while (current.getLeft() != null) {
            current = current.getLeft();
        }

        return current;
    }

    // =====================================================
    // FIND MAXIMUM
    // =====================================================

    public ServiceRequestNode findMax() {

        if (root == null) {
            return null;
        }

        ServiceRequestNode current = root;

        while (current.getRight() != null) {
            current = current.getRight();
        }

        return current;
    }

    // =====================================================
    // SIZE
    // =====================================================

    public int size() {
        return size;
    }

    // =====================================================
    // EMPTY CHECK
    // =====================================================

    public boolean isEmpty() {
        return root == null;
    }

    // =====================================================
    // CLEAR
    // =====================================================

    public void clear() {
        root = null;
        size = 0;
    }

    // =====================================================
    // GET ROOT
    // =====================================================

    public ServiceRequestNode getRoot() {
        return root;
    }
}
