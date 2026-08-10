package gh.edu.ug.cs.ugmaintenance.models;

import java.time.LocalDateTime;

import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

public class RequestStatusLog {
    private int logId;
    private int requestId;
    private RequestStatus oldStatus;
    private RequestStatus newStatus;
    private int updatedBy;
    private String comments;
    private LocalDateTime updatedAt;

    public RequestStatusLog(){
    }

    public RequestStatusLog(int logId,
                            int requestId,
                            RequestStatus oldStatus,
                            RequestStatus newStatus,
                            int updatedBy,
                            String comments,
                            LocalDateTime updatedAt) {
        this.logId = logId;
        this.requestId = requestId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.updatedBy = updatedBy;
        this.comments = comments;
        this.updatedAt = updatedAt;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public RequestStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(RequestStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public RequestStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(RequestStatus newStatus) {
        this.newStatus = newStatus;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "RequestStatusLog{" +
                "logId=" + logId +
                ", requestId=" + requestId +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                ", updatedBy=" + updatedBy +
                ", comments='" + comments + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
