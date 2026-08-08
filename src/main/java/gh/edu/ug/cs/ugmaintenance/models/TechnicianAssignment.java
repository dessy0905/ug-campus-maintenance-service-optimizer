package gh.edu.ug.cs.ugmaintenance.models;

import java.time.LocalDateTime;

import gh.edu.ug.cs.ugmaintenance.models.enums.AssignmentStatus;

public class TechnicianAssignment {
    private int assignmentId;
    private int requestId;
    private int technicianId;
    private LocalDateTime assignedDate;
    private AssignmentStatus assignmentStatus;

    public TechnicianAssignment(){
    }

    public TechnicianAssignment(int assignmentId,
                                int requestId,
                                int technicianId,
                                LocalDateTime assignedDate,
                                AssignmentStatus assignmentStatus) {
        this.assignmentId = assignmentId;
        this.requestId = requestId;
        this.technicianId = technicianId;
        this.assignedDate = assignedDate;
        this.assignmentStatus = assignmentStatus;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public AssignmentStatus getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(AssignmentStatus assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    @Override
    public String toString(){
        return "TechnicianAssignment{" +
                "assignmentId=" + assignmentId +
                ", requestId=" + requestId +
                ", technicianId=" + technicianId +
                ", assignedDate=" + assignedDate +
                ", assignmentStatus=" + assignmentStatus +
                '}';
    }
}
