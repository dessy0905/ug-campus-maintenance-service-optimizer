package gh.edu.ug.cs.ugmaintenance.models;

import java.time.LocalDateTime;

public class Technician {
    private int technicianId;
    private String fullName;
    private String specialization;
    private int categoryId;
    private String phoneNumber;
    private String vehicleAssigned;
    private boolean availabilityStatus;
    private LocalDateTime createdAt;

    public Technician() {
    }

    public Technician(int technicianId,
                      String fullName,
                      String specialization,
                      int categoryId,
                      String phoneNumber,
                      String vehicleAssigned,
                      boolean availabilityStatus,
                      LocalDateTime createdAt) {

        this.technicianId = technicianId;
        this.fullName = fullName;
        this.specialization = specialization;
        this.categoryId = categoryId;
        this.phoneNumber = phoneNumber;
        this.vehicleAssigned = vehicleAssigned;
        this.availabilityStatus = availabilityStatus;
        this.createdAt = createdAt;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicleAssigned() {
        return vehicleAssigned;
    }

    public void setVehicleAssigned(String vehicleAssigned) {
        this.vehicleAssigned = vehicleAssigned;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Technician{" +
                "technicianId=" + technicianId +
                ", fullName='" + fullName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", categoryId=" + categoryId +
                ", availabilityStatus=" + availabilityStatus +
                '}';
    }
}
