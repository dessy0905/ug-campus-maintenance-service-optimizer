package gh.edu.ug.cs.ugmaintenance.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

import gh.edu.ug.cs.ugmaintenance.models.Road;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.repositories.RoadRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;

public class RouteService {

    private final RoadRepository roadRepository;
    private final TechnicianRepository technicianRepository;

    public RouteService() {
        this.roadRepository = new RoadRepository();
        this.technicianRepository = new TechnicianRepository();
    }

    public List<Integer> findShortestRoute(int startLocationId, int endLocationId) {
        validateLocationIds(startLocationId, endLocationId);

        if (startLocationId == endLocationId) {
            List<Integer> path = new ArrayList<>();
            path.add(startLocationId);
            return path;
        }

        Map<Integer, List<Road>> adjacency = buildAdjacencyMap();
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<RouteStep> queue = new PriorityQueue<>(Comparator.comparingDouble(step -> step.distance));

        distances.put(startLocationId, 0.0);
        queue.offer(new RouteStep(startLocationId, 0.0));

        while (!queue.isEmpty()) {
            RouteStep current = queue.poll();
            if (current.distance > distances.getOrDefault(current.locationId, Double.MAX_VALUE)) {
                continue;
            }

            if (current.locationId == endLocationId) {
                break;
            }

            List<Road> outgoing = adjacency.getOrDefault(current.locationId, new ArrayList<>());
            for (Road road : outgoing) {
                int nextLocation = road.getToLocationId();
                double candidateDistance = current.distance + road.getDistanceKm();

                if (candidateDistance < distances.getOrDefault(nextLocation, Double.MAX_VALUE)) {
                    distances.put(nextLocation, candidateDistance);
                    previous.put(nextLocation, current.locationId);
                    queue.offer(new RouteStep(nextLocation, candidateDistance));
                }
            }
        }

        if (!distances.containsKey(endLocationId)) {
            return new ArrayList<>();
        }

        List<Integer> path = new ArrayList<>();
        int current = endLocationId;
        while (current != 0) {
            path.add(0, current);
            if (!previous.containsKey(current)) {
                if (current == startLocationId) {
                    break;
                }
                return new ArrayList<>();
            }
            current = previous.get(current);
        }

        if (path.isEmpty() || path.get(0) != startLocationId) {
            return new ArrayList<>();
        }

        return path;
    }

    public double calculateRouteDistance(int startLocationId, int endLocationId) {
        validateLocationIds(startLocationId, endLocationId);

        if (startLocationId == endLocationId) {
            return 0.0;
        }

        List<Integer> route = findShortestRoute(startLocationId, endLocationId);
        if (route.isEmpty()) {
            return -1.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            int from = route.get(i);
            int to = route.get(i + 1);
            totalDistance += roadRepository.findByFromLocation(from).stream()
                    .filter(road -> road.getToLocationId() == to)
                    .mapToDouble(Road::getDistanceKm)
                    .findFirst()
                    .orElse(0.0);
        }

        return totalDistance == 0.0 && route.size() > 1 ? -1.0 : totalDistance;
    }

    public Optional<Technician> findNearestTechnician(int locationId) {
        return findNearestTechnician(locationId, -1);
    }

    public Optional<Technician> findNearestTechnician(int locationId, int categoryId) {
        validateLocationId(locationId);

        List<Technician> availableTechnicians;
        if (categoryId > 0) {
            availableTechnicians = technicianRepository.findAvailableByCategory(categoryId);
        } else {
            availableTechnicians = technicianRepository.findAvailableTechnicians();
        }

        if (availableTechnicians.isEmpty()) {
            return Optional.empty();
        }

        // This is the best current implementation without a technician home-location field.
        // Once a technician location or base station is stored, the route distance can be
        // used to rank candidates exactly using the graph above.
        return Optional.of(availableTechnicians.get(0));
    }

    private Map<Integer, List<Road>> buildAdjacencyMap() {
        Map<Integer, List<Road>> adjacency = new HashMap<>();

        for (Road road : roadRepository.findAll()) {
            adjacency.computeIfAbsent(road.getFromLocationId(), key -> new ArrayList<>()).add(road);
        }

        return adjacency;
    }

    private void validateLocationIds(int startLocationId, int endLocationId) {
        validateLocationId(startLocationId);
        validateLocationId(endLocationId);
    }

    private void validateLocationId(int locationId) {
        if (locationId <= 0) {
            throw new IllegalArgumentException("Location ID must be greater than zero.");
        }
    }

    private static class RouteStep {
        private final int locationId;
        private final double distance;

        private RouteStep(int locationId, double distance) {
            this.locationId = locationId;
            this.distance = distance;
        }
    }
}
