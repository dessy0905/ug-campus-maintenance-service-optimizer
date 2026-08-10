package gh.edu.ug.cs.ugmaintenance.algorithms.searching;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public class BinarySearch {

    public static int searchById(ServiceRequest[] requests, int n, int targetId) {
        int low = 0;
        int high = n - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (requests[mid].getRequestId() == targetId) {
                return mid;
            } else if (requests[mid].getRequestId() < targetId) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static int searchByUrgency(ServiceRequest[] requests, int n, int urgency) {
        int low = 0;
        int high = n - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (requests[mid].getUrgencyLevel() == urgency) {
                return mid;
            } else if (requests[mid].getUrgencyLevel() < urgency) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static long timedSearchById(ServiceRequest[] requests, int n, int targetId) {
        long start = System.nanoTime();
        searchById(requests, n, targetId);
        return System.nanoTime() - start;
    }
}
