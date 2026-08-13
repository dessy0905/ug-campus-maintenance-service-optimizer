package gh.edu.ug.cs.ugmaintenance.datastructures.stack;

import gh.edu.ug.cs.ugmaintenance.datastructures.Node;

public class Stack<T> {
    private Node<T> top;
    private int size;

    public Stack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public void display(){
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }
        Node<T> current = top;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty — cannot pop");
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty — cannot peek");
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }
}