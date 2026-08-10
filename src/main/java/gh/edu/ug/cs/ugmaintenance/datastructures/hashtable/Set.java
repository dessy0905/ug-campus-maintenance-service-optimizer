package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

/**
 * Custom set built on top of {@link HashTable} — no {@code java.util.HashSet}
 * under the hood. Stores each element exactly once; membership tests are the
 * O(1) expected hash-table lookup.
 *
 * <p>Example use in the campus system: keep the set of service categories
 * that currently have open requests, or deduplicate technician names when
 * compiling report statistics.</p>
 *
 * <p><strong>Preconditions:</strong> elements must be non-null (an
 * {@link IllegalArgumentException} is thrown otherwise) and must have a
 * stable {@code hashCode()}.</p>
 *
 * @param <T> type of elements in the set
 */
public class Set<T> {

    private final HashTable<T, Boolean> table;

    /** Creates an empty set backed by a default-size hash table. */
    public Set() {
        this(HashTable.DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty set backed by a hash table with the given initial
     * capacity.
     *
     * @throws IllegalArgumentException when {@code initialCapacity <= 0}
     */
    public Set(int initialCapacity) {
        table = new HashTable<>(initialCapacity);
    }

    /**
     * Adds {@code element} if it is not already present.
     *
     * @return {@code true} when the element was added, {@code false} when it
     *         was already in the set (set unchanged)
     * @throws IllegalArgumentException when {@code element} is null
     */
    public boolean add(T element) {
        if (table.containsKey(element)) {
            return false;
        }
        table.put(element, Boolean.TRUE);
        return true;
    }

    /**
     * Removes {@code element} if present.
     *
     * @return {@code true} when the element was removed, {@code false} when
     *         it was not in the set
     * @throws IllegalArgumentException when {@code element} is null
     */
    public boolean remove(T element) {
        return table.remove(element) != null;
    }

    /**
     * Membership test — the set/map lookup use case required by the
     * assignment.
     *
     * @throws IllegalArgumentException when {@code element} is null
     */
    public boolean contains(T element) {
        return table.containsKey(element);
    }

    /** Number of distinct elements. */
    public int size() {
        return table.size();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    /** Removes all elements. */
    public void clear() {
        table.clear();
    }

    /**
     * All elements, in the underlying table's bucket order.
     */
    public Object[] toArray() {
        return table.keys().toArray();
    }

    @Override
    public String toString() {
        return "Set{" + table.keys() + "}";
    }
}
