package gh.edu.ug.cs.ugmaintenance.algorithms;

import gh.edu.ug.cs.ugmaintenance.algorithms.dynamicprogramming.Knapsack;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.LinkedList;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class DynamicProgrammingKnapsackTest {

    @Test
    public void solve_returnsMaximumUrgencyValueForRequestList() {

        LinkedList<ServiceRequest> requests =
                new LinkedList<>();

        requests.add(createRequest(
                1,
                "Leaking pipe in library",
                4
        ));

        requests.add(createRequest(
                2,
                "Power outage in lecture theatre",
                4
        ));

        requests.add(createRequest(
                3,
                "Blocked drain at hall",
                2
        ));

        requests.add(createRequest(
                4,
                "Computer lab network issue",
                5
        ));

        requests.add(createRequest(
                5,
                "Broken desk in faculty office",
                1
        ));

        requests.add(createRequest(
                6,
                "Security gate malfunction",
                4
        ));

        /*
         * We can select at most 3 requests.
         *
         * Highest urgencies:
         *
         * 5 + 4 + 4 = 13
         */
        int result = Knapsack.solve(requests, 3);

        assertEquals(13, result);
    }


    @Test
    public void solve_returnsZeroWhenCapacityIsZero() {

        LinkedList<ServiceRequest> requests =
                new LinkedList<>();

        requests.add(createRequest(
                1,
                "Leaking pipe in library",
                4
        ));

        requests.add(createRequest(
                2,
                "Power outage in lecture theatre",
                4
        ));

        int result = Knapsack.solve(requests, 0);

        assertEquals(0, result);
    }


    @Test
    public void solve_selectsHighestUrgencyRequests() {

        LinkedList<ServiceRequest> requests =
                new LinkedList<>();

        requests.add(createRequest(
                1,
                "Low urgency request",
                1
        ));

        requests.add(createRequest(
                2,
                "Critical request",
                5
        ));

        requests.add(createRequest(
                3,
                "High urgency request",
                4
        ));

        /*
         * Capacity = 2
         *
         * Best combination:
         *
         * 5 + 4 = 9
         */
        int result = Knapsack.solve(requests, 2);

        assertEquals(9, result);
    }


    /**
     * Helper method for creating ServiceRequest objects.
     */
    private ServiceRequest createRequest(
            int requestId,
            String title,
            int urgencyLevel) {

        return new ServiceRequest(
                requestId,
                1,
                1,
                1,
                title,
                "Test service request",
                urgencyLevel,
                RequestStatus.PENDING,
                LocalDateTime.of(
                        2026,
                        2,
                        1,
                        8,
                        0
                ),
                null
        );
    }
}