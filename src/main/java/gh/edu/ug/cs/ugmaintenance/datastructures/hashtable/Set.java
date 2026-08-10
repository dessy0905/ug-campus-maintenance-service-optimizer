package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.util.List;

/**
 * A custom Set implemented on top of the {@link HashTable} (member #9 deliverable).
 *
 * <p>Every element is stored as a key in a {@code HashTable<T, Object>} with a
 * shared sentinel value. Membership, insertion and deletion therefore inherit
 * the hash table's average O(1) complexity, and duplicate elements are
 * automatically rejected by the underlying table.</p>
 *
 * <p>In the campus maintenance context sets are useful for membership checks,
 * e.g. "which technicians are on duty", "which locations are in the dispatch
 * zone", or combining groups with union/intersection/difference.</p>
 *
 * <p><b>Naming note:</b> this class deliberately shadows {@code java.util.Set}.
 * Any code that needs the standard library set must refer to it with its fully
 * qualified name {@code java.util.Set}.</p>
 *
 * @param <T> the type of elements maintained by this set
 */
public class Set<T> {

    private static final Object PRESENT = new Object();

    private final HashTable<T, Object> table;

    public Set() {
        this.table = new HashTable<>();
    }

    public Set(int initialCapacity) {
        this.table = new HashTable<>(initialCapacity);
    }

    /**
     * Adds an element to the set.
     *
     * @return {@code true} if the element was newly added,
     *         {@code false} if it was already present
     * @throws IllegalArgumentException if {@code element} is {@code null}
     */
    public boolean add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Set does not accept null elements");
        }
        return table.put(element, PRESENT) == null;
    }

    /**
     * Removes an element from the set.
     *
     * @return {@code true} if the element was present and removed
     */
    public boolean remove(T element) {
        return table.remove(element) != null;
    }

    /** Returns {@code true} if the element is a member of the set. */
    public boolean contains(T element) {
        return table.containsKey(element);
    }

    public int size() {
        return table.size();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    public void clear() {
        table.clear();
    }

    /** All elements of the set. */
    public List<T> toList() {
        return table.keySet();
    }

    /** Union: elements in this set or in {@code other}. */
    public Set<T> union(Set<T> other) {
        Set<T> result = new Set<>(table.getCapacity() + other.table.getCapacity());
        for (T element : this.toList()) {
            result.add(element);
        }
        for (T element : other.toList()) {
            result.add(element);
        }
        return result;
    }

    /** Intersection: elements in both this set and {@code other}. */
    public Set<T> intersection(Set<T> other) {
        Set<T> result = new Set<>(Math.min(table.getCapacity(), other.table.getCapacity()));
        for (T element : this.toList()) {
            if (other.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /** Difference: elements in this set but not in {@code other}. */
    public Set<T> difference(Set<T> other) {
        Set<T> result = new Set<>(table.getCapacity());
        for (T element : this.toList()) {
            if (!other.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /** Returns {@code true} if every element of this set is also in {@code other}. */
    public boolean isSubsetOf(Set<T> other) {
        for (T element : this.toList()) {
            if (!other.contains(element)) {
                return false;
            }
        }
        return true;
    }

    /** Prints the elements of the set (demo/evidence aid). */
    public void display() {
        System.out.println("Set" + table.keySet());
    }
}
