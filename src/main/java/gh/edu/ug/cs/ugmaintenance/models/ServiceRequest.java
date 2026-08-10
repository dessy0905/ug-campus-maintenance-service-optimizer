package gh.edu.ug.cs.ugmaintenance.models;

import java.time.LocalDateTime;

import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

public class ServiceRequest {
     private int requestId;
    private int userId;
    private int locationId;
    private int categoryId;
    private String requestTitle;
    private String description;
    private int urgencyLevel;
    private RequestStatus status;
    private LocalDateTime requestDate;
    private LocalDateTime completionDate;

    public ServiceRequest() {
    }

    public ServiceRequest(int requestId,
                          int userId,
                          int locationId,
                          int categoryId,
                          String requestTitle,
                          String description,
                          int urgencyLevel,
                          RequestStatus status,
                          LocalDateTime requestDate,
                          LocalDateTime completionDate) {

        this.requestId = requestId;
        this.userId = userId;
        this.locationId = locationId;
        this.categoryId = categoryId;
        this.requestTitle = requestTitle;
        this.description = description;
        this.urgencyLevel = urgencyLevel;
        this.status = status;
        this.requestDate = requestDate;
        this.completionDate = completionDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(int urgencyLevel) {
        if (urgencyLevel < 1 || urgencyLevel > 5) {
            throw new IllegalArgumentException(
                    "Urgency level must be between 1 and 5."
            );
        }
        this.urgencyLevel = urgencyLevel;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }
    
    @Override
    public String toString() {
        return "ServiceRequest{" +
                "requestId=" + requestId +
                ", requestTitle='" + requestTitle + '\'' +
                ", urgencyLevel=" + urgencyLevel +
                ", status=" + status +
                '}';
    }
}
