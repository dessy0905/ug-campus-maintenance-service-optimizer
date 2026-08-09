package gh.edu.ug.cs.ugmaintenance.algorithms.sorting;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public class SelectionSort {

    public static void sortByUrgency(ServiceRequest[] requests, int n) {
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (requests[j].getUrgencyLevel() < requests[minIndex].getUrgencyLevel()) {
                    minIndex = j;
                } else if (requests[j].getUrgencyLevel() == requests[minIndex].getUrgencyLevel() &&
                           requests[j].getRequestId() < requests[minIndex].getRequestId()) {
                    minIndex = j;
                }
            }
            swap(requests, i, minIndex);
        }
    }

    public static void sortById(ServiceRequest[] requests, int n) {
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (requests[j].getRequestId() < requests[minIndex].getRequestId()) {
                    minIndex = j;
                }
            }
            swap(requests, i, minIndex);
        }
    }

    public static void sortByDate(ServiceRequest[] requests, int n) {
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (requests[j].getRequestDate().isBefore(requests[minIndex].getRequestDate())) {
                    minIndex = j;
                }
            }
            swap(requests, i, minIndex);
        }
    }

    private static void swap(ServiceRequest[] requests, int a, int b) {
        ServiceRequest temp = requests[a];
        requests[a] = requests[b];
        requests[b] = temp;
    }

    public static long timedSortByUrgency(ServiceRequest[] requests, int n) {
        long start = System.nanoTime();
        sortByUrgency(requests, n);
        return System.nanoTime() - start;
    }
}
