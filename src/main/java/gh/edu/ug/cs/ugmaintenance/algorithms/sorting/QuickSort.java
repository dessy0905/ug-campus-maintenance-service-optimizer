package gh.edu.ug.cs.ugmaintenance.algorithms.sorting;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public class QuickSort {

    public static void sortByUrgency(ServiceRequest[] requests, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByUrgency(requests, low, high);
            sortByUrgency(requests, low, pivotIndex - 1);
            sortByUrgency(requests, pivotIndex + 1, high);
        }
    }

    private static int partitionByUrgency(ServiceRequest[] requests, int low, int high) {
        int pivot = requests[high].getUrgencyLevel();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (requests[j].getUrgencyLevel() <= pivot) {
                i++;
                swap(requests, i, j);
            }
        }
        swap(requests, i + 1, high);
        return i + 1;
    }

    public static void sortById(ServiceRequest[] requests, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionById(requests, low, high);
            sortById(requests, low, pivotIndex - 1);
            sortById(requests, pivotIndex + 1, high);
        }
    }

    private static int partitionById(ServiceRequest[] requests, int low, int high) {
        int pivot = requests[high].getRequestId();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (requests[j].getRequestId() <= pivot) {
                i++;
                swap(requests, i, j);
            }
        }
        swap(requests, i + 1, high);
        return i + 1;
    }

    private static void swap(ServiceRequest[] requests, int a, int b) {
        ServiceRequest temp = requests[a];
        requests[a] = requests[b];
        requests[b] = temp;
    }

    public static long timedSortByUrgency(ServiceRequest[] requests, int n) {
        long start = System.nanoTime();
        sortByUrgency(requests, 0, n - 1);
        return System.nanoTime() - start;
    }
}
