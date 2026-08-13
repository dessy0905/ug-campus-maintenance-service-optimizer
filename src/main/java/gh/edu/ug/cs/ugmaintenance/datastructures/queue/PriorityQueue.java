package gh.edu.ug.cs.ugmaintenance.datastructures.queue;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.services.PriorityCalculator;

public class PriorityQueue<T>  {

    private final DynamicArray<T> heap;

    public PriorityQueue() {
        this.heap = new DynamicArray<>();
    }

private boolean higherPriority(T first, T second) {

    if (first instanceof ServiceRequest
           && second instanceof ServiceRequest) {

            ServiceRequest firstRequest = (ServiceRequest) first;
            ServiceRequest secondRequest = (ServiceRequest) second;

            double firstScore =
                    PriorityCalculator.calculateScore(firstRequest);

            double secondScore = 
                    PriorityCalculator.calculateScore(secondRequest);
                    
                    
            if (firstScore != secondScore) {
                return firstScore > secondScore;
            } 
            
            return firstRequest.getUrgencyLevel()
                    > secondRequest.getUrgencyLevel();
           }
    
    return ((Comparable<T>) first).compareTo(second) > 0;       
}    
    /**
     * Adds an element to the priority queue.
     * Higher values have higher priority.
     */
    public void offer(T element) {

        if (element == null) {
            throw new IllegalArgumentException(
                    "PriorityQueue does not support null elements"
            );
        }

        heap.add(element);
        siftUp(heap.size() - 1);
    }

    /**
     * Removes and returns the highest-priority element.
     */
    public T poll() {

        if (isEmpty()) {
            throw new RuntimeException(
                    "PriorityQueue is empty — cannot poll"
            );
        }

        T result = heap.get(0);
        int lastIndex = heap.size() - 1;

        // Only one element
        if (lastIndex == 0) {
            heap.remove(0);
            return result;
        }

        // Move the last element to the root
        T lastItem = heap.remove(lastIndex);
        heap.set(0, lastItem);

        // Restore max-heap property
        siftDown(0);

        return result;
    }

    /**
     * Returns the highest-priority element
     * without removing it.
     */
    public T peek() {

        if (isEmpty()) {
            throw new RuntimeException(
                    "PriorityQueue is empty — cannot peek"
            );
        }

        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    /**
     * Moves an element upward until the max-heap
     * property is restored.
     */
    private void siftUp(int index) {

        while (index > 0) {

            int parentIndex = (index - 1) / 2;

            T current = heap.get(index);
            T parent = heap.get(parentIndex);

            // Parent already has higher/equal priority
            if (!higherPriority(current, parent)) {
                break;
            }

            // Swap current and parent
            heap.set(index, parent);
            heap.set(parentIndex, current);

            index = parentIndex;
        }
    }

    /**
     * Moves an element downward until the max-heap
     * property is restored.
     */
    private void siftDown(int index) {

        int size = heap.size();

        while (true) {

            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;

            // Assume current node has highest priority
            int largest = index;

            // Check left child
            if (leftChild < size
                    && higherPriority(heap.get(leftChild), heap.get(largest))) {

                largest = leftChild;
            }

            // Check right child
            if (rightChild < size
                    && higherPriority(heap.get(rightChild), heap.get(largest))) {

                largest = rightChild;
            }

            // Current node is already the largest
            if (largest == index) {
                break;
            }

            // Swap
            T temp = heap.get(index);

            heap.set(index, heap.get(largest));
            heap.set(largest, temp);

            index = largest;
        }
    }
}