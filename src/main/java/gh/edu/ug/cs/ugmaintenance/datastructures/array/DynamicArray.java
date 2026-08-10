package gh.edu.ug.cs.ugmaintenance.datastructures.array;

import java.util.Arrays;

public class DynamicArray<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public DynamicArray() {
        elements = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public DynamicArray(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }

        elements = (T[]) new Object[capacity];
        size = 0;
    }

    // Add an element to the end of the array
    public void add(T element) {
        ensureCapacity();
        elements[size] = element;
        size++;
    }

    // Add an element at a specific index
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }

        ensureCapacity();

        // Shift elements one position to the right
        System.arraycopy(
                elements,
                index,
                elements,
                index + 1,
                size - index
        );

        elements[index] = element;
        size++;
    }

    // Get an element at a specific index
    public T get(int index) {
        checkIndex(index);
        return elements[index];
    }

    // Replace an element at a specific index
    public void set(int index, T element) {
        checkIndex(index);
        elements[index] = element;
    }

    // Remove an element at a specific index
    public T remove(int index) {
        checkIndex(index);

        T removedElement = elements[index];

        int elementsToMove = size - index - 1;

        if (elementsToMove > 0) {
            System.arraycopy(
                    elements,
                    index + 1,
                    elements,
                    index,
                    elementsToMove
            );
        }

        elements[size - 1] = null;
        size--;

        return removedElement;
    }

    // Check whether an element exists
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (element == null) {
                if (elements[i] == null) {
                    return true;
                }
            } else if (element.equals(elements[i])) {
                return true;
            }
        }

        return false;
    }

    // Return the number of elements
    public int size() {
        return size;
    }

    // Check whether the array is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove all elements
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    // Return the current capacity
    public int capacity() {
        return elements.length;
    }

    // Increase capacity when the array is full
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size < elements.length) {
            return;
        }

        int newCapacity;

        if (elements.length == 0) {
            newCapacity = 1;
        } else {
            newCapacity = elements.length * 2;
        }

        T[] newElements = (T[]) new Object[newCapacity];

        System.arraycopy(
                elements,
                0,
                newElements,
                0,
                size
        );

        elements = newElements;
    }

    // Validate an index for existing elements
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }
}