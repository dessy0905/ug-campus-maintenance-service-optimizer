package gh.edu.ug.cs.ugmaintenance.datastructures.disjointset;

import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashMap;

/**
 * Custom Generic Disjoint Set (Union-Find) data structure.
 *
 * <p>Maintains a collection of disjoint (non-overlapping) sets.
 * Employs two key optimizations to achieve near-constant amortized time complexity:
 * <ul>
 *   <li><b>Path Compression</b> in {@link #find(Object)}: flattens the tree structure
 *       so every visited node points directly to the set representative (root).</li>
 *   <li><b>Union by Rank</b> in {@link #union(Object, Object)}: attaches the shorter
 *       tree under the root of the taller tree to prevent tree degradation.</li>
 * </ul>
 *
 * <p>Uses the project's custom {@link HashMap} for internal mapping of elements
 * to their parents and ranks, avoiding Java's standard collection libraries.</p>
 *
 * @param <T> the type of elements maintained by this Disjoint Set
 */
public class DisjointSet<T> {

    /**
     * Maps each element to its parent in the disjoint set forest.
     * A root element points to itself.
     */
    private final HashMap<T, T> parent;

    /**
     * Stores the upper bound on the height (rank) of the tree for each root.
     */
    private final HashMap<T, Integer> rank;

    /**
     * Tracks the number of disjoint sets / connected components.
     */
    private int setCount;

    /**
     * Constructs an empty DisjointSet.
     */
    public DisjointSet() {
        this.parent = new HashMap<>();
        this.rank = new HashMap<>();
        this.setCount = 0;
    }

    /**
     * Constructs an empty DisjointSet with a specified initial capacity.
     *
     * @param initialCapacity initial capacity for internal hash maps
     */
    public DisjointSet(int initialCapacity) {
        this.parent = new HashMap<>(initialCapacity);
        this.rank = new HashMap<>(initialCapacity);
        this.setCount = 0;
    }

    /**
     * Creates a new set containing the single specified element.
     * If the element already exists, the operation has no effect.
     *
     * @param element the element to create a set for
     * @throws IllegalArgumentException if element is null
     */
    public void makeSet(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null in makeSet");
        }

        // If the element is already present, do not overwrite
        if (parent.containsKey(element)) {
            return;
        }

        // In a new singleton set, the element is its own parent with rank 0
        parent.put(element, element);
        rank.put(element, 0);
        setCount++;
    }

    /**
     * Finds and returns the representative (root) of the set containing the given element.
     * Employs path compression: every node along the path is made to point directly
     * to the root, optimizing future lookups.
     *
     * @param element the element whose set representative is to be found
     * @return the representative element of the set
     * @throws IllegalArgumentException if element is null or not found in the disjoint set
     */
    public T find(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot find null element");
        }

        if (!parent.containsKey(element)) {
            throw new IllegalArgumentException("Element does not exist in DisjointSet: " + element);
        }

        T p = parent.get(element);

        // If element is not its own parent, recursively find the root with path compression
        if (!element.equals(p)) {
            T root = find(p);
            parent.put(element, root); // Path compression: point directly to root
            return root;
        }

        return element;
    }

    /**
     * Merges (unions) the set containing element {@code first} with the set
     * containing element {@code second} using union by rank.
     *
     * @param first the first element
     * @param second the second element
     * @return {@code true} if two distinct sets were merged; {@code false} if they were already in the same set
     * @throws IllegalArgumentException if either element is null or not registered
     */
    public boolean union(T first, T second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Union elements cannot be null");
        }

        T rootFirst = find(first);
        T rootSecond = find(second);

        // Already in the same set
        if (rootFirst.equals(rootSecond)) {
            return false;
        }

        int rankFirst = rank.get(rootFirst);
        int rankSecond = rank.get(rootSecond);

        // Union by rank: attach the tree with lower rank under the tree with higher rank
        if (rankFirst < rankSecond) {
            parent.put(rootFirst, rootSecond);
        } else if (rankFirst > rankSecond) {
            parent.put(rootSecond, rootFirst);
        } else {
            // Equal ranks: make one root the parent of the other and increment rank
            parent.put(rootSecond, rootFirst);
            rank.put(rootFirst, rankFirst + 1);
        }

        setCount--;
        return true;
    }

    /**
     * Checks whether two elements belong to the same set (i.e. are connected).
     *
     * @param first the first element
     * @param second the second element
     * @return {@code true} if both elements share the same set representative, {@code false} otherwise
     * @throws IllegalArgumentException if either element is null or not registered
     */
    public boolean connected(T first, T second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Elements cannot be null in connected check");
        }
        return find(first).equals(find(second));
    }

    /**
     * Returns the total number of elements registered across all disjoint sets.
     *
     * @return total element count
     */
    public int size() {
        return parent.size();
    }

    /**
     * Returns the current number of disjoint sets (connected components).
     *
     * @return number of disjoint sets
     */
    public int getSetCount() {
        return setCount;
    }

    /**
     * Checks whether an element is present in the disjoint set.
     *
     * @param element the element to check
     * @return {@code true} if registered, {@code false} otherwise
     */
    public boolean contains(T element) {
        if (element == null) {
            return false;
        }
        return parent.containsKey(element);
    }

    /**
     * Checks whether the disjoint set contains no elements.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return parent.isEmpty();
    }

    /**
     * Clears all sets from this disjoint set.
     */
    public void clear() {
        parent.clear();
        rank.clear();
        setCount = 0;
    }
}
