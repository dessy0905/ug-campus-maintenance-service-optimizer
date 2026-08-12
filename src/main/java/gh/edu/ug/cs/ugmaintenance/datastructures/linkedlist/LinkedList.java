package gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist;

import gh.edu.ug.cs.ugmaintenance.datastructures.Node;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

public class LinkedList<T>  implements List<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Add an element to the end of the list
    @Override
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.previous = tail;
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    // Add an element at a specific index
    @Override
    public void add(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }

        if (index == size) {
            add(data);
            return;
        }

        Node<T> newNode = new Node<>(data);

        if (index == 0) {
            newNode.next = head;
            head.previous = newNode;
            head = newNode;
            size++;
            return;
        }

        Node<T> current = getNode(index);

        newNode.next = current;
        newNode.previous = current.previous;

        current.previous.next = newNode;
        current.previous = newNode;

        size++;
    }

    // Get an element at a specific index
    @Override
    public T get(int index) {
        return getNode(index).data;
    }

    // Replace an element at a specific index
    @Override
    public void set(int index, T data) {
        getNode(index).data = data;
    }

    // Remove an element at a specific index
    @Override
    public T remove(int index) {
        Node<T> current = getNode(index);
        T removedData = current.data;

        if (current.previous != null) {
            current.previous.next = current.next;
        } else {
            head = current.next;
        }

        if (current.next != null) {
            current.next.previous = current.previous;
        } else {
            tail = current.previous;
        }

        current.data = null;
        current.next = null;
        current.previous = null;

        size--;

        return removedData;
    }

    // Check whether an element exists
    @Override
    public boolean contains(T data) {
        Node<T> current = head;

        while (current != null) {
            if (data == null) {
                if (current.data == null) {
                    return true;
                }
            } else if (data.equals(current.data)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    // Return the number of elements
    @Override
    public int size() {
        return size;
    }

    // Check whether the list is empty
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove all elements
    @Override
    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> nextNode = current.next;
            current.data = null;
            current.next = null;
            current.previous = null;
            current = nextNode;
        }        
        head = null;
        tail = null;
        size = 0;
    }

    // Get the first element
    public T getFirst() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Linked List is empty");
        }

        return head.data;
    }

    // Get the last element
    public T getLast() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Linked List is empty");
        }

        return tail.data;
    }

    // Find a node by index
    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }

        Node<T> current;

        // Traverse from the beginning if index is closer to the head
        if (index < size / 2) {
            current = head;

            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        }

        // Traverse from the end if index is closer to the tail
        else {
            current = tail;

            for (int i = size - 1; i > index; i--) {
                current = current.previous;
            }
        }

        return current;
    }
}