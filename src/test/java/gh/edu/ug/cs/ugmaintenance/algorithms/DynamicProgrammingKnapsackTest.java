package gh.edu.ug.cs.ugmaintenance.algorithms;

import gh.edu.ug.cs.ugmaintenance.algorithms.dynamicprogramming.knapsack;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DynamicProgrammingKnapsackTest {
    @Test
    public void solve_returnsMaximumUrgencyValueForRequestList() {
        List<ServiceRequest> requests = List.of(
                new ServiceRequest(3, "critical"),
                new ServiceRequest(4, "high"),
                new ServiceRequest(2, "medium")
        );

        int result = knapsack.solve(requests, 5);

        assertEquals(6, result);
    }

    @Test
    public void solve_returnsZeroWhenNoRequestsFit() {
        List<ServiceRequest> requests = List.of(
                new ServiceRequest(6, "critical"),
                new ServiceRequest(7, "high")
        );

        int result = knapsack.solve(requests, 5);

        assertEquals(0, result);
    }
}
