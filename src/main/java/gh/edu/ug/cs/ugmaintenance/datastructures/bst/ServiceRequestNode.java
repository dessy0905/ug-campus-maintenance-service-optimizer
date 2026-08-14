package gh.edu.ug.cs.ugmaintenance.datastructures.bst;

import java.time.LocalDateTime;

/**
 * Node used by the Binary Search Tree.
 *
 * Each node represents one maintenance service request.
 * The request ID is used as the BST key.
 */
public class ServiceRequestNode {

    // =====================================================
    // REQUEST DATA
    // =====================================================

    private int requestId;
    private int userId;
    private int locationId;
    private int technicianId;

    private String issueType;
    private String description;
    private String urgencyLevel;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // =====================================================
    // BST LINKS
    // =====================================================

    private ServiceRequestNode left;
    private ServiceRequestNode right;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ServiceRequestNode(
            int requestId,
            int userId,
            int locationId,
            int technicianId,
            String issueType,
            String description,
            String urgencyLevel,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.requestId = requestId;
        this.userId = userId;
        this.locationId = locationId;
        this.technicianId = technicianId;

        this.issueType = issueType;
        this.description = description;
        this.urgencyLevel = urgencyLevel;
        this.status = status;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

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

    public int getTechnicianId() {
        return technicianId;
    }

    public String getIssueType() {
        return issueType;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ServiceRequestNode getLeft() {
        return left;
    }

    public ServiceRequestNode getRight() {
        return right;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLeft(ServiceRequestNode left) {
        this.left = left;
    }

    public void setRight(ServiceRequestNode right) {
        this.right = right;
    }

    // =====================================================
    // DISPLAY
    // =====================================================

    @Override
    public String toString() {
        return "ServiceRequestNode{" +
                "requestId=" + requestId +
                ", issueType='" + issueType + '\'' +
                ", description='" + description + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}