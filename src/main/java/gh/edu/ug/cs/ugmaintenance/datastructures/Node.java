package gh.edu.ug.cs.ugmaintenance.datastructures;

public class Node<T> {

    public T data;
    public Node<T> next;
    public Node<T> previous;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.previous = null;
    }
}