package gh.edu.ug.cs.ugmaintenance.datastructures.deque;

import gh.edu.ug.cs.ugmaintenance.datastructures.Node;

public class Deque<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    public Deque() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void addFront(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.next = front;
            front.previous = newNode;
            front = newNode;
        }
        size++;
    }

    public void addRear(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.previous = rear;
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public T removeFront() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty — cannot removeFront");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        } else {
            front.previous = null;
        }
        size--;
        return data;
    }

    public T removeRear() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty — cannot removeRear");
        }
        T data = rear.data;
        rear = rear.previous;
        if (rear == null) {
            front = null;
        } else {
            rear.next = null;
        }
        size--;
        return data;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("Deque is empty");
            return;
        }
        Node<T> current = front;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }
}