package gh.edu.ug.cs.ugmaintenance.datastructures.queue;

import gh.edu.ug.cs.ugmaintenance.datastructures.Node;

public class Queue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty — cannot dequeue");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty — cannot peek");
        }
        return front.data;
    }

        public void display(){
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }
        Node<T> current = front;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }
}