package gh.edu.ug.cs.ugmaintenance.datastructures.hash;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;   
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
public class HashSet<T> implements Set<T> {

    private static final Object PRESENT = new Object();

    private final HashTable<T, Object> table;

    public HashSet() {
        this.table = new HashTable<>();
    }

    public HashSet(int initialCapacity) {
        this.table = new HashTable<>(initialCapacity);
    }

    /**
     * Adds an element to the set.
     *
     * @return {@code true} if the element was newly added,
     *         {@code false} if it was already present
     * @throws IllegalArgumentException if {@code element} is {@code null}
     */

    @Override
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
    @Override
    public boolean remove(T element) {
        return table.remove(element) != null;
    }

    /** Returns {@code true} if the element is a member of the set. */
    
    @Override
    public boolean contains(T element) {
        return table.containsKey(element);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    @Override
    public void clear() {
        table.clear();
    }

    /** All elements of the set. */
    @Override
    public List<T> toList() {
        return table.keySet();
    }

    /** Union: elements in this set or in {@code other}. */
    @Override
    public HashSet<T> union(Set<T> other) {
        HashSet<T> result = new HashSet<>();
        List<T> thisElements = this.toList();
        for (int i = 0; i < thisElements.size(); i++) {
            result.add(thisElements.get(i));
        }
        List<T> otherElements = other.toList();
        for (int i = 0; i < otherElements.size(); i++) {
            result.add(otherElements.get(i));
        }
        return result;
    }

    /** Intersection: elements in both this set and {@code other}. */
    @Override
    public HashSet<T> intersection(Set<T> other) {
        HashSet<T> result = new HashSet<>();
        List<T> thisElements = this.toList();
        for (int i = 0; i < thisElements.size(); i++) {
            T element = thisElements.get(i);
            if (other.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /** Difference: elements in this set but not in {@code other}. */
    @Override
    public HashSet<T> difference(Set<T> other) {
        HashSet<T> result = new HashSet<>();
        List<T> thisElements = this.toList();
        for (int i = 0; i < thisElements.size(); i++) {
            T element = thisElements.get(i);
            if (!other.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /** Returns {@code true} if every element of this set is also in {@code other}. */
    @Override
    public boolean isSubsetOf(Set<T> other) {
        List<T> thisElements = this.toList();
        for (int i = 0; i < thisElements.size(); i++) {
            if (!other.contains(thisElements.get(i))) {
                return false;
            }
        }
        return true;
    }

    /** Prints the elements of the set (demo/evidence aid). */
    @Override
    public void display() {
        List<T> elements = table.keySet();
        System.out.print("Set{");
        for (int i = 0; i < elements.size(); i++) {
            System.out.print(elements.get(i));
            if (i < elements.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
}
