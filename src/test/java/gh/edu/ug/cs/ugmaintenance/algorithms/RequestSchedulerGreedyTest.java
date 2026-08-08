package gh.edu.ug.cs.ugmaintenance.algorithms;

import gh.edu.ug.cs.ugmaintenance.algorithms.greedy.RequestSchedulerGreedy;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RequestSchedulerGreedyTest {
    @Test
    public void scheduleByHighestUrgency_picksHighestUrgencyRequestsFirst() {
        List<ServiceRequest> requests = List.of(
                new ServiceRequest(5, "critical"),
                new ServiceRequest(3, "high"),
                new ServiceRequest(2, "medium")
        );

        List<ServiceRequest> scheduled = RequestSchedulerGreedy.scheduleByHighestUrgency(requests, 5);

        assertEquals(1, scheduled.size());
        assertEquals("critical", scheduled.get(0).getUrgencyLevel());
    }

    @Test
    public void scheduleByUrgencyPerHour_picksBestValuePerHour() {
        List<ServiceRequest> requests = List.of(
                new ServiceRequest(4, "high"),
                new ServiceRequest(2, "medium"),
                new ServiceRequest(3, "critical")
        );

        List<ServiceRequest> scheduled = RequestSchedulerGreedy.scheduleByUrgencyPerHour(requests, 5);

        assertEquals(2, scheduled.size());
        assertEquals("critical", scheduled.get(0).getUrgencyLevel());
    }
}
