package gh.edu.ug.cs.ugmaintenance.algorithms.greedy;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class RequestSchedulerGreedy {
    private RequestSchedulerGreedy() {
    }

    public static List<ServiceRequest> scheduleByHighestUrgency(List<ServiceRequest> requests, int availableHours) {
        validateInputs(requests, availableHours);

        ServiceRequest[] sorted = requests.toArray(new ServiceRequest[0]);
        Arrays.sort(sorted, Comparator.comparingInt(ServiceRequest::getUrgencyScore).reversed());

        List<ServiceRequest> scheduled = new ArrayList<>();
        int remainingHours = availableHours;

        for (ServiceRequest request : sorted) {
            if (request.getRepairHours() <= remainingHours) {
                scheduled.add(request);
                remainingHours -= request.getRepairHours();
            }
        }

        return scheduled;
    }

    public static List<ServiceRequest> scheduleByUrgencyPerHour(List<ServiceRequest> requests, int availableHours) {
        validateInputs(requests, availableHours);

        ServiceRequest[] sorted = requests.toArray(new ServiceRequest[0]);
        Arrays.sort(sorted, Comparator.comparingDouble(RequestSchedulerGreedy::urgencyPerHour).reversed());

        List<ServiceRequest> scheduled = new ArrayList<>();
        int remainingHours = availableHours;

        for (ServiceRequest request : sorted) {
            if (request.getRepairHours() <= remainingHours) {
                scheduled.add(request);
                remainingHours -= request.getRepairHours();
            }
        }

        return scheduled;
    }

    private static void validateInputs(List<ServiceRequest> requests, int availableHours) {
        if (requests == null) {
            throw new IllegalArgumentException("Requests must not be null.");
        }
        if (availableHours < 0) {
            throw new IllegalArgumentException("Available hours must be non-negative.");
        }
    }

    private static double urgencyPerHour(ServiceRequest request) {
        return request.getUrgencyScore() / (double) request.getRepairHours();
    }
}

