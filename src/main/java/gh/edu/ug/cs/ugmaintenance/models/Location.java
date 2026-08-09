package gh.edu.ug.cs.ugmaintenance.models;

import java.time.LocalDateTime;

import gh.edu.ug.cs.ugmaintenance.models.enums.LocationType;

public class Location {
    private int locationId;
    private String locationName;
    private LocationType locationType;
    private String description;
    private LocalDateTime createdAt;

    public Location() {
    }

    public Location(int locationId,
                    String locationName,
                    LocationType locationType,
                    String description,
                LocalDateTime createdAt) {

        this.locationId = locationId;
        this.locationName = locationName;
        this.locationType = locationType;
        this.description = description;
        this.createdAt=createdAt;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", locationName='" + locationName + '\'' +
                ", locationType=" + locationType +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
