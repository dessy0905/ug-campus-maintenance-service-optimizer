package gh.edu.ug.cs.ugmaintenance.datastructures.tree;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;

/**
 * Custom Generic B-Tree Implementation from Scratch.
 *
 * <p>A self-balancing search tree data structure that maintains sorted data
 * and allows searches, sequential access, insertions, and deletions in
 * logarithmic time. Suitable for indexing large datasets (e.g. campus maintenance
 * logs and location indices).</p>
 *
 * <p>Characteristics:
 * <ul>
 *   <li>Configurable minimum degree {@code t >= 2}.</li>
 *   <li>Every node except the root contains at least {@code t - 1} keys and at most {@code 2t - 1} keys.</li>
 *   <li>Every internal node except the root has at least {@code t} children and at most {@code 2t} children.</li>
 *   <li>All leaf nodes reside at the exact same depth (perfect balance).</li>
 * </ul>
 *
 * <p>Uses only custom data structures: {@link DynamicArray} and {@link BTreeNode}.
 * No Java Collections are used.</p>
 *
 * @param <T> key element type, must implement {@link Comparable}
 */
public class BTree<T extends Comparable<T>> {

    private static final int DEFAULT_MIN_DEGREE = 2; // 2-3-4 tree by default

    private final int t;
    private BTreeNode<T> root;
    private int size;

    /**
     * Constructs a B-Tree with the default minimum degree (t = 2).
     */
    public BTree() {
        this(DEFAULT_MIN_DEGREE);
    }

    /**
     * Constructs a B-Tree with a configurable minimum degree {@code t}.
     *
     * @param t minimum degree (must be >= 2)
     * @throws IllegalArgumentException if t < 2
     */
    public BTree(int t) {
        if (t < 2) {
            throw new IllegalArgumentException("B-Tree minimum degree t must be at least 2");
        }
        this.t = t;
        this.root = null;
        this.size = 0;
    }

    /**
     * Inserts a key into the B-Tree.
     * Duplicate keys are ignored to maintain set semantics.
     *
     * @param key the key to insert
     * @throws IllegalArgumentException if key is null
     */
    public void insert(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot insert null into BTree");
        }

        // Ignore duplicate keys
        if (contains(key)) {
            return;
        }

        // Case 1: Empty tree
        if (root == null) {
            root = new BTreeNode<>(t, true);
            root.keys.add(key);
            size++;
            return;
        }

        // Case 2: Root is full -> Root Splitting (height increases by 1)
        if (root.isFull()) {
            BTreeNode<T> newRoot = new BTreeNode<>(t, false);
            newRoot.children.add(root);

            // Split the old root and move median key to newRoot
            splitChild(newRoot, 0, root);

            // Determine which of the two children will receive the new key
            int i = 0;
            if (key.compareTo(newRoot.keys.get(0)) > 0) {
                i++;
            }
            insertNonFull(newRoot.children.get(i), key);

            root = newRoot;
        } else {
            // Case 3: Root is not full
            insertNonFull(root, key);
        }

        size++;
    }

    /**
     * Inserts a key into a node that is guaranteed not to be full.
     */
    private void insertNonFull(BTreeNode<T> node, T key) {
        int i = node.keys.size() - 1;

        if (node.isLeaf) {
            // Insert key into sorted position in leaf node
            while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                i--;
            }
            node.keys.add(i + 1, key);
        } else {
            // Find child that should receive the key
            while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                i--;
            }
            i++;

            // If the targeted child is full, split it first (proactive splitting)
            if (node.children.get(i).isFull()) {
                splitChild(node, i, node.children.get(i));

                // After split, the middle key went up into node.keys[i]
                if (key.compareTo(node.keys.get(i)) > 0) {
                    i++;
                }
            }

            insertNonFull(node.children.get(i), key);
        }
    }

    /**
     * Splits a full child {@code fullChild} of {@code parent} at index {@code childIndex}.
     *
     * <p>Algorithm:
     * <ol>
     *   <li>Create new node {@code z} with the same leaf status as {@code fullChild}.</li>
     *   <li>Copy the highest {@code t - 1} keys from {@code fullChild} to {@code z}.</li>
     *   <li>If not a leaf, copy the highest {@code t} children from {@code fullChild} to {@code z}.</li>
     *   <li>Extract median key at index {@code t - 1} from {@code fullChild}.</li>
     *   <li>Truncate {@code fullChild} to retain the lowest {@code t - 1} keys and {@code t} children.</li>
     *   <li>Insert {@code z} as a child of {@code parent} at {@code childIndex + 1}.</li>
     *   <li>Insert median key into {@code parent} at {@code childIndex}.</li>
     * </ol>
     */
    private void splitChild(BTreeNode<T> parent, int childIndex, BTreeNode<T> fullChild) {
        // Step 1: Create new sibling node z
        BTreeNode<T> z = new BTreeNode<>(t, fullChild.isLeaf);

        // Step 2: Copy the last (t - 1) keys from fullChild to z
        for (int j = 0; j < t - 1; j++) {
            z.keys.add(fullChild.keys.get(j + t));
        }

        // Step 3: If not a leaf, copy the last t children from fullChild to z
        if (!fullChild.isLeaf) {
            for (int j = 0; j < t; j++) {
                z.children.add(fullChild.children.get(j + t));
            }
        }

        // Step 4: Extract the median key (at index t - 1)
        T medianKey = fullChild.keys.get(t - 1);

        // Step 5: Truncate fullChild keys (remove from 2t - 2 down to t - 1)
        int keysToRemove = fullChild.keys.size() - (t - 1);
        for (int k = 0; k < keysToRemove; k++) {
            fullChild.keys.remove(fullChild.keys.size() - 1);
        }

        // Truncate fullChild children if not leaf (remove from 2t - 1 down to t)
        if (!fullChild.isLeaf) {
            int childrenToRemove = fullChild.children.size() - t;
            for (int k = 0; k < childrenToRemove; k++) {
                fullChild.children.remove(fullChild.children.size() - 1);
            }
        }

        // Step 6: Insert sibling z into parent's children list at childIndex + 1
        parent.children.add(childIndex + 1, z);

        // Step 7: Insert median key into parent's keys list at childIndex
        parent.keys.add(childIndex, medianKey);
    }

    /**
     * Searches for a key in the B-Tree.
     *
     * @param key the key to search for
     * @return the found key or {@code null} if absent
     * @throws IllegalArgumentException if key is null
     */
    public T search(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot search for null key in BTree");
        }
        return root == null ? null : root.search(key);
    }

    /**
     * Checks whether a key exists in the B-Tree.
     *
     * @param key the key to check
     * @return {@code true} if present, {@code false} otherwise
     */
    public boolean contains(T key) {
        if (key == null) {
            return false;
        }
        return search(key) != null;
    }

    /**
     * Traverses and returns all keys in the B-Tree in ascending sorted order.
     *
     * @return {@link DynamicArray} containing sorted keys
     */
    public DynamicArray<T> traversal() {
        DynamicArray<T> result = new DynamicArray<>(size > 0 ? size : 10);
        if (root != null) {
            root.traverse(result);
        }
        return result;
    }

    /**
     * Alias for {@link #traversal()}.
     */
    public DynamicArray<T> inorderTraversal() {
        return traversal();
    }

    /**
     * Returns the height of the B-Tree.
     * Empty tree has height -1; a single root tree has height 0.
     *
     * @return tree height
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        int h = 0;
        BTreeNode<T> current = root;
        while (!current.isLeaf) {
            current = current.children.get(0);
            h++;
        }
        return h;
    }

    /**
     * Returns the number of keys stored in the B-Tree.
     *
     * @return key count
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the B-Tree is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the minimum degree of this B-Tree.
     *
     * @return degree parameter t
     */
    public int getDegree() {
        return t;
    }

    /**
     * Clears all keys from the B-Tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the root node for inspection and testing.
     */
    public BTreeNode<T> getRoot() {
        return root;
    }
}
