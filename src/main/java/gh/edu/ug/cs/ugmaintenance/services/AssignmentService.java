package gh.edu.ug.cs.ugmaintenance.services;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.models.TechnicianAssignment;
import gh.edu.ug.cs.ugmaintenance.models.enums.AssignmentStatus;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.repositories.ServiceRequestRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianAssignmentRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class AssignmentService {

    private final ServiceRequestRepository requestRepository;
    private final TechnicianRepository technicianRepository;
    private final TechnicianAssignmentRepository assignmentRepository;
    private final RouteService routeService;

    public AssignmentService() {
        this.requestRepository = new ServiceRequestRepository();
        this.technicianRepository = new TechnicianRepository();
        this.assignmentRepository = new TechnicianAssignmentRepository();
        this.routeService = new RouteService();
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
     * Find the nearest available technician for a request
     * using Dijkstra shortest-path distances on the campus road graph.
     */
    public Optional<Technician> findNearestTechnician(
            ServiceRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Request cannot be null."
            );
        }

        return routeService.findNearestTechnician(
                request.getLocationId(),
                request.getCategoryId()
        );
    }

    public Optional<Technician> findNearestTechnician(int requestId) {

        if (requestId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid request ID."
            );
        }

        Optional<ServiceRequest> request =
                requestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new IllegalArgumentException(
                    "Maintenance request not found."
            );
        }

        return findNearestTechnician(request.get());
    }

    /*
     * Automatically assign the nearest suitable technician
     * to a pending maintenance request.
     */
    public boolean autoAssignNearestTechnician(int requestId) {

        if (requestId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid request ID."
            );
        }

        Optional<ServiceRequest> request =
                requestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new IllegalArgumentException(
                    "Maintenance request not found."
            );
        }

        if (request.get().getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending requests can be auto-assigned."
            );
        }

        Optional<Technician> nearest =
                findNearestTechnician(request.get());

        if (nearest.isEmpty()) {
            return false;
        }

        return assignTechnician(
                requestId,
                nearest.get().getTechnicianId()
        );
    }

    /*
     * Attempt to auto-assign every pending request in the queue.
     * Returns the number of requests successfully assigned.
     */
    public int autoAssignPendingRequests() {

        List<ServiceRequest> pendingRequests =
                requestRepository.findPendingRequests();

        int assignedCount = 0;

        for (int i = 0; i < pendingRequests.size(); i++) {
            ServiceRequest request = pendingRequests.get(i);

            if (autoAssignNearestTechnician(request.getRequestId())) {
                assignedCount++;
            }
        }

        return assignedCount;
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

        if (request.get().getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending requests can be assigned."
            );
        }

        if (assignmentRepository.findByRequestId(requestId).isPresent()) {
            throw new IllegalStateException(
                    "Request already has an active assignment."
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

            requestRepository.updateStatus(
                    requestId,
                    RequestStatus.ASSIGNED
            );

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

                requestRepository.updateStatus(
                        assignment.get().getRequestId(),
                        RequestStatus.COMPLETED
                );

                request.get().setCompletionDate(
                        LocalDateTime.now()
                );

                requestRepository.update(request.get());
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

    public List<ServiceRequest> getAssignedRequests(int technicianId) {
        if (technicianId <= 0) {
            throw new IllegalArgumentException("Invalid technician ID.");
        }

        List<TechnicianAssignment> assignments =
                assignmentRepository.findByTechnicianId(technicianId);

        List<ServiceRequest> requests = new gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray<>();

        for (int i = 0; i < assignments.size(); i++) {
            TechnicianAssignment assignment = assignments.get(i);
            Optional<ServiceRequest> request =
                    requestRepository.findById(assignment.getRequestId());

            if (request.isPresent()) {
                requests.add(request.get());
            }
        }

        return requests;
    }

    public boolean acceptAssignmentByRequest(int requestId, int technicianId) {
        Optional<TechnicianAssignment> assignment =
                assignmentRepository.findByRequestAndTechnician(
                        requestId,
                        technicianId
                );

        if (assignment.isEmpty()) {
            throw new IllegalArgumentException("Assignment not found.");
        }

        return acceptAssignment(assignment.get().getAssignmentId());
    }

    public boolean rejectAssignmentByRequest(
            int requestId,
            int technicianId) {

        Optional<TechnicianAssignment> assignment =
                assignmentRepository.findByRequestAndTechnician(
                        requestId,
                        technicianId
                );

        if (assignment.isEmpty()) {
            throw new IllegalArgumentException("Assignment not found.");
        }

        int assignmentId = assignment.get().getAssignmentId();

        boolean rejected = assignmentRepository.updateStatus(
                assignmentId,
                AssignmentStatus.REJECTED
        );

        if (!rejected) {
            return false;
        }

        Optional<ServiceRequest> request =
                requestRepository.findById(requestId);

        if (request.isPresent()) {
            requestRepository.updateStatus(requestId, RequestStatus.PENDING);
        }

        Optional<Technician> technician =
                technicianRepository.findById(technicianId);

        if (technician.isPresent()) {
            technician.get().setAvailabilityStatus(true);
            technicianRepository.update(technician.get());
        }

        return true;
    }
}