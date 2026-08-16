package gh.edu.ug.cs.ugmaintenance.datastructures.redblacktree;

/**
 * Represents a node in the Red-Black Tree.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class RedBlackTreeNode<K extends Comparable<K>, V> {

    static final boolean RED = true;
    static final boolean BLACK = false;

    K key;
    V value;

    boolean color;

    RedBlackTreeNode<K, V> left;
    RedBlackTreeNode<K, V> right;
    RedBlackTreeNode<K, V> parent;

    /**
     * Creates a new Red-Black Tree node.
     * New nodes are initially RED.
     */
    public RedBlackTreeNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.color = RED;
    }
}s