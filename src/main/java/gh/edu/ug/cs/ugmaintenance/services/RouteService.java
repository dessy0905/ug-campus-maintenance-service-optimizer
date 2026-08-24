package gh.edu.ug.cs.ugmaintenance.services;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Dijkstra;
import gh.edu.ug.cs.ugmaintenance.datastructures.graph.Graph;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.HashMap;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.models.Location;
import gh.edu.ug.cs.ugmaintenance.models.Road;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.repositories.LocationRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.RoadRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;

public class RouteService {

    private final RoadRepository roadRepository;
    private final TechnicianRepository technicianRepository;
    private final LocationRepository locationRepository;

    private static volatile Graph cachedGraph;
    private static volatile HashMap<Integer, String> cachedLocationNames;
    private static final Object LOCK = new Object();

    public RouteService() {
        this.roadRepository = new RoadRepository();
        this.technicianRepository = new TechnicianRepository();
        this.locationRepository = new LocationRepository();
    }

    public List<Integer> findShortestRoute(int startLocationId, int endLocationId) {
        validateLocationIds(startLocationId, endLocationId);

        if (startLocationId == endLocationId) {
            List<Integer> path = new DynamicArray<>();
            path.add(startLocationId);
            return path;
        }

        Graph graph = getOrCreateGraph();

        if (!graph.containsVertex(startLocationId) || !graph.containsVertex(endLocationId)) {
            return new DynamicArray<>();
        }

        try {
            return new Dijkstra(graph).shortestPath(startLocationId, endLocationId);
        } catch (IllegalArgumentException e) {
            return new DynamicArray<>();
        }
    }

    public List<String> findShortestRouteNames(
            int startLocationId,
            int endLocationId) {

        return resolveLocationNames(
                findShortestRoute(startLocationId, endLocationId)
        );
    }

    public String getLocationName(int locationId) {
        validateLocationId(locationId);

        HashMap<Integer, String> names = getLocationNameIndex();
        String name = names.get(locationId);
        if (name != null) {
            return name;
        }

        return locationRepository.findById(locationId)
                .map(location -> location.getLocationName())
                .orElse(fallbackLocationName(locationId));
    }

    public List<String> resolveLocationNames(List<Integer> locationIds) {
        if (locationIds == null || locationIds.isEmpty()) {
            return new DynamicArray<>();
        }

        HashMap<Integer, String> locationNames = getLocationNameIndex();
        List<String> names = new DynamicArray<>();

        for (int i = 0; i < locationIds.size(); i++) {
            int locationId = locationIds.get(i);
            String name = locationNames.get(locationId);
            names.add(name != null ? name : fallbackLocationName(locationId));
        }

        return names;
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

        Graph graph = getOrCreateGraph();
        double totalDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            int from = route.get(i);
            int to = route.get(i + 1);
            try {
                totalDistance += graph.getEdgeWeight(from, to);
            } catch (Exception e) {
                // fall through
            }
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

        Graph graph = getOrCreateGraph();
        if (!graph.containsVertex(locationId)) {
            return Optional.empty();
        }

        HashMap<Integer, Double> distances = new Dijkstra(graph).shortestDistances(locationId);

        Technician nearestTechnician = null;
        double shortestDistance = Double.MAX_VALUE;

        for (int i = 0; i < availableTechnicians.size(); i++) {
            Technician technician = availableTechnicians.get(i);
            int technicianLocationId = technician.getLocationId();

            if (technicianLocationId <= 0 || !graph.containsVertex(technicianLocationId)) {
                continue;
            }

            Double distance = distances.get(technicianLocationId);
            if (distance == null || distance == Double.MAX_VALUE) {
                continue;
            }

            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestTechnician = technician;
            }
        }

        return Optional.ofNullable(nearestTechnician);
    }

    private Graph getOrCreateGraph() {
        if (cachedGraph != null) {
            return cachedGraph;
        }
        synchronized (LOCK) {
            if (cachedGraph != null) {
                return cachedGraph;
            }
            cachedGraph = buildGraph();
            return cachedGraph;
        }
    }

    private HashMap<Integer, String> getLocationNameIndex() {
        if (cachedLocationNames != null) {
            return cachedLocationNames;
        }
        synchronized (LOCK) {
            if (cachedLocationNames != null) {
                return cachedLocationNames;
            }
            cachedLocationNames = buildLocationNameIndex();
            return cachedLocationNames;
        }
    }

    private Graph buildGraph() {
        Graph graph = new Graph();
        List<Road> roads = roadRepository.findAll();

        for (int i = 0; i < roads.size(); i++) {
            Road road = roads.get(i);
            if (road == null) {
                continue;
            }

            int fromLocationId = road.getFromLocationId();
            int toLocationId = road.getToLocationId();
            double distanceKm = road.getDistanceKm();

            if (fromLocationId <= 0 || toLocationId <= 0 || distanceKm <= 0) {
                continue;
            }

            graph.addEdge(fromLocationId, toLocationId, distanceKm);
        }

        return graph;
    }

    private HashMap<Integer, String> buildLocationNameIndex() {
        HashMap<Integer, String> locationNames = new HashMap<>();
        List<Location> locations = locationRepository.findAll();

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            locationNames.put(
                    location.getLocationId(),
                    location.getLocationName()
            );
        }

        return locationNames;
    }

    private String fallbackLocationName(int locationId) {
        return "Location #" + locationId;
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

}
