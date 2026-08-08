package gh.edu.ug.cs.ugmaintenance.models.enums;

public enum AssignmentStatus {
    ASSIGNED,
    ACCEPTED,
    REJECTED,
    COMPLETED;

    public String getDbValue() {
        return switch (this) {
            case ASSIGNED -> "Assigned";
            case ACCEPTED -> "Accepted";
            case REJECTED -> "Rejected";
            case COMPLETED -> "Completed";
        };
    }

    public static AssignmentStatus fromDbValue(String dbValue) {
        if (dbValue == null || dbValue.isBlank()) {
            return null;
        }

        return AssignmentStatus.valueOf(dbValue.trim().toUpperCase());
    }
}
