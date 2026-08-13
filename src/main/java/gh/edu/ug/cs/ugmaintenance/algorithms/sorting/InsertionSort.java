package gh.edu.ug.cs.ugmaintenance.algorithms.sorting;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public class InsertionSort {

    public static void sortByUrgency(ServiceRequest[] requests, int n) {
        for (int i = 1; i < n; i++) {
            ServiceRequest key = requests[i];
            int j = i - 1;
            while (j >= 0 && requests[j].getUrgencyLevel() > key.getUrgencyLevel()) {
                requests[j + 1] = requests[j];
                j--;
            }
            requests[j + 1] = key;
        }
    }

    public static void sortById(ServiceRequest[] requests, int n) {
        for (int i = 1; i < n; i++) {
            ServiceRequest key = requests[i];
            int j = i - 1;
            while (j >= 0 && requests[j].getRequestId() > key.getRequestId()) {
                requests[j + 1] = requests[j];
                j--;
            }
            requests[j + 1] = key;
        }
    }

    public static void sortByDate(ServiceRequest[] requests, int n) {
        for (int i = 1; i < n; i++) {
            ServiceRequest key = requests[i];
            int j = i - 1;
            while (j >= 0 && requests[j].getRequestDate().isAfter(key.getRequestDate())) {
                requests[j + 1] = requests[j];
                j--;
            }
            requests[j + 1] = key;
        }
    }

    public static long timedSortByUrgency(ServiceRequest[] requests, int n) {
        long start = System.nanoTime();
        sortByUrgency(requests, n);
        return System.nanoTime() - start;
    }
}
