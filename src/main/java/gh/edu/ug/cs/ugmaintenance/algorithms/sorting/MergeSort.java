package gh.edu.ug.cs.ugmaintenance.algorithms.sorting;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public class MergeSort {

    public static void sortByUrgency(ServiceRequest[] requests, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sortByUrgency(requests, left, mid);
            sortByUrgency(requests, mid + 1, right);
            mergeByUrgency(requests, left, mid, right);
        }
    }

    private static void mergeByUrgency(ServiceRequest[] requests, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        ServiceRequest[] leftArr = new ServiceRequest[n1];
        ServiceRequest[] rightArr = new ServiceRequest[n2];

        for (int i = 0; i < n1; i++) leftArr[i] = requests[left + i];
        for (int j = 0; j < n2; j++) rightArr[j] = requests[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArr[i].getUrgencyLevel() <= rightArr[j].getUrgencyLevel()) {
                requests[k] = leftArr[i];
                i++;
            } else {
                requests[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) { requests[k] = leftArr[i]; i++; k++; }
        while (j < n2) { requests[k] = rightArr[j]; j++; k++; }
    }

    public static void sortById(ServiceRequest[] requests, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sortById(requests, left, mid);
            sortById(requests, mid + 1, right);
            mergeById(requests, left, mid, right);
        }
    }

    private static void mergeById(ServiceRequest[] requests, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        ServiceRequest[] leftArr = new ServiceRequest[n1];
        ServiceRequest[] rightArr = new ServiceRequest[n2];

        for (int i = 0; i < n1; i++) leftArr[i] = requests[left + i];
        for (int j = 0; j < n2; j++) rightArr[j] = requests[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArr[i].getRequestId() <= rightArr[j].getRequestId()) {
                requests[k] = leftArr[i];
                i++;
            } else {
                requests[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) { requests[k] = leftArr[i]; i++; k++; }
        while (j < n2) { requests[k] = rightArr[j]; j++; k++; }
    }

    public static long timedSortByUrgency(ServiceRequest[] requests, int n) {
        long start = System.nanoTime();
        sortByUrgency(requests, 0, n - 1);
        return System.nanoTime() - start;
    }
}
