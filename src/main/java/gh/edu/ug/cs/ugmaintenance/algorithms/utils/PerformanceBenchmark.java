package gh.edu.ug.cs.ugmaintenance.algorithms.utils;

import gh.edu.ug.cs.ugmaintenance.algorithms.searching.BinarySearch;
import gh.edu.ug.cs.ugmaintenance.algorithms.searching.LinearSearch;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.InsertionSort;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.MergeSort;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.QuickSort;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.SelectionSort;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class PerformanceBenchmark {

    public static ServiceRequest[] generateRequests(int size) {
        ServiceRequest[] requests = new ServiceRequest[size];
        for (int i = 0; i < size; i++) {
            int urgency = (i % 5) + 1;
            requests[i] = new ServiceRequest(
                size - i, 1, 1, 1,
                "REQ-" + (size - i),
                "Request " + i,
                urgency,
                RequestStatus.PENDING,
                LocalDateTime.of(2026, 1, (i % 28) + 1, 8, 0),
                null
            );
        }
        return requests;
    }

    public static ServiceRequest[] copy(ServiceRequest[] original, int n) {
        ServiceRequest[] copy = new ServiceRequest[n];
        for (int i = 0; i < n; i++) copy[i] = original[i];
        return copy;
    }

    public static void main(String[] args) throws IOException {
        int[] sizes = {10, 50, 100, 200, 500};
        FileWriter csv = new FileWriter("performance_member12.csv");
        csv.write("InputSize,Algorithm,TimeNs\n");

        for (int size : sizes) {
            ServiceRequest[] base = generateRequests(size);

            ServiceRequest[] arr1 = copy(base, size);
            long t1 = SelectionSort.timedSortByUrgency(arr1, size);
            csv.write(size + ",SelectionSort," + t1 + "\n");

            ServiceRequest[] arr2 = copy(base, size);
            long t2 = InsertionSort.timedSortByUrgency(arr2, size);
            csv.write(size + ",InsertionSort," + t2 + "\n");

            ServiceRequest[] arr3 = copy(base, size);
            long t3 = MergeSort.timedSortByUrgency(arr3, size);
            csv.write(size + ",MergeSort," + t3 + "\n");

            ServiceRequest[] arr4 = copy(base, size);
            long t4 = QuickSort.timedSortByUrgency(arr4, size);
            csv.write(size + ",QuickSort," + t4 + "\n");

            ServiceRequest[] arr5 = copy(base, size);
            long start = System.nanoTime();
            LinearSearch.search(arr5, arr5[size - 1]);
            long t5 = System.nanoTime() - start;
            csv.write(size + ",LinearSearch," + t5 + "\n");

            ServiceRequest[] arr6 = copy(base, size);
            SelectionSort.sortById(arr6, size);
            long t6 = BinarySearch.timedSearchById(arr6, size, arr6[size - 1].getRequestId());
            csv.write(size + ",BinarySearch," + t6 + "\n");

            System.out.println("Done size=" + size);
        }

        csv.flush();
        csv.close();
        System.out.println("CSV written to performance_member12.csv");
    }
}
