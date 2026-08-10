package gh.edu.ug.cs.ugmaintenance.datastructures.array;

import java.util.Arrays;

public class DynamicArray<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] elements;
    private int size;

    public DynamicArray() {
        elements = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public DynamicArray(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }

        elements = (T[]) new Object[capacity];
        size = 0;
    }

    public void add(T element) {
        ensureCapacity();
        elements[size] = element;
        size++;
    }

    public T get(int index) {
        checkIndex(index);
        return elements[index];
    }

    public void set(int index, T element) {
        checkIndex(index);
        elements[index] = element;
    }

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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    public int capacity() {
        return elements.length;
    }

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

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size
            );
        }
    }
}