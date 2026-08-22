package gh.edu.ug.cs.ugmaintenance.datastructures.tree;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;

/**
 * Node representation for a B-Tree of minimum degree {@code t}.
 *
 * <p>Properties:
 * <ul>
 *   <li>Contains between {@code t - 1} and {@code 2t - 1} keys (except root, which can have fewer).</li>
 *   <li>If internal (not a leaf), has {@code numKeys + 1} children.</li>
 *   <li>All keys within this node are maintained in strictly ascending sorted order.</li>
 * </ul>
 *
 * @param <T> key element type
 */
public class BTreeNode<T extends Comparable<T>> {

    public final int t;
    public final DynamicArray<T> keys;
    public final DynamicArray<BTreeNode<T>> children;
    public boolean isLeaf;

    /**
     * Constructs a B-Tree node.
     *
     * @param t minimum degree
     * @param isLeaf true if this node is a leaf, false if internal
     */
    public BTreeNode(int t, boolean isLeaf) {
        this.t = t;
        this.isLeaf = isLeaf;
        this.keys = new DynamicArray<>(2 * t);
        this.children = new DynamicArray<>(2 * t + 1);
    }

    /**
     * Searches for a key in the subtree rooted at this node.
     *
     * @param key the key to locate
     * @return the found key or {@code null} if not present
     */
    public T search(T key) {
        int i = 0;
        int numKeys = keys.size();

        // Find the first key greater than or equal to key
        while (i < numKeys && key.compareTo(keys.get(i)) > 0) {
            i++;
        }

        // If the key is present in this node, return it
        if (i < numKeys && key.compareTo(keys.get(i)) == 0) {
            return keys.get(i);
        }

        // If this is a leaf node, key is not in the tree
        if (isLeaf) {
            return null;
        }

        // Otherwise, recurse on the appropriate child
        return children.get(i).search(key);
    }

    /**
     * Collects all keys in the subtree rooted at this node in ascending order.
     *
     * @param result dynamic array accumulating the traversal
     */
    public void traverse(DynamicArray<T> result) {
        int numKeys = keys.size();
        for (int i = 0; i < numKeys; i++) {
            if (!isLeaf) {
                children.get(i).traverse(result);
            }
            result.add(keys.get(i));
        }

        if (!isLeaf) {
            children.get(numKeys).traverse(result);
        }
    }

    /**
     * Returns the number of keys currently stored in this node.
     */
    public int getKeyCount() {
        return keys.size();
    }

    /**
     * Returns the number of children currently attached to this node.
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Checks if this node contains the maximum allowable number of keys (2t - 1).
     */
    public boolean isFull() {
        return keys.size() >= (2 * t - 1);
    }
}
