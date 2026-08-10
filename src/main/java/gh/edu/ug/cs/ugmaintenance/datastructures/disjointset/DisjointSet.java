package gh.edu.ug.cs.ugmaintenance.datastructures.disjointset;

import java.util.Arrays;

/**
 * Disjoint-set (union-find) structure built from scratch for the route engine.
 *
 * <p>Operations provided:</p>
 * <ul>
 *   <li>{@link #makeSet(int)} - places an element in its own singleton set;</li>
 *   <li>{@link #find(int)}    - returns the representative of a set, applying
 *       full path compression along the way;</li>
 *   <li>{@link #union(int, int)} - merges two sets, attaching the shorter tree
 *       under the taller one (union by rank); component sizes are tracked.</li>
 * </ul>
 *
 * <p>Kruskal's algorithm uses this structure to detect cycles cheaply while
 * building the minimum spanning tree of the campus road network.</p>
 */
public class DisjointSet {

    private final int[] parent;
    private final int[] rank;
    private final int[] size;

    /**
     * Creates {@code n} disjoint sets, one per element {@code 0 .. n-1}.
     *
     * @param n number of elements; must be greater than zero
     */
    public DisjointSet(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("DisjointSet size must be greater than zero.");
        }
        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            makeSet(i);
        }
    }

    /**
     * Resets an element so it forms its own singleton set.
     *
     * <p>Intended for elements not yet merged into a larger set (the
     * constructor). Resetting an element that is the root of a merged set
     * leaves its former children still pointing at it, which would make
     * {@link #getSize(int)} stale.</p>
     */
    public void makeSet(int element) {
        validate(element);
        parent[element] = element;
        rank[element] = 0;
        size[element] = 1;
    }

    /**
     * Returns the representative of the set containing {@code element},
     * compressing the path so future finds are faster (almost O(1) amortised).
     */
    public int find(int element) {
        validate(element);

        int root = element;
        while (parent[root] != root) {
            root = parent[root];
        }

        // Second pass: point every node on the path straight at the root.
        int current = element;
        while (current != root) {
            int next = parent[current];
            parent[current] = root;
            current = next;
        }

        return root;
    }

    /**
     * Merges the sets containing {@code x} and {@code y} (union by rank).
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;
        }

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
            size[rootX] += size[rootY];
        }
    }

    /**
     * Returns true when both elements belong to the same set.
     */
    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }

    /**
     * Returns the number of elements in the set containing {@code element}.
     */
    public int getSize(int element) {
        return size[find(element)];
    }

    /**
     * Counts how many disjoint sets currently exist.
     */
    public int countComponents() {
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) {
                count++;
            }
        }
        return count;
    }

    /**
     * Raw parent pointer of an element. Exposed for trace-table evidence in
     * the written report (Kruskal connectivity trace).
     */
    public int getParent(int element) {
        validate(element);
        return parent[element];
    }

    /**
     * Total number of elements tracked by this structure.
     */
    public int size() {
        return parent.length;
    }

    private void validate(int element) {
        if (element < 0 || element >= parent.length) {
            throw new IllegalArgumentException(
                    "Element out of range [0, " + (parent.length - 1) + "]: " + element);
        }
    }

    @Override
    public String toString() {
        return "DisjointSet{components=" + countComponents()
                + ", parents=" + Arrays.toString(parent) + "}";
    }
}
