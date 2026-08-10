package gh.edu.ug.cs.ugmaintenance.datastructures.hashtable;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom hash table with separate chaining, built from scratch for the
 * campus maintenance platform (no {@code java.util.HashMap} under the hood).
 *
 * <p>Keys are mapped to buckets by {@link Object#hashCode()} (with an extra
 * bit-spread step to reduce clustering), and collisions are resolved by
 * chaining: every bucket is a singly linked list of {@link HashEntry}
 * nodes. Duplicate keys are not allowed — {@link #put(Object, Object)} on an
 * existing key overwrites the stored value.</p>
 *
 * <p>The table grows dynamically: once the current load factor
 * ({@code size / capacity}) reaches the configured threshold the bucket
 * array is doubled and every entry is rehashed into the new table. This
 * keeps chains short, giving O(1) <em>expected</em> time for
 * {@code put/get/remove}.</p>
 *
 * <p>For the empirical-efficiency lab the table records
 * {@link #getCollisionCount()} (every {@code put} of a <em>new</em> key into
 * an already-occupied bucket) and exposes the current load factor, capacity
 * and longest bucket, so collision statistics can be collected across
 * different table sizes as required by the assignment.</p>
 *
 * <p><strong>Preconditions:</strong> keys must be non-null
 * (an {@link IllegalArgumentException} is thrown otherwise) and must have a
 * stable {@code hashCode()}. Null <em>values</em> are allowed, so use
 * {@link #containsKey(Object)} rather than {@link #get(Object)} when you
 * need to distinguish "absent" from "present with a null value".</p>
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public class HashTable<K, V> {

    /** Default number of buckets used by the no-argument constructor. */
    public static final int DEFAULT_CAPACITY = 16;

    /**
     * Default growth threshold: the table is resized once
     * {@code size / capacity} reaches this value.
     */
    public static final double DEFAULT_LOAD_FACTOR = 0.75;

    private static final int MAX_CAPACITY = 1 << 30;

    /**
     * A key/value pair stored in a bucket chain. Package-private so the
     * report can trace bucket contents if needed.
     */
    static final class HashEntry<K, V> {
        final K key;
        V value;
        HashEntry<K, V> next;

        HashEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private HashEntry<K, V>[] table;
    private int size;
    private final double maxLoadFactor;
    private long collisionCount;
    private long resizeCount;

    /** Creates a table with {@link #DEFAULT_CAPACITY} buckets. */
    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a table with the given initial bucket count.
     *
     * @param initialCapacity requested number of buckets; rounded up to a
     *                        power of two so the index mask {@code & (n - 1)}
     *                        can be used instead of the slower {@code %}
     * @throws IllegalArgumentException when {@code initialCapacity <= 0}
     */
    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a table with the given bucket count and growth threshold.
     *
     * @param initialCapacity requested number of buckets (rounded up to a
     *                        power of two)
     * @param maxLoadFactor   threshold at which the table doubles; must be
     *                        strictly between 0 and 1 inclusive
     * @throws IllegalArgumentException when the capacity is not positive or
     *                                  the load factor is outside (0, 1]
     */
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, double maxLoadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Initial capacity must be greater than zero: " + initialCapacity);
        }
        if (maxLoadFactor <= 0.0 || maxLoadFactor > 1.0) {
            throw new IllegalArgumentException(
                    "Load factor must be in (0, 1]: " + maxLoadFactor);
        }
        this.table = (HashEntry<K, V>[]) new HashEntry[normalizeCapacity(initialCapacity)];
        this.maxLoadFactor = maxLoadFactor;
        this.size = 0;
        this.collisionCount = 0;
        this.resizeCount = 0;
    }

    /**
     * Associates {@code value} with {@code key}. If the key is already
     * present its value is replaced and the previous value returned.
     *
     * @param key   non-null key
     * @param value value to store (may be null)
     * @return the previous value, or {@code null} when the key was absent
     * @throws IllegalArgumentException when {@code key} is null
     */
    public V put(K key, V value) {
        requireKey(key);

        int index = hash(key);
        HashEntry<K, V> head = table[index];

        if (head == null) {
            // Fresh bucket: no collision.
            table[index] = new HashEntry<>(key, value);
            size++;
        } else {
            HashEntry<K, V> current = head;
            while (current != null) {
                if (key.equals(current.key)) {
                    V previous = current.value;
                    current.value = value;
                    return previous; // overwrite, size unchanged
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            // New key landing in an occupied bucket: count the collision.
            collisionCount++;
            current.next = new HashEntry<>(key, value);
            size++;
        }

        if (shouldResize()) {
            resize();
        }
        return null;
    }

    /**
     * Returns the value stored under {@code key}, or {@code null} when the
     * key is absent.
     *
     * @throws IllegalArgumentException when {@code key} is null
     */
    public V get(K key) {
        requireKey(key);

        HashEntry<K, V> current = table[hash(key)];
        while (current != null) {
            if (key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Removes {@code key} and returns its value, or {@code null} when the
     * key was absent.
     *
     * @throws IllegalArgumentException when {@code key} is null
     */
    public V remove(K key) {
        requireKey(key);

        int index = hash(key);
        HashEntry<K, V> head = table[index];
        if (head == null) {
            return null;
        }

        if (key.equals(head.key)) {
            table[index] = head.next;
            size--;
            return head.value;
        }

        HashEntry<K, V> previous = head;
        HashEntry<K, V> current = head.next;
        while (current != null) {
            if (key.equals(current.key)) {
                previous.next = current.next;
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    /**
     * Returns {@code true} when {@code key} is present (even when its value
     * is null).
     *
     * @throws IllegalArgumentException when {@code key} is null
     */
    public boolean containsKey(K key) {
        requireKey(key);

        HashEntry<K, V> current = table[hash(key)];
        while (current != null) {
            if (key.equals(current.key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Number of key/value pairs stored. */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Removes every entry, keeping the current bucket array. */
    public void clear() {
        java.util.Arrays.fill(table, null);
        size = 0;
        collisionCount = 0;
        resizeCount = 0;
    }

    /**
     * All keys currently stored, in bucket order (first bucket to last, head
     * of each chain to tail). Used by {@link Set}, {@link Map#keySet()} and
     * report traces.
     */
    public List<K> keys() {
        List<K> result = new ArrayList<>(size);
        for (HashEntry<K, V> entry : table) {
            for (HashEntry<K, V> current = entry; current != null; current = current.next) {
                result.add(current.key);
            }
        }
        return result;
    }

    /**
     * All values currently stored, in the same order as {@link #keys()}.
     */
    public List<V> values() {
        List<V> result = new ArrayList<>(size);
        for (HashEntry<K, V> entry : table) {
            for (HashEntry<K, V> current = entry; current != null; current = current.next) {
                result.add(current.value);
            }
        }
        return result;
    }

    /** Current number of buckets. */
    public int capacity() {
        return table.length;
    }

    /** Current load factor {@code size / capacity}. */
    public double loadFactor() {
        return (double) size / table.length;
    }

    /**
     * Total number of collisions observed: every {@link #put(Object, Object)}
     * of a <em>new</em> key into a bucket that already holds at least one
     * entry. Updates of existing keys and rehashes during growth are not
     * counted, so the figure accumulates over the lifetime of the table.
     */
    public long getCollisionCount() {
        return collisionCount;
    }

    /** Number of times the bucket array was doubled. */
    public long getResizeCount() {
        return resizeCount;
    }

    /** Length of the longest bucket chain (0 for an empty table). */
    public int getMaxBucketLength() {
        int max = 0;
        for (HashEntry<K, V> entry : table) {
            int length = 0;
            for (HashEntry<K, V> current = entry; current != null; current = current.next) {
                length++;
            }
            if (length > max) {
                max = length;
            }
        }
        return max;
    }

    /** Returns the raw chain at a bucket index. Exposed for trace tables. */
    HashEntry<K, V> bucket(int index) {
        return table[index];
    }

    // ------------------------------------------------------------------
    // Internals
    // ------------------------------------------------------------------

    private boolean shouldResize() {
        return table.length < MAX_CAPACITY
                && (double) size / table.length >= maxLoadFactor;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        HashEntry<K, V>[] old = table;
        table = (HashEntry<K, V>[]) new HashEntry[old.length << 1];
        size = 0;
        resizeCount++;

        for (HashEntry<K, V> entry : old) {
            for (HashEntry<K, V> current = entry; current != null; current = current.next) {
                insertUncounted(current.key, current.value);
            }
        }
    }

    /**
     * Re-inserts an entry during growth without counting a collision (the
     * key already exists; only the bucket layout changed).
     */
    private void insertUncounted(K key, V value) {
        int index = hash(key);
        HashEntry<K, V> head = table[index];
        if (head == null) {
            table[index] = new HashEntry<>(key, value);
        } else {
            HashEntry<K, V> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new HashEntry<>(key, value);
        }
        size++;
    }

    /**
     * Maps a key to a bucket index. The bit-spread reduces collisions for
     * keys whose hash codes share low-order bits (e.g. multiples of 16).
     * {@code table.length} is always a power of two, so {@code & (n - 1)}
     * yields a valid, non-negative index.
     */
    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        h ^= (h >>> 7) ^ (h >>> 4);
        return h & (table.length - 1);
    }

    private void requireKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("HashTable keys cannot be null.");
        }
    }

    /** Rounds a requested capacity up to the next power of two. */
    private static int normalizeCapacity(int requested) {
        int capacity = 1;
        while (capacity < requested && capacity < MAX_CAPACITY) {
            capacity <<= 1;
        }
        return capacity;
    }

    @Override
    public String toString() {
        return "HashTable{capacity=" + table.length
                + ", size=" + size
                + ", loadFactor=" + String.format("%.2f", loadFactor())
                + ", collisions=" + collisionCount
                + ", resizes=" + resizeCount + "}";
    }
}
