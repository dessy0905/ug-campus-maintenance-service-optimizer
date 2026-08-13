package gh.edu.ug.cs.ugmaintenance.services;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;


import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;


public class PriorityCalculatorTest {
    
    @Test
    public void urgencyFourGetsScoreSixten() {
        ServiceRequest request = new ServiceRequest(
            1, 1, 1, 1,
            "Test request",
            "Test request",
            4,
            null,
            LocalDateTime.now(),
            null
        );


        double score = PriorityCalculator.calculateScore(request);
        assertEquals(16.0, score, 0.01);
    }

    @Test
    public void urgencyFiveGetsMaximumBonus()  {
        ServiceRequest request = new ServiceRequest(
            2, 1, 1, 1,
            "Critical request",
            "Test request",
            5,
            null,
            LocalDateTime.now(),
            null
        );

        double score = PriorityCalculator.calculateScore(request);
        assertEquals(25.0, score, 0.01);
    }

    @Test
    public void urgencyBelowOneisClampedToOne() {
        ServiceRequest request = new ServiceRequest(
            3, 1, 1, 1,
            "Invalid low urgency",
            "Test request",
            0,
            null,
            LocalDateTime.now(),
            null
        );


        double score = PriorityCalculator.calculateScore(request);

        assertEquals(4.0, score, 0.01);
    }

    @Test
    public void urgencyAboveFiveisClampedToFive() {
        ServiceRequest request = new ServiceRequest(
            4, 1, 1, 1,
            "Invalid high urgency",
            "Test request",
            6,
            null,
            LocalDateTime.now(),
            null
        );

        double score = PriorityCalculator.calculateScore(request);

        assertEquals(25.0, score, 0.01);
    }
    
}
