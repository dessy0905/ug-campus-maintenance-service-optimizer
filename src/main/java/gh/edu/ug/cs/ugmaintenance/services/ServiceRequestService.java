package gh.edu.ug.cs.ugmaintenance.services;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.datastructures.queue.PriorityQueue;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;
import gh.edu.ug.cs.ugmaintenance.repositories.ServiceRequestRepository;


import java.time.LocalDateTime;
import java.util.Optional;

public class ServiceRequestService {
        private final ServiceRequestRepository repository;
        private final PriorityQueue<ServiceRequest> priorityQueue;

    public ServiceRequestService() {
        this.repository = new ServiceRequestRepository();
        this.priorityQueue = new PriorityQueue<>();
    }

        private void validateRequest(ServiceRequest request) {
            if (request == null) {
                throw new IllegalArgumentException("Request cannot be null.");
            }

            if (request.getUrgencyLevel() < 1 || request.getUrgencyLevel() > 5) {
                throw new IllegalArgumentException("Priority must be between 1 and 5.");
            }

            if (request.getRequestTitle() == null || request.getRequestTitle().isBlank()) {
                throw new IllegalArgumentException("Request title is required.");
            }
        }

        public boolean createRequest(ServiceRequest request) {
            validateRequest(request);

            request.setStatus(RequestStatus.PENDING);
            request.setRequestDate(LocalDateTime.now());

            priorityQueue.offer(request);

            return repository.save(request);
        }

        public boolean updateRequest(ServiceRequest request){
            validateRequest(request);
            if(request.getRequestId() <= 0){

                throw new IllegalArgumentException(
                        "Invalid request ID."
                );

            }

            return repository.update(request);

        }

        public boolean deleteRequest(int requestId) {

            if (requestId <= 0) {
                throw new IllegalArgumentException("Invalid request ID.");
            }

            return repository.delete(requestId);
        }

        public List<ServiceRequest> getAllRequests(){

            return repository.findAll();

        }

        public List<ServiceRequest> getPendingRequests() {
            return repository.findPendingRequests();
        }

        public ServiceRequest getNextPriorityRequest() {
            return priorityQueue.poll();
        }

        public List<ServiceRequest> getRequestsByPriority(int priority) {
            return repository.findByPriority(priority);
        }

        public List<ServiceRequest> getRequestsByCategory(int categoryId) {
            return repository.findByCategory(categoryId);
        }

        public List<ServiceRequest> getRequestsByLocation(int locationId) {
            return repository.findByLocation(locationId);
        }

        public List<ServiceRequest> getRequestsByUser(int userId) {
            if (userId <= 0) {
                throw new IllegalArgumentException("Invalid user ID.");
            }

            return repository.findByUserId(userId);
        }

        public boolean updateStatus(int requestId, RequestStatus status) {
            if (requestId <= 0) {
                throw new IllegalArgumentException("Invalid request ID.");
            }
            if (status == null) {
                throw new IllegalArgumentException("Status cannot be null.");
            }

            return repository.updateStatus(requestId, status);
        }

        public Optional<ServiceRequest> getRequestById(int id){
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid request ID.");
            }
            return repository.findById(id);

        }
}
