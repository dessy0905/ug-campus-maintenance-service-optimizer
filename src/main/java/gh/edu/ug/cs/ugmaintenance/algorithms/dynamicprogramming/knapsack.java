package gh.edu.ug.cs.ugmaintenance.algorithms.dynamicprogramming;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;

import java.util.List;

public final class knapsack {
    private knapsack() {
    }

    public static int mapUrgencyToScore(String urgencyLevel) {
        if (urgencyLevel == null) {
            throw new IllegalArgumentException("Urgency level must not be null.");
        }

        switch (urgencyLevel.toLowerCase()) {
            case "critical":
                return 4;
            case "high":
                return 3;
            case "medium":
                return 2;
            case "low":
                return 1;
            default:
                throw new IllegalArgumentException("Unknown urgency level: " + urgencyLevel);
        }
    }

    public static int solve(int[] repairHours, int[] urgencyLevel, int availableHours) {
        if (repairHours == null || urgencyLevel == null) {
            throw new IllegalArgumentException("Repair hours and urgency score must not be null.");
        }

        if (repairHours.length != urgencyLevel.length) {
            throw new IllegalArgumentException("Repair hours and urgency score must have the same length.");
        }

        if (availableHours < 0) {
            throw new IllegalArgumentException("Available hours must be non-negative.");
        }

        int[][] dp = new int[repairHours.length + 1][availableHours + 1];

        for (int itemIndex = 1; itemIndex <= repairHours.length; itemIndex++) {
            int weight = repairHours[itemIndex - 1];
            int value = urgencyLevel[itemIndex - 1];

            for (int currentCapacity = 0; currentCapacity <= availableHours; currentCapacity++) {
                dp[itemIndex][currentCapacity] = dp[itemIndex - 1][currentCapacity];

                if (weight <= currentCapacity) {
                    int candidate = dp[itemIndex - 1][currentCapacity - weight] + value;

                    if (candidate > dp[itemIndex][currentCapacity]) {
                        dp[itemIndex][currentCapacity] = candidate;
                    }
                }
            }
        }

        return dp[repairHours.length][availableHours];
    }

    public static int solve(List<ServiceRequest> requests, int availableHours) {
        if (requests == null) {
            throw new IllegalArgumentException("Requests must not be null.");
        }
        if (availableHours < 0) {
            throw new IllegalArgumentException("Available hours must be non-negative.");
        }

        int[] weights = new int[requests.size()];
        int[] values = new int[requests.size()];

        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest request = requests.get(i);
            weights[i] = request.getRepairHours();
            values[i] = request.getUrgencyScore();
        }

        return solve(weights, values, availableHours);
    }
}