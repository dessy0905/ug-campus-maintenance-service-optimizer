package gh.edu.ug.cs.ugmaintenance.services;

import java.time.Duration;
import java.time.LocalDateTime;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public class PriorityCalculator {

    public static double calculateScore(ServiceRequest request) {

        int boundedUrgency = 
                  Math.max(1,
                  Math.min(request.getUrgencyLevel(), 5));
                  
        double waitHours = 
                 Duration.between(
                          request.getRequestDate(),
                          LocalDateTime.now())
                          .toMinutes() / 60.0;
                          
        double maxUrgencyBonus = 
                 (boundedUrgency == 5) ? 5.0 : 0.0;
                 
        return (boundedUrgency * 4.0)
                + (waitHours * 3.0)
                +maxUrgencyBonus;         
    }


    
}

