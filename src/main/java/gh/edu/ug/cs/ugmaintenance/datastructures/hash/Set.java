package gh.edu.ug.cs.ugmaintenance.datastructures.hash;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

public interface Set <T>{
    boolean add(T element);

    boolean remove(T element);

    boolean contains(T element);

    int size();

    boolean isEmpty();

    void clear();

    List<T> toList();

    Set<T> union(Set<T> other);

    Set<T> intersection(Set<T> other);

    Set<T> difference(Set<T> other);

    boolean isSubsetOf(Set<T> other);

    void display();
    
}
