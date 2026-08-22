package gh.edu.ug.cs.ugmaintenance.datastructures.tree;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;

/**
 * Custom Generic Binary Search Tree (BST) implementation.
 *
 * <p>Enforces the BST invariant: for any node N, all values in N's left subtree
 * are strictly smaller than N.data, and all values in N's right subtree are strictly
 * greater than N.data.</p>
 *
 * <p>Supports insertion, search, contains, deletion (covering leaf, single-child,
 * and two-child cases with inorder successor), min, max, height, and traversals
 * (inorder, preorder, postorder).</p>
 *
 * <p>All traversal results are returned using the project's custom {@link DynamicArray}.
 * No Java Collections library is used.</p>
 *
 * @param <T> the type of elements maintained by this tree, must implement {@link Comparable}
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private TreeNode<T> root;
    private int size;

    /**
     * Constructs an empty Binary Search Tree.
     */
    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Inserts a value into the BST maintaining the binary search tree property.
     * Duplicate values are ignored (or not re-inserted) to maintain unique keys.
     *
     * @param value the value to insert
     * @throws IllegalArgumentException if value is null
     */
    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert null into BinarySearchTree");
        }

        int previousSize = size;
        root = insertRecursive(root, value);
        if (size > previousSize) {
            // size was incremented in recursive step
        }
    }

    private TreeNode<T> insertRecursive(TreeNode<T> current, T value) {
        if (current == null) {
            size++;
            return new TreeNode<>(value);
        }

        int cmp = value.compareTo(current.data);
        if (cmp < 0) {
            current.left = insertRecursive(current.left, value);
        } else if (cmp > 0) {
            current.right = insertRecursive(current.right, value);
        } else {
            // Duplicate value encountered: do not insert duplicate
        }

        return current;
    }

    /**
     * Searches for a value in the BST.
     *
     * @param value the value to search for
     * @return the matching value if found, or {@code null} if not found
     * @throws IllegalArgumentException if value is null
     */
    public T search(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot search for null in BinarySearchTree");
        }

        TreeNode<T> node = searchNode(root, value);
        return node != null ? node.data : null;
    }

    private TreeNode<T> searchNode(TreeNode<T> current, T value) {
        while (current != null) {
            int cmp = value.compareTo(current.data);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    /**
     * Checks whether a value exists in the BST.
     *
     * @param value the value to check
     * @return {@code true} if present, {@code false} otherwise
     */
    public boolean contains(T value) {
        if (value == null) {
            return false;
        }
        return search(value) != null;
    }

    /**
     * Deletes a value from the BST.
     *
     * <p>Handles all 3 deletion cases:
     * <ul>
     *   <li><b>Case 1 (Leaf node):</b> Remove node directly.</li>
     *   <li><b>Case 2 (One child):</b> Replace node with its child.</li>
     *   <li><b>Case 3 (Two children):</b> Find inorder successor (min of right subtree),
     *       replace current data with successor data, and delete successor from right subtree.</li>
     * </ul>
     *
     * @param value the value to delete
     * @return {@code true} if value was found and removed, {@code false} otherwise
     * @throws IllegalArgumentException if value is null
     */
    public boolean delete(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot delete null from BinarySearchTree");
        }

        if (!contains(value)) {
            return false;
        }

        root = deleteRecursive(root, value);
        size--;
        return true;
    }

    private TreeNode<T> deleteRecursive(TreeNode<T> current, T value) {
        if (current == null) {
            return null;
        }

        int cmp = value.compareTo(current.data);

        if (cmp < 0) {
            current.left = deleteRecursive(current.left, value);
        } else if (cmp > 0) {
            current.right = deleteRecursive(current.right, value);
        } else {
            // Node found! Handle deletion cases:

            // Case 1 & 2: No child or only one child
            if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            }

            // Case 3: Node with two children
            // Find the inorder successor (smallest in the right subtree)
            TreeNode<T> successor = findMinNode(current.right);

            // Copy successor's data to this node
            current.data = successor.data;

            // Delete the successor from the right subtree
            current.right = deleteRecursive(current.right, successor.data);
        }

        return current;
    }

    /**
     * Returns the minimum value in the BST.
     *
     * @return minimum value
     * @throws IllegalStateException if the tree is empty
     */
    public T min() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot find minimum in empty BinarySearchTree");
        }
        return findMinNode(root).data;
    }

    private TreeNode<T> findMinNode(TreeNode<T> node) {
        TreeNode<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Returns the maximum value in the BST.
     *
     * @return maximum value
     * @throws IllegalStateException if the tree is empty
     */
    public T max() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot find maximum in empty BinarySearchTree");
        }
        return findMaxNode(root).data;
    }

    private TreeNode<T> findMaxNode(TreeNode<T> node) {
        TreeNode<T> current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    /**
     * Computes the height of the BST.
     * Empty tree has height -1; a tree with a single root has height 0.
     *
     * @return tree height
     */
    public int height() {
        return computeHeight(root);
    }

    private int computeHeight(TreeNode<T> node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = computeHeight(node.left);
        int rightHeight = computeHeight(node.right);
        return 1 + (leftHeight > rightHeight ? leftHeight : rightHeight);
    }

    /**
     * Returns the number of elements in the tree.
     *
     * @return tree size
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the BST is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the root node (useful for testing and tree inspection).
     */
    public TreeNode<T> getRoot() {
        return root;
    }

    // =========================================================
    // TRAVERSALS
    // =========================================================

    /**
     * Inorder Traversal: Left Subtree -> Root -> Right Subtree.
     * Yields elements in ascending sorted order.
     *
     * @return {@link DynamicArray} of elements in inorder sequence
     */
    public DynamicArray<T> inorder() {
        DynamicArray<T> result = new DynamicArray<>(size > 0 ? size : 10);
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(TreeNode<T> node, DynamicArray<T> list) {
        if (node != null) {
            inorderRecursive(node.left, list);
            list.add(node.data);
            inorderRecursive(node.right, list);
        }
    }

    /**
     * Preorder Traversal: Root -> Left Subtree -> Right Subtree.
     *
     * @return {@link DynamicArray} of elements in preorder sequence
     */
    public DynamicArray<T> preorder() {
        DynamicArray<T> result = new DynamicArray<>(size > 0 ? size : 10);
        preorderRecursive(root, result);
        return result;
    }

    private void preorderRecursive(TreeNode<T> node, DynamicArray<T> list) {
        if (node != null) {
            list.add(node.data);
            preorderRecursive(node.left, list);
            preorderRecursive(node.right, list);
        }
    }

    /**
     * Postorder Traversal: Left Subtree -> Right Subtree -> Root.
     *
     * @return {@link DynamicArray} of elements in postorder sequence
     */
    public DynamicArray<T> postorder() {
        DynamicArray<T> result = new DynamicArray<>(size > 0 ? size : 10);
        postorderRecursive(root, result);
        return result;
    }

    private void postorderRecursive(TreeNode<T> node, DynamicArray<T> list) {
        if (node != null) {
            postorderRecursive(node.left, list);
            postorderRecursive(node.right, list);
            list.add(node.data);
        }
    }
}
