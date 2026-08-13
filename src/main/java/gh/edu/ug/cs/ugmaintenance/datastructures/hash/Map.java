package gh.edu.ug.cs.ugmaintenance.datastructures.hash;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

public interface Map <K, V> {
    V put(K key, V value);

    V get(K key);

    V getOrDefault(K key, V defaultValue);

    V putIfAbsent(K key, V value);

    V remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    int size();

    boolean isEmpty();

    void clear();
    
    List<K> keySet();
    List<V> values();

    void display();
    
}
