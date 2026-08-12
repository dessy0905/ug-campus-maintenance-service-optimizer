package gh.edu.ug.cs.ugmaintenance.datastructures.bst;

import java.time.LocalDateTime;

public class ServiceRequestNode {

    private int requestId;
    private int userId;
    private int locationId;
    private int categoryId;

    private String requestTitle;
    private String description;
    private String urgencyLevel;
    private String status;

    private LocalDateTime requestDate;
    private LocalDateTime completionDate;

    ServiceRequestNode left;
    ServiceRequestNode right;

    public ServiceRequestNode(
            int requestId,
            int userId,
            int locationId,
            int categoryId,
            String requestTitle,
            String description,
            String urgencyLevel,
            String status,
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

        this.left = null;
        this.right = null;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public int getRequestId() {
        return requestId;
    }

    public int getUserId() {
        return userId;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    // =====================================================
    // COPY DATA
    // =====================================================

    /*
     * Used when deleting a node with two children.
     * The in-order successor's data is copied into
     * the node being deleted.
     */
    void copyFrom(ServiceRequestNode other) {

        this.requestId = other.requestId;
        this.userId = other.userId;
        this.locationId = other.locationId;
        this.categoryId = other.categoryId;
        this.requestTitle = other.requestTitle;
        this.description = other.description;
        this.urgencyLevel = other.urgencyLevel;
        this.status = other.status;
        this.requestDate = other.requestDate;
        this.completionDate = other.completionDate;
    }

    // =====================================================
    // TO STRING
    // =====================================================

    @Override
    public String toString() {

        return "ServiceRequest{"
                + "id=" + requestId
                + ", userId=" + userId
                + ", locationId=" + locationId
                + ", categoryId=" + categoryId
                + ", title='" + requestTitle + '\''
                + ", description='" + description + '\''
                + ", urgency='" + urgencyLevel + '\''
                + ", status='" + status + '\''
                + ", requestDate=" + requestDate
                + ", completionDate=" + completionDate
                + '}';
    }
}