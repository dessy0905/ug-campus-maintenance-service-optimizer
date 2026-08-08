package gh.edu.ug.cs.ugmaintenance.models.enums;

public enum LocationType {
    DEPARTMENT,
    HALL,
    OFFICE,
    LECTURE_HALL,
    LABORATORY,
    LIBRARY,
    WASHROOM;

    public String getDbValue() {
        return switch (this) {
            case DEPARTMENT -> "Department";
            case HALL -> "Hall";
            case OFFICE -> "Office";
            case LECTURE_HALL -> "Lecture Hall";
            case LABORATORY -> "Laboratory";
            case LIBRARY -> "Library";
            case WASHROOM -> "Washroom";
        };
    }

    public static LocationType fromDbValue(String dbValue) {
        if (dbValue == null || dbValue.isBlank()) {
            return null;
        }

        String normalized = dbValue.trim()
                .replace(' ', '_')
                .toUpperCase();

        return LocationType.valueOf(normalized);
    }
}
