package gh.edu.ug.cs.ugmaintenance.algorithms;

import gh.edu.ug.cs.ugmaintenance.algorithms.searching.BinarySearch;
import gh.edu.ug.cs.ugmaintenance.algorithms.searching.LinearSearch;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.InsertionSort;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.MergeSort;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.QuickSort;
import gh.edu.ug.cs.ugmaintenance.algorithms.sorting.SelectionSort;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class SearchSortTest {

    private ServiceRequest[] requests;
    private int n;

    @Before
    public void setUp() {
        n = 9;
        requests = new ServiceRequest[n];
        requests[0] = new ServiceRequest(6, 1, 1, 1, "M006", "Electrical fault JQB-19", 2, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 6, 8, 0), null);
        requests[1] = new ServiceRequest(2, 1, 2, 2, "M002", "Plumbing CS-Lab", 1, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 2, 8, 0), null);
        requests[2] = new ServiceRequest(9, 1, 3, 3, "M009", "Internet Balme-Library", 3, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 9, 8, 0), null);
        requests[3] = new ServiceRequest(1, 1, 4, 1, "M001", "Furniture CS-Office", 2, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 1, 8, 0), null);
        requests[4] = new ServiceRequest(4, 1, 5, 2, "M004", "AC Sarbah-Hall", 1, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 4, 8, 0), null);
        requests[5] = new ServiceRequest(8, 1, 6, 4, "M008", "Cleaning Legon-Hall", 4, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 8, 8, 0), null);
        requests[6] = new ServiceRequest(3, 1, 7, 3, "M003", "Electrical UG-Main-Gate", 3, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 3, 8, 0), null);
        requests[7] = new ServiceRequest(7, 1, 8, 1, "M007", "Plumbing N-Block", 2, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 7, 8, 0), null);
        requests[8] = new ServiceRequest(5, 1, 9, 5, "M005", "Internet Night-Market", 5, RequestStatus.PENDING, LocalDateTime.of(2026, 1, 5, 8, 0), null);
    }

    @Test
    public void testLinearSearchFound() {
        int result = LinearSearch.search(requests, requests[7]);
        assertEquals(7, result);
    }

    @Test
    public void testLinearSearchNotFound() {
        ServiceRequest fake = new ServiceRequest(99, 1, 1, 1, "M099", "Fake", 1, RequestStatus.PENDING, LocalDateTime.now(), null);
        int result = LinearSearch.search(requests, fake);
        assertEquals(-1, result);
    }

    @Test
    public void testBinarySearchFound() {
        SelectionSort.sortById(requests, n);
        int result = BinarySearch.searchById(requests, n, 7);
        assertNotEquals(-1, result);
        assertEquals(7, requests[result].getRequestId());
    }

    @Test
    public void testBinarySearchNotFound() {
        SelectionSort.sortById(requests, n);
        int result = BinarySearch.searchById(requests, n, 99);
        assertEquals(-1, result);
    }

    @Test
    public void testSelectionSortByUrgency() {
        SelectionSort.sortByUrgency(requests, n);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getUrgencyLevel() <= requests[i + 1].getUrgencyLevel());
        }
    }

    @Test
    public void testSelectionSortById() {
        SelectionSort.sortById(requests, n);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getRequestId() <= requests[i + 1].getRequestId());
        }
    }

    @Test
    public void testInsertionSortByUrgency() {
        InsertionSort.sortByUrgency(requests, n);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getUrgencyLevel() <= requests[i + 1].getUrgencyLevel());
        }
    }

    @Test
    public void testInsertionSortById() {
        InsertionSort.sortById(requests, n);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getRequestId() <= requests[i + 1].getRequestId());
        }
    }

    @Test
    public void testMergeSortByUrgency() {
        MergeSort.sortByUrgency(requests, 0, n - 1);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getUrgencyLevel() <= requests[i + 1].getUrgencyLevel());
        }
    }

    @Test
    public void testMergeSortById() {
        MergeSort.sortById(requests, 0, n - 1);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getRequestId() <= requests[i + 1].getRequestId());
        }
    }

    @Test
    public void testQuickSortByUrgency() {
        QuickSort.sortByUrgency(requests, 0, n - 1);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getUrgencyLevel() <= requests[i + 1].getUrgencyLevel());
        }
    }

    @Test
    public void testQuickSortById() {
        QuickSort.sortById(requests, 0, n - 1);
        for (int i = 0; i < n - 1; i++) {
            assertTrue(requests[i].getRequestId() <= requests[i + 1].getRequestId());
        }
    }

    @Test
    public void testSingleElement() {
        ServiceRequest[] single = {requests[0]};
        SelectionSort.sortByUrgency(single, 1);
        assertEquals(1, single.length);
    }

    @Test
    public void testAlreadySorted() {
        InsertionSort.sortByUrgency(requests, n);
        ServiceRequest[] copy = new ServiceRequest[n];
        for (int i = 0; i < n; i++) copy[i] = requests[i];
        InsertionSort.sortByUrgency(copy, n);
        for (int i = 0; i < n; i++) {
            assertEquals(requests[i].getRequestId(), copy[i].getRequestId());
        }
    }
}
