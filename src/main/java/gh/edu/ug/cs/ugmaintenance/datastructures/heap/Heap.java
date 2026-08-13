package gh.edu.ug.cs.ugmaintenance.datastructures.heap;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.services.PriorityCalculator;

public class Heap {

     private DynamicArray<ServiceRequest> heap;

    public Heap (){
        this.heap = new DynamicArray<>();
    }

  private boolean higherPriority(ServiceRequest first, ServiceRequest second) {

    double firstScore = PriorityCalculator.calculateScore(first);
    double secondScore = PriorityCalculator.calculateScore(second);

    if (firstScore > secondScore) {
        return true;
    }

    if (firstScore < secondScore) {
        return false;
    }

    return first.getUrgencyLevel() > second.getUrgencyLevel();
  }

    public void insert(ServiceRequest request) {
        heap.add(request);
        siftUp(heap.size() - 1);
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (higherPriority(heap.get(index), heap.get(parentIndex))) {
                ServiceRequest temp  = heap.get(index); 
                heap.set(index, heap.get(parentIndex));
                heap.set(parentIndex, temp);

                index = parentIndex;
            }  else {
                break;
            }
        }
    }

    public ServiceRequest extractMax() {
        if (heap.isEmpty()) {
            return null;
        }

        ServiceRequest max = heap.get(0);

        ServiceRequest last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty())  {
            heap.set(0, last);
            siftDown(0);
        }

        return max;
    }

    private void siftDown(int index) {
        int size = heap.size();

        while (true) {
            int leftChild = (2 * index) + 1;
            int rightChild = (2 * index) + 2;
            int largest = index;

            if (leftChild < size &&
                higherPriority(heap.get(leftChild),heap.get(largest)))  {
                largest = leftChild;    
            }

            if (rightChild < size &&
                higherPriority(heap.get(rightChild),heap.get(largest)))  {
                largest = rightChild;
            } 

            if (largest != index) {
                ServiceRequest temp = heap.get(index);
                heap.set(index, heap.get(largest));
                heap.set(largest,temp);

                index = largest;
            }  else {
                break;
            } 
        }
    }

    public ServiceRequest peek() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }


}