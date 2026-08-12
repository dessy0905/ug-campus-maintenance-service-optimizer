package gh.edu.ug.cs.ugmaintenance.datastructures.hash;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

/**
 * A custom Map implemented on top of the {@link HashTable} (member #9 deliverable).
 *
 * <p>The map delegates all storage to an internal {@link HashTable} and adds
 * map-oriented convenience operations such as {@link #getOrDefault} and
 * {@link #putIfAbsent}. Because the map is only a thin wrapper, every
 * operation keeps the hash table's average O(1) time complexity.</p>
 *
 * <p>In the campus maintenance context a map is useful for look-up indexes,
 * e.g. {@code locationId -> location name} or {@code requestId -> request},
 * where a key is fetched in constant average time instead of scanning a list.</p>
 *
 * <p><b>Naming note:</b> this class deliberately shadows {@code java.util.Map}.
 * Any code that needs the standard library map must refer to it with its fully
 * qualified name {@code java.util.Map}.</p>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class HashMap<K, V> implements Map<K, V> {

    private final HashTable<K, V> table;

    public HashMap() {
        this.table = new HashTable<>();
    }

    public HashMap(int initialCapacity) {
        this.table = new HashTable<>(initialCapacity);
    }

    public HashMap(int initialCapacity, double loadFactorThreshold) {
        this.table = new HashTable<>(initialCapacity, loadFactorThreshold);
    }

    /**
     * Associates the given value with the given key, returning the previous
     * value if the key already existed, otherwise {@code null}.
     */

    @Override
    public V put(K key, V value) {
        return table.put(key, value);
    }

    @Override
    /** Returns the value bound to the key, or {@code null} if absent. */
    public V get(K key) {
        return table.get(key);
    }

    /** Returns the value bound to the key, or {@code defaultValue} if absent. */
    @Override
    public V getOrDefault(K key, V defaultValue) {
        if (!table.containsKey(key)) {
            return defaultValue;
        }
        return table.get(key);
    }

    /** Binds the value only if the key is not already present. */
    @Override
    public V putIfAbsent(K key, V value) {
        if (table.containsKey(key)) {
            return table.get(key);
        }
        return table.put(key, value);
    }

    /** Removes the entry for the key, returning the removed value or {@code null}. */
    @Override
    public V remove(K key) {
        return table.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return table.containsValue(value);
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

    /** All keys currently in the map. */
    @Override
    public List<K> keySet() {
        return table.keySet();
    }

    /** All values currently in the map. */
    @Override
    public List<V> values() {
        return table.values();
    }

    /** Current load factor of the underlying hash table. */
    public double getLoadFactor() {
        return table.getLoadFactor();
    }

    /** Collision statistics of the underlying hash table (efficiency lab evidence). */
    public int getCollisionCount() {
        return table.getCollisionCount();
    }

    /** Prints the map entries (demo/evidence aid). */
    @Override
    public void display() {
        List<K> keys = table.keySet();
        System.out.print("Map{");
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            System.out.print(key + "=" + table.get(key));
            if (i < keys.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
}
