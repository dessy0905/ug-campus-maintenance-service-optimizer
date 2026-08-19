package gh.edu.ug.cs.ugmaintenance.algorithms.dynamicprogramming;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.LinkedList;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

public final class Knapsack {

    private Knapsack() {
    }

    /**
     * 0/1 Knapsack Dynamic Programming algorithm.
     *
     * Weight of every request = 1
     * Value of every request  = urgency level
     *
     * The capacity represents the maximum number of
     * service requests that can be handled.
     *
     * The algorithm finds the maximum possible total
     * urgency within that capacity.
     *
     * @param requests service requests
     * @param maxRequests maximum number of requests that can be handled
     * @return maximum total urgency
     */
    public static int solve(
            LinkedList<ServiceRequest> requests,
            int maxRequests) {

        validateInputs(requests, maxRequests);

        int numberOfRequests = requests.size();

        /*
         * dp[i][j] means:
         *
         * The maximum total urgency we can obtain
         * using the first i requests when we can select
         * at most j requests.
         */
        int[][] dp = new int[numberOfRequests + 1][maxRequests + 1];

        for (int i = 1; i <= numberOfRequests; i++) {

            ServiceRequest request = requests.get(i - 1);

            int urgency = request.getUrgencyLevel();

            for (int capacity = 0; capacity <= maxRequests; capacity++) {

                /*
                 * Option 1:
                 * Do not select the current request.
                 */
                dp[i][capacity] = dp[i - 1][capacity];

                /*
                 * Option 2:
                 * Select the current request.
                 *
                 * Every request has a weight of 1.
                 */
                if (capacity >= 1) {

                    int include =
                            dp[i - 1][capacity - 1]
                            + urgency;

                    if (include > dp[i][capacity]) {
                        dp[i][capacity] = include;
                    }
                }
            }
        }

        return dp[numberOfRequests][maxRequests];
    }


    /**
     * Returns the actual requests selected by the
     * Dynamic Programming algorithm.
     *
     * This uses the team's custom LinkedList.
     */
    public static LinkedList<ServiceRequest> selectRequests(
            LinkedList<ServiceRequest> requests,
            int maxRequests) {

        validateInputs(requests, maxRequests);

        int numberOfRequests = requests.size();

        int[][] dp = new int[numberOfRequests + 1][maxRequests + 1];

        /*
         * Build the DP table.
         */
        for (int i = 1; i <= numberOfRequests; i++) {

            ServiceRequest request = requests.get(i - 1);

            int urgency = request.getUrgencyLevel();

            for (int capacity = 0; capacity <= maxRequests; capacity++) {

                dp[i][capacity] = dp[i - 1][capacity];

                if (capacity >= 1) {

                    int include =
                            dp[i - 1][capacity - 1]
                            + urgency;

                    if (include > dp[i][capacity]) {
                        dp[i][capacity] = include;
                    }
                }
            }
        }

        /*
         * Backtrack through the DP table to find
         * which requests were selected.
         */
        LinkedList<ServiceRequest> selected =
                new LinkedList<>();

        int capacity = Math.min(maxRequests, numberOfRequests);

        for (int i = numberOfRequests; i >= 1; i--) {

            /*
             * If the value changed from the previous row,
             * the current request was selected.
             */
            if (dp[i][capacity] != dp[i - 1][capacity]) {

                ServiceRequest request = requests.get(i - 1);

                selected.add(0, request);

                capacity--;

                if (capacity == 0) {
                    break;
                }
            }
        }

        return selected;
    }


    /**
     * Calculates the total urgency of a list of requests.
     */
    public static int calculateTotalUrgency(
            LinkedList<ServiceRequest> requests) {

        if (requests == null) {
            throw new IllegalArgumentException(
                    "Requests must not be null."
            );
        }

        int total = 0;

        for (int i = 0; i < requests.size(); i++) {

            ServiceRequest request = requests.get(i);

            if (request == null) {
                throw new IllegalArgumentException(
                        "Request must not be null."
                );
            }

            total += request.getUrgencyLevel();
        }

        return total;
    }


    /**
     * Validates the input.
     */
    private static void validateInputs(
            LinkedList<ServiceRequest> requests,
            int maxRequests) {

        if (requests == null) {
            throw new IllegalArgumentException(
                    "Requests must not be null."
            );
        }

        if (maxRequests < 0) {
            throw new IllegalArgumentException(
                    "Maximum requests must be non-negative."
            );
        }
    }
}