package gh.edu.ug.cs.ugmaintenance.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;
import gh.edu.ug.cs.ugmaintenance.repositories.ServiceRequestRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;

public class ReportService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final TechnicianRepository technicianRepository;

    public ReportService() {
        this.serviceRequestRepository = new ServiceRequestRepository();
        this.technicianRepository = new TechnicianRepository();
    }

    public List<ServiceRequest> getCompletedRequests() {
        return serviceRequestRepository.findCompletedRequests();
    }

    public List<ServiceRequest> getPendingRequests() {
        return serviceRequestRepository.findPendingRequests();
    }

    public List<ServiceRequest> getRequestsByCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Invalid category ID.");
        }

        return serviceRequestRepository.findByCategory(categoryId);
    }

    public List<ServiceRequest> getRequestsByPriority(int priority) {
        return serviceRequestRepository.findByPriority(priority);
    }

    public Map<String, Integer> getTechnicianStatistics() {
        List<Technician> technicians = technicianRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();

        stats.put("total", technicians.size());
        stats.put("available", 0);
        stats.put("unavailable", 0);

        for (Technician technician : technicians) {
            if (technician.isAvailabilityStatus()) {
                stats.put("available", stats.get("available") + 1);
            } else {
                stats.put("unavailable", stats.get("unavailable") + 1);
            }
        }

        return stats;
    }

    public List<ServiceRequest> getRequestsByStatus(RequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return serviceRequestRepository.findByStatus(status);
    }
}
