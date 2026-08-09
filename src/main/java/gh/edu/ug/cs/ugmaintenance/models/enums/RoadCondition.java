package gh.edu.ug.cs.ugmaintenance.models.enums;

public enum RoadCondition {
    GOOD,
    FAIR,
    POOR,
    CLOSED;

    public String getDbValue() {
        return switch (this) {
            case GOOD -> "Good";
            case FAIR -> "Fair";
            case POOR -> "Poor";
            case CLOSED -> "Closed";
        };
    }

    public static RoadCondition fromDbValue(String dbValue) {
        if (dbValue == null || dbValue.isBlank()) {
            return null;
        }

        return switch (dbValue.trim()) {
            case "Excellent" -> GOOD;
            case "Good" -> GOOD;
            case "Fair" -> FAIR;
            case "Poor" -> POOR;
            case "Closed" -> CLOSED;
            default -> throw new IllegalArgumentException("Unsupported road condition: " + dbValue);
        };
    }
}
