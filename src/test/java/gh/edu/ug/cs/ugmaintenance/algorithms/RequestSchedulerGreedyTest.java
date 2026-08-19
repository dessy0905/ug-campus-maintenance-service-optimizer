package gh.edu.ug.cs.ugmaintenance.algorithms;

import gh.edu.ug.cs.ugmaintenance.algorithms.greedy.RequestSchedulerGreedy;
import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.LinkedList;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class RequestSchedulerGreedyTest {

    @Test
    public void scheduleByHighestUrgency_ordersRequestsByUrgency() {

        LinkedList<ServiceRequest> requests =
                new LinkedList<>();

        ServiceRequest request1 = new ServiceRequest(
                1,
                1,
                3,
                1,
                "Leaking pipe in library",
                "Water leakage near the study area",
                4,
                RequestStatus.PENDING,
                LocalDateTime.of(2026, 2, 1, 8, 0),
                null
        );

        ServiceRequest request2 = new ServiceRequest(
                2,
                2,
                6,
                2,
                "Power outage in lecture theatre",
                "Several lights are off in the lecture hall",
                4,
                RequestStatus.ASSIGNED,
                LocalDateTime.of(2026, 2, 2, 9, 30),
                null
        );

        ServiceRequest request3 = new ServiceRequest(
                3,
                7,
                4,
                5,
                "Blocked drain at hall",
                "Drainage is blocked near the dormitory entrance",
                2,
                RequestStatus.IN_PROGRESS,
                LocalDateTime.of(2026, 2, 3, 10, 15),
                null
        );

        ServiceRequest request4 = new ServiceRequest(
                4,
                4,
                8,
                3,
                "Computer lab network issue",
                "Students cannot access the lab network",
                5,
                RequestStatus.COMPLETED,
                LocalDateTime.of(2026, 2, 4, 11, 0),
                LocalDateTime.of(2026, 2, 4, 14, 30)
        );

        ServiceRequest request5 = new ServiceRequest(
                5,
                8,
                2,
                4,
                "Broken desk in faculty office",
                "One office desk is damaged and unstable",
                1,
                RequestStatus.PENDING,
                LocalDateTime.of(2026, 2, 5, 12, 30),
                null
        );

        ServiceRequest request6 = new ServiceRequest(
                6,
                5,
                1,
                6,
                "Security gate malfunction",
                "Main gate access card reader is failing",
                4,
                RequestStatus.CANCELLED,
                LocalDateTime.of(2026, 2, 6, 13, 0),
                null
        );

        // Add requests in the same order as the CSV
        requests.add(request1);
        requests.add(request2);
        requests.add(request3);
        requests.add(request4);
        requests.add(request5);
        requests.add(request6);

        DynamicArray<ServiceRequest> scheduled =
                RequestSchedulerGreedy.scheduleByHighestUrgency(
                        requests
                );

        /*
         * Expected urgency order:
         *
         * 5
         * 4
         * 4
         * 4
         * 2
         * 1
         */

        assertEquals(5, scheduled.get(0).getUrgencyLevel());

        assertEquals(4, scheduled.get(1).getUrgencyLevel());

        assertEquals(4, scheduled.get(2).getUrgencyLevel());

        assertEquals(4, scheduled.get(3).getUrgencyLevel());

        assertEquals(2, scheduled.get(4).getUrgencyLevel());

        assertEquals(1, scheduled.get(5).getUrgencyLevel());

        assertEquals(6, scheduled.size());
    }
}