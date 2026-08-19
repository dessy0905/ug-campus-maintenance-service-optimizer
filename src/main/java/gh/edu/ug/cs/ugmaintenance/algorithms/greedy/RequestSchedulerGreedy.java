package gh.edu.ug.cs.ugmaintenance.algorithms.greedy;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.LinkedList;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public final class RequestSchedulerGreedy {

    private RequestSchedulerGreedy() {
    }

    /**
     * Greedy strategy:
     * Arrange maintenance requests from highest
     * urgency to lowest urgency.
     *
     * Urgency levels:
     * 5 = highest priority
     * 1 = lowest priority
     *
     * Uses the team's custom LinkedList and DynamicArray.
     */
    public static DynamicArray<ServiceRequest> scheduleByHighestUrgency(
            LinkedList<ServiceRequest> requests) {

        if (requests == null) {
            throw new IllegalArgumentException(
                    "Requests must not be null."
            );
        }

        // Copy the requests from the custom LinkedList
        // into the custom DynamicArray.
        DynamicArray<ServiceRequest> scheduled =
                new DynamicArray<>();

        for (int i = 0; i < requests.size(); i++) {
            scheduled.add(requests.get(i));
        }

        /*
         * Selection Sort
         *
         * At every position, find the request
         * with the highest urgency and place it there.
         */
        for (int i = 0; i < scheduled.size() - 1; i++) {

            int highestUrgencyIndex = i;

            for (int j = i + 1; j < scheduled.size(); j++) {

                if (scheduled.get(j).getUrgencyLevel()
                        > scheduled.get(highestUrgencyIndex)
                        .getUrgencyLevel()) {

                    highestUrgencyIndex = j;
                }
            }

            // Swap the requests
            if (highestUrgencyIndex != i) {

                ServiceRequest temporary =
                        scheduled.get(i);

                scheduled.set(
                        i,
                        scheduled.get(highestUrgencyIndex)
                );

                scheduled.set(
                        highestUrgencyIndex,
                        temporary
                );
            }
        }

        return scheduled;
    }
}