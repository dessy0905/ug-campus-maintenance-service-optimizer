package gh.edu.ug.cs.ugmaintenance.services;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.models.TechnicianAssignment;
import gh.edu.ug.cs.ugmaintenance.models.enums.AssignmentStatus;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;
import gh.edu.ug.cs.ugmaintenance.repositories.ServiceRequestRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianAssignmentRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AssignmentService {

    private final ServiceRequestRepository requestRepository;
    private final TechnicianRepository technicianRepository;
    private final TechnicianAssignmentRepository assignmentRepository;

    public AssignmentService() {
        this.requestRepository = new ServiceRequestRepository();
        this.technicianRepository = new TechnicianRepository();
        this.assignmentRepository = new TechnicianAssignmentRepository();
    }

    /*
     * Find technicians who are available and belong
     * to the same service category as the request.
     */
    public List<Technician> findSuitableTechnicians(
            ServiceRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Request cannot be null."
            );
        }

        return technicianRepository.findAvailableByCategory(
                request.getCategoryId()
        );
    }

    /*
     * Assign a technician to a maintenance request.
     */
    public boolean assignTechnician(
            int requestId,
            int technicianId) {

        if (requestId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid request ID."
            );
        }

        if (technicianId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid technician ID."
            );
        }

        Optional<ServiceRequest> request =
                requestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new IllegalArgumentException(
                    "Maintenance request not found."
            );
        }

        Optional<Technician> technician =
                technicianRepository.findById(technicianId);

        if (technician.isEmpty()) {
            throw new IllegalArgumentException(
                    "Technician not found."
            );
        }

        if (!technician.get().isAvailabilityStatus()) {
            throw new IllegalStateException(
                    "Technician is not available."
            );
        }

        if (technician.get().getCategoryId()
                != request.get().getCategoryId()) {

            throw new IllegalArgumentException(
                    "Technician does not match the request category."
            );
        }

        TechnicianAssignment assignment =
                new TechnicianAssignment();

        assignment.setRequestId(requestId);
        assignment.setTechnicianId(technicianId);
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setAssignmentStatus(
                AssignmentStatus.ASSIGNED
        );

        boolean assigned =
                assignmentRepository.save(assignment);

        if (assigned) {

            request.get().setStatus(
                    RequestStatus.ASSIGNED
            );

            requestRepository.update(request.get());

            technician.get().setAvailabilityStatus(false);

            technicianRepository.update(technician.get());
        }

        return assigned;
    }

    /*
     * Technician accepts an assignment.
     */
    public boolean acceptAssignment(int assignmentId) {

        if (assignmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID."
            );
        }

        return assignmentRepository.updateStatus(
                assignmentId,
                AssignmentStatus.ACCEPTED
        );
    }

    /*
     * Technician rejects an assignment.
     */
    public boolean rejectAssignment(int assignmentId) {

        if (assignmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID."
            );
        }

        return assignmentRepository.updateStatus(
                assignmentId,
                AssignmentStatus.REJECTED
        );
    }

    /*
     * Technician completes an assignment.
     */
    public boolean completeAssignment(int assignmentId) {

        if (assignmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID."
            );
        }

        Optional<TechnicianAssignment> assignment =
                assignmentRepository.findById(assignmentId);

        if (assignment.isEmpty()) {
            throw new IllegalArgumentException(
                    "Assignment not found."
            );
        }

        boolean completed =
                assignmentRepository.updateStatus(
                        assignmentId,
                        AssignmentStatus.COMPLETED
                );

        if (completed) {

            Optional<ServiceRequest> request =
                    requestRepository.findById(
                            assignment.get().getRequestId()
                    );

            if (request.isPresent()) {

                request.get().setStatus(
                        RequestStatus.COMPLETED
                );

                request.get().setCompletionDate(
                        LocalDateTime.now()
                );

                requestRepository.update(
                        request.get()
                );
            }

            Optional<Technician> technician =
                    technicianRepository.findById(
                            assignment.get().getTechnicianId()
                    );

            if (technician.isPresent()) {

                technician.get()
                        .setAvailabilityStatus(true);

                technicianRepository.update(
                        technician.get()
                );
            }
        }

        return completed;
    }
}