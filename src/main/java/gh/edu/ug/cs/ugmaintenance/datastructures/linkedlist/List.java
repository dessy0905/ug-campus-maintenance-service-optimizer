package gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist;

public interface List <T> {
    /**
     * Appends an element to the end of the list.
     */
    void add(T element);

    /**
     * Inserts an element at the specified index.
     */
    void add(int index, T element);

    /**
     * Returns the element at the specified index.
     */
    T get(int index);

    /**
     * Replaces the element at the specified index with a new element.
     */
    void set(int index, T element);

    /**
     * Removes and returns the element at the specified index.
     */
    T remove(int index);

    /**
     * Checks if the list contains the specified element.
     */
    boolean contains(T element);

    /**
     * Returns the current number of elements in the list.
     */
    int size();

    /**
     * Checks whether the list has no elements.
     */
    boolean isEmpty();

    /**
     * Removes all elements from the list.
     */
    void clear();
    
}
