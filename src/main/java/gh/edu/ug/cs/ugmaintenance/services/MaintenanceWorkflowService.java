package gh.edu.ug.cs.ugmaintenance.services;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

public class MaintenanceWorkflowService {

    private final ServiceRequestService requestService;
    private final AssignmentService assignmentService;
    private final LookupService lookupService;
    private final RequestViewService viewService;

    public MaintenanceWorkflowService() {
        this.requestService = new ServiceRequestService();
        this.assignmentService = new AssignmentService();
        this.lookupService = new LookupService();
        this.viewService = new RequestViewService();
    }

    public Map<String, Object> createRequestAndAssign(
            String title,
            String description,
            String locationName,
            String categoryName,
            int priority,
            int createdBy) {

        ServiceRequest request = new ServiceRequest();
        request.setUserId(createdBy);
        request.setLocationId(lookupService.resolveLocationId(locationName));
        request.setCategoryId(lookupService.resolveCategoryId(categoryName));
        request.setRequestTitle(title);
        request.setDescription(description);
        request.setUrgencyLevel(priority);

        if (!requestService.createRequest(request)) {
            throw new IllegalStateException("Failed to create maintenance request.");
        }

        int requestId = request.getRequestId();
        CompletableFuture.runAsync(() -> {
            try {
                assignmentService.autoAssignNearestTechnician(requestId);
            } catch (RuntimeException exception) {
                exception.printStackTrace();
            }
        });

        return viewService.toViewWithoutAssignment(
            request,
            locationName,
            categoryName
        );
    }

    public Map<String, Object> autoAssignRequest(int requestId) {
        if (!assignmentService.autoAssignNearestTechnician(requestId)) {
            throw new IllegalStateException(
                    "No suitable technician available for request "
                            + requestId
            );
        }

        return viewService.toView(
                requestService.getRequestById(requestId).orElseThrow()
        );
    }

    public int autoAssignAllPending() {
        return assignmentService.autoAssignPendingRequests();
    }

    public boolean updateRequestStatus(int requestId, String statusName) {
        Optional<ServiceRequest> request = requestService.getRequestById(requestId);
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Maintenance request not found.");
        }

        RequestStatus status = RequestStatus.fromDbValue(statusName);
        if (status == null) {
            throw new IllegalArgumentException("Invalid request status.");
        }

        if (!requestService.updateStatus(requestId, status)) {
            throw new IllegalStateException("Failed to update request status.");
        }

        if (status == RequestStatus.COMPLETED) {
            Optional<ServiceRequest> updated = requestService.getRequestById(requestId);
            if (updated.isPresent()) {
                updated.get().setCompletionDate(java.time.LocalDateTime.now());
                requestService.updateRequest(updated.get());
            }
        }

        return true;
    }
}
