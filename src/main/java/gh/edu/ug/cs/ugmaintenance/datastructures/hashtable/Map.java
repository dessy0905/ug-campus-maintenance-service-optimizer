package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.util.List;

/**
 * Custom map (dictionary) built on top of {@link HashTable} — no
 * {@code java.util.HashMap} under the hood. The map is a thin, defensive
 * wrapper that exposes key-value semantics over the hash table and reuses
 * the custom {@link Set} for {@link #keySet()}.
 *
 * <p>Example use in the campus system: map a location id to its name, or a
 * technician id to the number of jobs assigned, so lookups stay O(1)
 * expected.</p>
 *
 * <p><strong>Preconditions:</strong> keys must be non-null (an
 * {@link IllegalArgumentException} is thrown otherwise). Null values are
 * allowed; use {@link #containsKey(Object)} to distinguish "absent" from
 * "present with a null value".</p>
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public class Map<K, V> {

    private final HashTable<K, V> table;

    /** Creates an empty map backed by a default-size hash table. */
    public Map() {
        table = new HashTable<>();
    }

    /**
     * Creates an empty map backed by a hash table with the given initial
     * capacity.
     *
     * @throws IllegalArgumentException when {@code initialCapacity <= 0}
     */
    public Map(int initialCapacity) {
        table = new HashTable<>(initialCapacity);
    }

    /**
     * Associates {@code value} with {@code key}, replacing any previous
     * value.
     *
     * @return the previous value, or {@code null} when the key was absent
     * @throws IllegalArgumentException when {@code key} is null
     */
    public V put(K key, V value) {
        return table.put(key, value);
    }

    /**
     * Returns the value stored under {@code key}, or {@code null} when the
     * key is absent.
     *
     * @throws IllegalArgumentException when {@code key} is null
     */
    public V get(K key) {
        return table.get(key);
    }

    /**
     * Removes {@code key} and returns its value, or {@code null} when the
     * key was absent.
     *
     * @throws IllegalArgumentException when {@code key} is null
     */
    public V remove(K key) {
        return table.remove(key);
    }

    /**
     * Membership test for keys.
     *
     * @throws IllegalArgumentException when {@code key} is null
     */
    public boolean containsKey(K key) {
        return table.containsKey(key);
    }

    /**
     * The set of all keys currently in the map — a live snapshot built on
     * the custom {@link Set}.
     */
    public Set<K> keySet() {
        Set<K> keys = new Set<>();
        for (K key : table.keys()) {
            keys.add(key);
        }
        return keys;
    }

    /**
     * All values currently in the map, in bucket order.
     */
    public List<V> values() {
        return table.values();
    }

    /** Number of key/value pairs. */
    public int size() {
        return table.size();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    /** Removes all entries. */
    public void clear() {
        table.clear();
    }

    @Override
    public String toString() {
        return "Map{size=" + table.size() + ", entries=" + table.keys() + "}";
    }
}
