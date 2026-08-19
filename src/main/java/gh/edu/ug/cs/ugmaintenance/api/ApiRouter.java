package gh.edu.ug.cs.ugmaintenance.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.models.TechnicianAssignment;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;
import gh.edu.ug.cs.ugmaintenance.repositories.LocationRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.ServiceCategoryRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianAssignmentRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;
import gh.edu.ug.cs.ugmaintenance.services.AssignmentService;
import gh.edu.ug.cs.ugmaintenance.services.AuthService;
import gh.edu.ug.cs.ugmaintenance.services.MaintenanceWorkflowService;
import gh.edu.ug.cs.ugmaintenance.services.RequestViewService;
import gh.edu.ug.cs.ugmaintenance.services.RouteService;
import gh.edu.ug.cs.ugmaintenance.services.ServiceRequestService;
import gh.edu.ug.cs.ugmaintenance.services.TechnicianService;

public class ApiRouter implements HttpHandler {

    private final AuthService authService = new AuthService();
    private final ServiceRequestService requestService = new ServiceRequestService();
    private final AssignmentService assignmentService = new AssignmentService();
    private final MaintenanceWorkflowService workflowService =
            new MaintenanceWorkflowService();
    private final RequestViewService viewService = new RequestViewService();
    private final RouteService routeService = new RouteService();
    private final TechnicianService technicianService = new TechnicianService();
    private final LocationRepository locationRepository = new LocationRepository();
    private final ServiceCategoryRepository categoryRepository =
            new ServiceCategoryRepository();
    private final TechnicianRepository technicianRepository =
            new TechnicianRepository();
    private final TechnicianAssignmentRepository assignmentRepository =
            new TechnicianAssignmentRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpUtil.handleOptions(exchange);
            return;
        }

        try {
            route(exchange);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            HttpUtil.sendError(exchange, 400, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            HttpUtil.sendError(exchange, 500, "Internal server error.");
        }
    }

    private void route(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod().toUpperCase();
        String path = exchange.getRequestURI().getPath();
        String apiPath = path.startsWith("/api")
                ? path.substring(4)
                : path;

        if ("/auth/login".equals(apiPath) && "POST".equals(method)) {
            handleLogin(exchange);
            return;
        }

        if ("/metadata/locations".equals(apiPath) && "GET".equals(method)) {
            handleLocations(exchange);
            return;
        }

        if ("/metadata/categories".equals(apiPath) && "GET".equals(method)) {
            handleCategories(exchange);
            return;
        }

        if ("/requests".equals(apiPath) && "GET".equals(method)) {
            handleGetRequests(exchange);
            return;
        }

        if ("/requests".equals(apiPath) && "POST".equals(method)) {
            handleCreateRequest(exchange);
            return;
        }

        if ("/requests/auto-assign".equals(apiPath) && "POST".equals(method)) {
            int assigned = workflowService.autoAssignAllPending();
            HttpUtil.sendJson(
                    exchange,
                    200,
                    Map.of("assignedCount", assigned)
            );
            return;
        }

        if ("/stats".equals(apiPath) && "GET".equals(method)) {
            handleStats(exchange);
            return;
        }

        if ("/technicians".equals(apiPath) && "GET".equals(method)) {
            handleTechnicians(exchange);
            return;
        }

        if (apiPath.startsWith("/technicians/") && !apiPath.endsWith("/assignments")
                && "GET".equals(method)) {
            int technicianId = Integer.parseInt(apiPath.substring("/technicians/".length()));
            handleTechnician(exchange, technicianId);
            return;
        }

        if (apiPath.startsWith("/requests/") && apiPath.endsWith("/route")
                && "GET".equals(method)) {
            int requestId = parseId(apiPath, "/requests/", "/route");
            handleRoute(exchange, requestId);
            return;
        }

        if (apiPath.startsWith("/requests/") && apiPath.endsWith("/assign")
                && "POST".equals(method)) {
            int requestId = parseId(apiPath, "/requests/", "/assign");
            handleAutoAssign(exchange, requestId);
            return;
        }

        if (apiPath.startsWith("/requests/") && apiPath.endsWith("/status")
                && "PATCH".equals(method)) {
            int requestId = parseId(apiPath, "/requests/", "/status");
            handleUpdateStatus(exchange, requestId);
            return;
        }

        if (apiPath.startsWith("/requests/") && apiPath.endsWith("/accept")
                && "POST".equals(method)) {
            int requestId = parseId(apiPath, "/requests/", "/accept");
            handleAccept(exchange, requestId);
            return;
        }

        if (apiPath.startsWith("/requests/") && apiPath.endsWith("/reject")
                && "POST".equals(method)) {
            int requestId = parseId(apiPath, "/requests/", "/reject");
            handleReject(exchange, requestId);
            return;
        }

        if (apiPath.startsWith("/requests/") && "GET".equals(method)) {
            int requestId = Integer.parseInt(apiPath.substring("/requests/".length()));
            handleGetRequest(exchange, requestId);
            return;
        }

        if (apiPath.startsWith("/technicians/") && apiPath.endsWith("/assignments")
                && "GET".equals(method)) {
            int technicianId = parseId(apiPath, "/technicians/", "/assignments");
            handleTechnicianAssignments(exchange, technicianId);
            return;
        }

        HttpUtil.sendError(exchange, 404, "Route not found.");
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, Object> body = readMap(exchange);
        String role = String.valueOf(body.get("role"));
        HttpUtil.sendJson(exchange, 200, authService.loginByFrontendRole(role));
    }

    private void handleLocations(HttpExchange exchange) throws IOException {
        var locations = locationRepository.findAll();
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            names.add(locations.get(i).getLocationName());
        }

        HttpUtil.sendJson(exchange, 200, names);
    }

    private void handleCategories(HttpExchange exchange) throws IOException {
        var categories = categoryRepository.findAll();
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            names.add(categories.get(i).getCategoryName());
        }

        HttpUtil.sendJson(exchange, 200, names);
    }

    private void handleGetRequests(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQuery(query);

        if (params.containsKey("userId")) {
            int userId = Integer.parseInt(params.get("userId"));
            List<ServiceRequest> requests = requestService.getRequestsByUser(userId);
            HttpUtil.sendJson(exchange, 200, viewService.toViews(requests));
            return;
        }

        List<ServiceRequest> requests = requestService.getAllRequests();
        ArrayList<Map<String, Object>> views = new ArrayList<>();
        List<Map<String, Object>> mapped = viewService.toViews(requests);

        for (int i = 0; i < mapped.size(); i++) {
            views.add(mapped.get(i));
        }

        if (params.containsKey("status") && !"All".equals(params.get("status"))) {
            views.removeIf(item -> !params.get("status").equals(item.get("status")));
        }

        if (params.containsKey("category") && !"All".equals(params.get("category"))) {
            views.removeIf(item -> !params.get("category").equals(item.get("category")));
        }

        if (params.containsKey("priority") && !"All".equals(params.get("priority"))) {
            int priority = Integer.parseInt(params.get("priority"));
            views.removeIf(item -> priority != (int) item.get("priority"));
        }

        HttpUtil.sendJson(exchange, 200, views);
    }

    private void handleCreateRequest(HttpExchange exchange) throws IOException {
        Map<String, Object> body = readMap(exchange);

        requireField(body, "title");
        requireField(body, "description");
        requireField(body, "location");
        requireField(body, "category");
        requireField(body, "priority");
        requireField(body, "createdBy");

        Map<String, Object> created = workflowService.createRequestAndAssign(
                String.valueOf(body.get("title")),
                String.valueOf(body.get("description")),
                String.valueOf(body.get("location")),
                String.valueOf(body.get("category")),
                ((Number) body.get("priority")).intValue(),
                ((Number) body.get("createdBy")).intValue()
        );

        HttpUtil.sendJson(exchange, 201, created);
    }

    private void handleGetRequest(HttpExchange exchange, int requestId)
            throws IOException {

        Optional<ServiceRequest> request = requestService.getRequestById(requestId);
        if (request.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Request not found.");
            return;
        }

        HttpUtil.sendJson(exchange, 200, viewService.toView(request.get()));
    }

    private void handleAutoAssign(HttpExchange exchange, int requestId)
            throws IOException {

        Map<String, Object> body = readMap(exchange);

        if (body.containsKey("technicianId")) {
            int technicianId = ((Number) body.get("technicianId")).intValue();
            assignmentService.assignTechnician(requestId, technicianId);
            Optional<ServiceRequest> request = requestService.getRequestById(requestId);
            HttpUtil.sendJson(exchange, 200, viewService.toView(request.orElseThrow()));
            return;
        }

        HttpUtil.sendJson(
                exchange,
                200,
                workflowService.autoAssignRequest(requestId)
        );
    }

    private void handleUpdateStatus(HttpExchange exchange, int requestId)
            throws IOException {

        Map<String, Object> body = readMap(exchange);
        String status = String.valueOf(body.get("status"));
        workflowService.updateRequestStatus(requestId, status);

        Optional<ServiceRequest> request = requestService.getRequestById(requestId);
        HttpUtil.sendJson(exchange, 200, viewService.toView(request.orElseThrow()));
    }

    private void handleAccept(HttpExchange exchange, int requestId)
            throws IOException {

        Map<String, Object> body = readMap(exchange);
        int technicianId = ((Number) body.get("technicianId")).intValue();
        assignmentService.acceptAssignmentByRequest(requestId, technicianId);

        Optional<ServiceRequest> request = requestService.getRequestById(requestId);
        HttpUtil.sendJson(exchange, 200, viewService.toView(request.orElseThrow()));
    }

    private void handleReject(HttpExchange exchange, int requestId)
            throws IOException {

        Map<String, Object> body = readMap(exchange);
        int technicianId = ((Number) body.get("technicianId")).intValue();
        assignmentService.rejectAssignmentByRequest(requestId, technicianId);

        Optional<ServiceRequest> request = requestService.getRequestById(requestId);
        HttpUtil.sendJson(exchange, 200, viewService.toView(request.orElseThrow()));
    }

    private void handleTechnicianAssignments(
            HttpExchange exchange,
            int technicianId) throws IOException {

        List<ServiceRequest> requests = assignmentService.getAssignedRequests(technicianId);
        HttpUtil.sendJson(exchange, 200, viewService.toViews(requests));
    }

    private void handleTechnicians(HttpExchange exchange) throws IOException {
        List<Technician> technicians = technicianService.getAllTechnicians();
        ArrayList<Map<String, Object>> views = new ArrayList<>();

        for (int i = 0; i < technicians.size(); i++) {
            views.add(viewService.toTechnicianView(technicians.get(i)));
        }

        HttpUtil.sendJson(exchange, 200, views);
    }

    private void handleTechnician(HttpExchange exchange, int technicianId)
            throws IOException {

        Optional<Technician> technician = technicianRepository.findById(technicianId);
        if (technician.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Technician not found.");
            return;
        }

        HttpUtil.sendJson(exchange, 200, viewService.toTechnicianView(technician.get()));
    }

    private void handleStats(HttpExchange exchange) throws IOException {
        List<ServiceRequest> all = requestService.getAllRequests();
        int total = all.size();
        int pending = 0;
        int completed = 0;

        for (int i = 0; i < all.size(); i++) {
            RequestStatus status = all.get(i).getStatus();
            if (status == RequestStatus.PENDING) {
                pending++;
            } else if (status == RequestStatus.COMPLETED) {
                completed++;
            }
        }

        HttpUtil.sendJson(
                exchange,
                200,
                Map.of("total", total, "pending", pending, "completed", completed)
        );
    }

    private void handleRoute(HttpExchange exchange, int requestId)
            throws IOException {

        Optional<ServiceRequest> request = requestService.getRequestById(requestId);
        if (request.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Request not found.");
            return;
        }

        Optional<TechnicianAssignment> assignment =
                assignmentRepository.findByRequestId(requestId);

        if (assignment.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "No assignment found for this request.");
            return;
        }

        Optional<Technician> technician =
                technicianRepository.findById(assignment.get().getTechnicianId());

        if (technician.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Assigned technician not found.");
            return;
        }

        int startLocationId = technician.get().getLocationId();
        int endLocationId = request.get().getLocationId();

        List<Integer> routeIds = routeService.findShortestRoute(
                startLocationId,
                endLocationId
        );
        List<String> routeNames = routeService.findShortestRouteNames(
                startLocationId,
                endLocationId
        );
        double distanceKm = routeService.calculateRouteDistance(
                startLocationId,
                endLocationId
        );

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("start", routeService.getLocationName(startLocationId));
        payload.put("destination", routeService.getLocationName(endLocationId));
        payload.put("distanceKm", distanceKm);
        payload.put("distanceMeters", Math.round(distanceKm * 1000));
        payload.put("steps", toJavaList(routeNames));
        payload.put("routeIds", toJavaList(routeIds));
        payload.put(
                "estimated",
                Map.of(
                        "walking",
                        Math.max(1, (int) Math.ceil(distanceKm * 12)) + " minutes",
                        "driving",
                        Math.max(1, (int) Math.ceil(distanceKm * 3)) + " minutes"
                )
        );

        HttpUtil.sendJson(exchange, 200, payload);
    }

    private Map<String, Object> readMap(HttpExchange exchange) throws IOException {
        String body = HttpUtil.readBody(exchange);
        if (body == null || body.isBlank()) {
            return Map.of();
        }

        return HttpUtil.gson().fromJson(
                body,
                new TypeToken<Map<String, Object>>() {}.getType()
        );
    }

    private void requireField(Map<String, Object> body, String field) {
        Object value = body.get(field);
        if (value == null || (value instanceof String text && text.isBlank())) {
            throw new IllegalArgumentException(field + " is required.");
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> params = new LinkedHashMap<>();
        if (query == null || query.isBlank()) {
            return params;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                params.put(
                        parts[0],
                        URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }

        return params;
    }

    private int parseId(String apiPath, String prefix, String suffix) {
        String middle = apiPath.substring(prefix.length(), apiPath.length() - suffix.length());
        return Integer.parseInt(middle);
    }

    private ArrayList<Object> toJavaList(List<?> source) {
        ArrayList<Object> target = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
        return target;
    }
}
