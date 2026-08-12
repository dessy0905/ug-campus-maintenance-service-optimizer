package gh.edu.ug.cs.ugmaintenance.models;
import gh.edu.ug.cs.ugmaintenance.models.enums.RoadCondition;
import java.time.LocalDateTime;

public class Road {
    private int roadId;
    private int fromLocationId;
    private int toLocationId;
    private double distanceKm;
    private int travelTimeMinutes;
    private RoadCondition roadCondition;
    private LocalDateTime createdAt;

    public Road(){
    }

    public Road(int roadId, int fromLocationId, int toLocationId, double distanceKm, int travelTimeMinutes, RoadCondition roadCondition, LocalDateTime createdAt) {
        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.travelTimeMinutes = travelTimeMinutes;
        this.roadCondition = roadCondition;
        this.createdAt=createdAt;
    }

    public int getRoadId(){
        return roadId;
    }

    public void setRoadId(int roadId){
        this.roadId = roadId;
    }

    public int getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(int fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public int getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(int toLocationId) {
        this.toLocationId = toLocationId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getTravelTimeMinutes() {
        return travelTimeMinutes;
    }

    public void setTravelTimeMinutes(int travelTimeMinutes) {
        this.travelTimeMinutes = travelTimeMinutes;
    }

    public RoadCondition getRoadCondition() {
        return roadCondition;
    }

    public void setRoadCondition(RoadCondition roadCondition) {
        this.roadCondition = roadCondition;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Road{" +
                "roadId=" + roadId +
                ", fromLocationId=" + fromLocationId +
                ", toLocationId=" + toLocationId +
                ", distanceKm=" + distanceKm +
                ", travelTimeMinutes=" + travelTimeMinutes +
                ", roadCondition=" + roadCondition +
                '}';
    }
}
