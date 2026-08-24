package gh.edu.ug.cs.ugmaintenance.services;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.models.TechnicianAssignment;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianAssignmentRepository;

public class RequestViewService {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final LookupService lookupService;
    private final TechnicianAssignmentRepository assignmentRepository;

    public RequestViewService() {
        this.lookupService = new LookupService();
        this.assignmentRepository = new TechnicianAssignmentRepository();
    }

    public Map<String, Object> toView(ServiceRequest request) {
        Map<String, Object> view = new LinkedHashMap<>();

        Optional<TechnicianAssignment> assignment =
                assignmentRepository.findByRequestId(request.getRequestId());

        Integer assignedTechnicianId = assignment
                .map(TechnicianAssignment::getTechnicianId)
                .orElse(null);

        String statusValue = request.getStatus() != null ? request.getStatus().getDbValue() : "Pending";
        String assignmentStatusValue = assignment
                .map(item -> item.getAssignmentStatus().getDbValue())
                .orElse(null);

        if ("Accepted".equalsIgnoreCase(assignmentStatusValue) && "Assigned".equalsIgnoreCase(statusValue)) {
            statusValue = "Accepted";
        }

        view.put("id", request.getRequestId());
        view.put("title", request.getRequestTitle());
        view.put("description", request.getDescription());
        view.put(
                "location",
                lookupService.getLocationName(request.getLocationId())
        );
        view.put(
                "category",
                lookupService.getCategoryName(request.getCategoryId())
        );
        view.put("priority", request.getUrgencyLevel());
        view.put("status", statusValue);
        view.put(
                "date",
                request.getRequestDate() != null
                        ? request.getRequestDate().format(DATE_FORMAT)
                        : null
        );
        view.put("createdBy", request.getUserId());
        view.put("assignedTechnician", assignedTechnicianId);
        view.put(
                "assignmentId",
                assignment.map(TechnicianAssignment::getAssignmentId).orElse(null)
        );
        view.put("assignmentStatus", assignmentStatusValue);

        return view;
    }

        public Map<String, Object> toViewWithoutAssignment(
                        ServiceRequest request,
                        String locationName,
                        String categoryName) {
                Map<String, Object> view = new LinkedHashMap<>();
                view.put("id", request.getRequestId());
                view.put("title", request.getRequestTitle());
                view.put("description", request.getDescription());
                view.put("location", locationName);
                view.put("category", categoryName);
                view.put("priority", request.getUrgencyLevel());
                view.put("status", request.getStatus().getDbValue());
                view.put("date", request.getRequestDate().format(DATE_FORMAT));
                view.put("createdBy", request.getUserId());
                view.put("assignedTechnician", null);
                view.put("assignmentId", null);
                view.put("assignmentStatus", null);
                return view;
        }

    public List<Map<String, Object>> toViews(List<ServiceRequest> requests) {
        List<Map<String, Object>> views = new DynamicArray<>();
        if (requests == null || requests.isEmpty()) {
            return views;
        }

        List<TechnicianAssignment> allAssignments = assignmentRepository.findAll();
        Map<Integer, TechnicianAssignment> assignmentMap = new java.util.HashMap<>();
        for (int i = 0; i < allAssignments.size(); i++) {
            TechnicianAssignment a = allAssignments.get(i);
            assignmentMap.put(a.getRequestId(), a);
        }

        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest req = requests.get(i);
            views.add(toViewWithAssignment(req, assignmentMap.get(req.getRequestId())));
        }

        return views;
    }

    private Map<String, Object> toViewWithAssignment(ServiceRequest request, TechnicianAssignment assignment) {
        Map<String, Object> view = new LinkedHashMap<>();

        Integer assignedTechnicianId = assignment != null ? assignment.getTechnicianId() : null;
        String statusValue = request.getStatus() != null ? request.getStatus().getDbValue() : "Pending";
        String assignmentStatusValue = assignment != null && assignment.getAssignmentStatus() != null
                ? assignment.getAssignmentStatus().getDbValue()
                : null;

        if ("Accepted".equalsIgnoreCase(assignmentStatusValue) && "Assigned".equalsIgnoreCase(statusValue)) {
            statusValue = "Accepted";
        }

        view.put("id", request.getRequestId());
        view.put("title", request.getRequestTitle());
        view.put("description", request.getDescription());
        view.put("location", lookupService.getLocationName(request.getLocationId()));
        view.put("category", lookupService.getCategoryName(request.getCategoryId()));
        view.put("priority", request.getUrgencyLevel());
        view.put("status", statusValue);
        view.put(
                "date",
                request.getRequestDate() != null
                        ? request.getRequestDate().format(DATE_FORMAT)
                        : null
        );
        view.put("createdBy", request.getUserId());
        view.put("assignedTechnician", assignedTechnicianId);
        view.put("assignmentId", assignment != null ? assignment.getAssignmentId() : null);
        view.put("assignmentStatus", assignmentStatusValue);

        return view;
    }

    public Map<String, Object> toTechnicianView(Technician technician) {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", technician.getTechnicianId());
        view.put("name", technician.getFullName());
        view.put("specialization", technician.getSpecialization());
        view.put(
                "category",
                lookupService.getCategoryName(technician.getCategoryId())
        );
        view.put("phone", technician.getPhoneNumber());
        view.put(
                "status",
                technician.isAvailabilityStatus() ? "Active" : "Busy"
        );
        view.put(
                "location",
                lookupService.getLocationName(technician.getLocationId())
        );
        return view;
    }
}
