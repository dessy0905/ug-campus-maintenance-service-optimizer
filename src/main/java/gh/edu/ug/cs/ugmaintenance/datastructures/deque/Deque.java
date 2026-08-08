package gh.edu.ug.cs.ugmaintenance.datastructures.deque;

public class Deque<T> {
    private DoublyNode<T> front;
    private DoublyNode<T> rear;
    private int size;

    public Deque() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void addFront(T data) {
        DoublyNode<T> newNode = new DoublyNode<>(data);
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
        DoublyNode<T> newNode = new DoublyNode<>(data);
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

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }
}