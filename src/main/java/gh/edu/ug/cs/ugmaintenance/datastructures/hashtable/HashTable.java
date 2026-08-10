package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom hash table (key-value store) implemented from scratch using
 * separate chaining for collision handling.
 *
 * <p>Responsible member: 9 - Cheryl Abena Asantewaa Kwakye
 * (Hash Table, Set, Map)</p>
 *
 * <p>Design notes:</p>
 * <ul>
 *   <li>Keys are hashed with {@code key.hashCode()} and mapped to a bucket
 *       index using {@code (hash & 0x7fffffff) % capacity}.</li>
 *   <li>Collisions are resolved with separate chaining: each bucket is a
 *       singly linked list of entries.</li>
 *   <li>When the load factor ({@code size / capacity}) exceeds the configured
 *       threshold the table is resized (doubled) and every entry is rehashed,
 *       keeping look-ups close to O(1) average.</li>
 *   <li>Collision statistics (load factor, collision count, longest chain,
 *       average chain length) are exposed for the efficiency lab
 *       (Section 9: hash table load factor experiment).</li>
 * </ul>
 *
 * <p>Preconditions:</p>
 * <ul>
 *   <li>{@code key} must not be {@code null} for any operation.</li>
 *   <li>Initial capacity must be positive.</li>
 *   <li>Load factor threshold must be in the range (0, 1].</li>
 * </ul>
 *
 * @param <K> the type of keys maintained by this table
 * @param <V> the type of mapped values
 */
public class HashTable<K, V> {

    /** A single key-value pair stored in a bucket chain. */
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

    private Entry<K, V>[] table;      // array of buckets (separate chaining)
    private int size;                 // number of key-value pairs
    private int capacity;             // number of buckets
    private double loadFactorThreshold;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR_THRESHOLD);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR_THRESHOLD);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, double loadFactorThreshold) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive, got " + initialCapacity);
        }
        if (loadFactorThreshold <= 0 || loadFactorThreshold > 1) {
            throw new IllegalArgumentException(
                    "Load factor threshold must be in the range (0, 1], got " + loadFactorThreshold);
        }
        this.capacity = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        this.table = (Entry<K, V>[]) new Entry[initialCapacity];
        this.size = 0;
    }

    /** Maps a key to a bucket index. Uses only the hash, so negative hashes are normalised. */
    private int indexFor(K key) {
        int hash = key.hashCode();
        return (hash & 0x7fffffff) % capacity;
    }

    /**
     * Associates the given value with the given key.
     * If the key already exists its value is overwritten.
     *
     * @return the previous value bound to the key, or {@code null} if the key was new
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("HashTable does not accept null keys");
        }
        int index = indexFor(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                V oldValue = current.value;
                current.value = value;          // overwrite existing key
                return oldValue;
            }
            current = current.next;
        }
        // Key not present: insert a new entry at the head of the bucket chain.
        Entry<K, V> newNode = new Entry<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        if (getLoadFactor() > loadFactorThreshold) {
            resize(capacity * 2);
        }
        return null;
    }

    /**
     * Returns the value bound to the given key, or {@code null} if the key is absent.
     *
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("HashTable does not accept null keys");
        }
        int index = indexFor(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /** Returns {@code true} if the table contains an entry for the given key. */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("HashTable does not accept null keys");
        }
        int index = indexFor(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Returns {@code true} if at least one entry has the given value. */
    public boolean containsValue(V value) {
        for (Entry<K, V> head : table) {
            Entry<K, V> current = head;
            while (current != null) {
                if (current.value == null ? value == null : current.value.equals(value)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    /**
     * Removes the entry for the given key if present.
     *
     * @return the value that was bound to the key, or {@code null} if the key was absent
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("HashTable does not accept null keys");
        }
        int index = indexFor(key);
        Entry<K, V> previous = null;
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    /** Doubles the bucket array and rehashes every entry into the new table. */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        capacity = newCapacity;
        table = (Entry<K, V>[]) new Entry[newCapacity];
        size = 0;
        for (Entry<K, V> head : oldTable) {
            Entry<K, V> current = head;
            while (current != null) {
                Entry<K, V> next = current.next;
                int index = indexFor(current.key);
                current.next = table[index];
                table[index] = current;
                size++;
                current = next;
            }
        }
    }

    /** Removes all entries. The bucket count is kept. */
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (Entry<K, V>[]) new Entry[capacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Number of buckets currently in the table. */
    public int getCapacity() {
        return capacity;
    }

    /** Current load factor {@code size / capacity}. */
    public double getLoadFactor() {
        return (double) size / capacity;
    }

    /** The configured load factor threshold that triggers resizing. */
    public double getLoadFactorThreshold() {
        return loadFactorThreshold;
    }

    /**
     * Number of entries that share a bucket with at least one other entry
     * (i.e. {@code size - number of non-empty buckets}). This is the number of
     * keys that "lost" their private slot because of a collision.
     */
    public int getCollisionCount() {
        int nonEmptyBuckets = 0;
        for (Entry<K, V> head : table) {
            if (head != null) {
                nonEmptyBuckets++;
            }
        }
        return size - nonEmptyBuckets;
    }

    /** Longest chain (bucket) in the table. */
    public int getMaxChainLength() {
        int max = 0;
        for (Entry<K, V> head : table) {
            int length = 0;
            Entry<K, V> current = head;
            while (current != null) {
                length++;
                current = current.next;
            }
            if (length > max) {
                max = length;
            }
        }
        return max;
    }

    /** Average number of entries per occupied bucket (0 for an empty table). */
    public double getAverageChainLength() {
        int nonEmptyBuckets = 0;
        for (Entry<K, V> head : table) {
            if (head != null) {
                nonEmptyBuckets++;
            }
        }
        return nonEmptyBuckets == 0 ? 0.0 : (double) size / nonEmptyBuckets;
    }

    /** All keys currently in the table. */
    public List<K> keySet() {
        List<K> keys = new ArrayList<>();
        for (Entry<K, V> head : table) {
            Entry<K, V> current = head;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }
        return keys;
    }

    /** All values currently in the table. */
    public List<V> values() {
        List<V> values = new ArrayList<>();
        for (Entry<K, V> head : table) {
            Entry<K, V> current = head;
            while (current != null) {
                values.add(current.value);
                current = current.next;
            }
        }
        return values;
    }

    /** Prints a readable summary of the current table state (demo/evidence aid). */
    public void display() {
        System.out.println("HashTable{size=" + size
                + ", capacity=" + capacity
                + ", loadFactor=" + String.format("%.3f", getLoadFactor())
                + ", collisions=" + getCollisionCount()
                + ", maxChain=" + getMaxChainLength()
                + ", avgChain=" + String.format("%.3f", getAverageChainLength()) + "}");
    }
}
