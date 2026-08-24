package gh.edu.ug.cs.ugmaintenance.models.enums;

public enum RequestStatus {
    PENDING,
    ASSIGNED,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public String getDbValue() {
        return switch (this) {
            case PENDING -> "Pending";
            case ASSIGNED -> "Assigned";
            case ACCEPTED -> "Accepted";
            case IN_PROGRESS -> "In Progress";
            case COMPLETED -> "Completed";
            case CANCELLED -> "Cancelled";
        };
    }

    public static RequestStatus fromDbValue(String dbValue) {
        if (dbValue == null || dbValue.isBlank()) {
            return null;
        }

        String normalized = dbValue.trim()
                .replace(' ', '_')
                .toUpperCase();

        return RequestStatus.valueOf(normalized);
    }
}
