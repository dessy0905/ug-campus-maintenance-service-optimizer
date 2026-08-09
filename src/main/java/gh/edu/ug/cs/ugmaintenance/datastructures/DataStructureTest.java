package gh.edu.ug.cs.ugmaintenance.datastructures;

import gh.edu.ug.cs.ugmaintenance.datastructures.stack.Stack;
import gh.edu.ug.cs.ugmaintenance.datastructures.queue.Queue;
import gh.edu.ug.cs.ugmaintenance.datastructures.queue.CircularQueue;
import gh.edu.ug.cs.ugmaintenance.datastructures.deque.Deque;



public class DataStructureTest {
    public static void main(String[] args) {
        testStack();
        testQueue();
        testCircularQueue();
        testDeque();


        System.out.println("All tests passed!");
    }

    // ---------- Stack Tests ----------
    public static void testStack(){
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("After pushing  3 elements"); stack.display(); // should print 3 2 1
        System.out.println("Top element: " + stack.peek()); 
        System.out.println("Popped element: " + stack.pop());
        System.out.println("Stack after popping"); stack.display(); // should print 2 1
    }



    // ---------- Queue Tests ----------
    public static void testQueue() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(3);
        queue.enqueue(4);
        System.out.println("After enqueueing 3 elements");
        queue.display(); // should print 1 2 3
        System.out.println("Front element: " + queue.peek());
        System.out.println("Dequeued element: " + queue.dequeue());
        System.out.println("Queue after dequeueing");
        queue.display(); // should print 2 3
        System.out.println();
    }

    // ---------- CircularQueue Tests ----------
    public static void testCircularQueue() {
        CircularQueue<Integer> cq = new CircularQueue<>(3);
        cq.enqueue(1);
        cq.enqueue(2);
        cq.enqueue(13);
        System.out.println("After enqueueing 3 elements into circular queue");
        cq.display(); // should print 1 2 13
        System.out.println("Dequeued element: " + cq.dequeue());
        System.out.println("Circular queue after dequeueing");
        cq.display(); // should print 2 3
        System.out.println();
    }

    // ---------- Deque Tests ----------
    public static void testDeque() {
        Deque<Integer> deque = new Deque<>();
        deque.addFront(1);
        deque.addRear(12);
        deque.addFront(5);
        System.out.println("After adding elements to deque");
        deque.display(); // should print 5 1 12
        System.out.println("Removed front element: " + deque.removeFront());
        System.out.println("Removed rear element: " + deque.removeRear());
        System.out.println("Deque after removals");
        deque.display(); // should print 1
        System.out.println();
    }
}