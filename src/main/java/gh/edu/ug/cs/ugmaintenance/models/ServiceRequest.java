package gh.edu.ug.cs.ugmaintenance.models;

import java.util.Objects;

public final class ServiceRequest {
    private final int repairHours;
    private final String urgencyLevel;
    private final int urgencyScore;

    public ServiceRequest(int repairHours, String urgencyLevel) {
        if (repairHours <= 0) {
            throw new IllegalArgumentException("Repair hours must be greater than zero.");
        }
        if (urgencyLevel == null) {
            throw new IllegalArgumentException("Urgency level must not be null.");
        }

        this.repairHours = repairHours;
        this.urgencyLevel = urgencyLevel;
        this.urgencyScore = mapUrgencyToScore(urgencyLevel);
    }

    private static int mapUrgencyToScore(String urgencyLevel) {
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

    public int getRepairHours() {
        return repairHours;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public int getUrgencyScore() {
        return urgencyScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServiceRequest)) {
            return false;
        }
        ServiceRequest other = (ServiceRequest) obj;
        return repairHours == other.repairHours
                && urgencyScore == other.urgencyScore
                && Objects.equals(urgencyLevel, other.urgencyLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repairHours, urgencyLevel, urgencyScore);
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "repairHours=" + repairHours +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", urgencyScore=" + urgencyScore +
                '}';
    }
}
