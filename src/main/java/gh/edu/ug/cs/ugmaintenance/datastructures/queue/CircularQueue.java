package gh.edu.ug.cs.ugmaintenance.datastructures.queue;

public class CircularQueue<T> {
    private Object[] items;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.items = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(T data) {
        if (isFull()) {
            throw new RuntimeException("Circular Queue is full — cannot enqueue");
        }
        rear = (rear + 1) % capacity;
        items[rear] = data;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Circular Queue is empty — cannot dequeue");
        }
        T data = (T) items[front];
        items[front] = null;
        front = (front + 1) % capacity;
        size--;
        return data;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Circular Queue is empty — cannot peek");
        }
        return (T) items[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int size() {
        return size;
    }
}