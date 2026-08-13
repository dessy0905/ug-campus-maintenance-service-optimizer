package gh.edu.ug.cs.ugmaintenance.models.enums;

public enum UserRole {
    STUDENT,
    STAFF,
    LECTURER,
    OFFICER,
    ADMIN;

    public String getDbValue() {
        return switch (this) {
            case STUDENT -> "Student";
            case STAFF -> "Maintenance Officer";
            case LECTURER -> "Lecturer";
            case OFFICER -> "Security Officer";
            case ADMIN -> "Administrator";
        };
    }

    public static UserRole fromDbValue(String dbValue) {
        if (dbValue == null || dbValue.isBlank()) {
            return null;
        }

        return switch (dbValue.trim()) {
            case "Student" -> STUDENT;
            case "Lecturer" -> LECTURER;
            case "Maintenance Officer" -> STAFF;
            case "Security Officer" -> OFFICER;
            case "Administrator" -> ADMIN;
            default -> throw new IllegalArgumentException("Unsupported user role: " + dbValue);
        };
    }
}
