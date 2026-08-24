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
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Bad request";
            int statusCode = 400;
            if (msg.toLowerCase().contains("unauthorized") || msg.toLowerCase().contains("not assigned")) {
                statusCode = 403;
            } else if (msg.toLowerCase().contains("not found")) {
                statusCode = 404;
            }
            HttpUtil.sendError(exchange, statusCode, msg);
        } catch (IllegalStateException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Conflict";
            int statusCode = msg.toLowerCase().contains("not found") ? 404 : 409;
            HttpUtil.sendError(exchange, statusCode, msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            HttpUtil.sendError(exchange, 500, ex.getMessage() != null ? ex.getMessage() : "Internal server error.");
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

        if (("/metadata/locations".equals(apiPath) || "/admin/locations".equals(apiPath)) && "GET".equals(method)) {
            handleLocations(exchange);
            return;
        }

        if (("/metadata/categories".equals(apiPath) || "/admin/categories".equals(apiPath)) && "GET".equals(method)) {
            handleCategories(exchange);
            return;
        }

        if (("/requests".equals(apiPath) || "/admin/service-requests".equals(apiPath)) && "GET".equals(method)) {
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

        if (("/stats".equals(apiPath) || "/admin/stats".equals(apiPath)) && "GET".equals(method)) {
            handleStats(exchange);
            return;
        }

        if (("/technicians".equals(apiPath) || "/admin/technicians".equals(apiPath)) && "GET".equals(method)) {
            handleTechnicians(exchange);
            return;
        }

        if (apiPath.startsWith("/technicians/") && (apiPath.endsWith("/assignments") || apiPath.endsWith("/requests"))
                && "GET".equals(method)) {
            int technicianId = apiPath.endsWith("/requests")
                    ? parseId(apiPath, "/technicians/", "/requests")
                    : parseId(apiPath, "/technicians/", "/assignments");
            handleTechnicianAssignments(exchange, technicianId);
            return;
        }

        if (apiPath.startsWith("/technicians/") && apiPath.contains("/requests/") && apiPath.endsWith("/accept")
                && ("POST".equals(method) || "PUT".equals(method))) {
            String stripped = apiPath.substring("/technicians/".length(), apiPath.length() - "/accept".length());
            String[] parts = stripped.split("/requests/");
            int technicianId = Integer.parseInt(parts[0]);
            int requestId = Integer.parseInt(parts[1]);
            assignmentService.acceptAssignmentByRequest(requestId, technicianId);
            Optional<ServiceRequest> request = requestService.getRequestById(requestId);
            HttpUtil.sendJson(exchange, 200, viewService.toView(request.orElseThrow()));
            return;
        }

        if (apiPath.startsWith("/technicians/") && !apiPath.endsWith("/assignments") && !apiPath.endsWith("/requests")
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

        HttpUtil.sendError(exchange, 404, "Route not found.");
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, Object> body = readMap(exchange);
        String role = String.valueOf(body.get("role"));
        Integer entityId = null;
        if (body.get("technicianId") instanceof Number num) {
            entityId = num.intValue();
        } else if (body.get("userId") instanceof Number num) {
            entityId = num.intValue();
        } else if (body.get("id") instanceof Number num) {
            entityId = num.intValue();
        }
        HttpUtil.sendJson(exchange, 200, authService.login(role, entityId));
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

        List<ServiceRequest> requests = params.containsKey("userId")
                ? requestService.getRequestsByUser(Integer.parseInt(params.get("userId")))
                : requestService.getAllRequests();

        List<Map<String, Object>> mapped = viewService.toViews(requests);
        ArrayList<Map<String, Object>> views = new ArrayList<>();

        for (int i = 0; i < mapped.size(); i++) {
            views.add(mapped.get(i));
        }

        if (params.containsKey("status") && !"All".equalsIgnoreCase(params.get("status"))) {
            views.removeIf(item -> !params.get("status").equalsIgnoreCase(String.valueOf(item.get("status"))));
        }

        if (params.containsKey("category") && !"All".equalsIgnoreCase(params.get("category"))) {
            views.removeIf(item -> !params.get("category").equalsIgnoreCase(String.valueOf(item.get("category"))));
        }

        if (params.containsKey("priority") && !"All".equalsIgnoreCase(params.get("priority"))) {
            int priority = Integer.parseInt(params.get("priority"));
            views.removeIf(item -> item.get("priority") == null || priority != ((Number) item.get("priority")).intValue());
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

        Object createdByObj = body.get("createdBy") != null ? body.get("createdBy") : body.get("userId");
        if (createdByObj == null) {
            HttpUtil.sendError(exchange, 400, "createdBy is required.");
            return;
        }

        int createdBy = parseInt(createdByObj, 1);
        int priority = parsePriority(body.get("priority"));

        Map<String, Object> created = workflowService.createRequestAndAssign(
                String.valueOf(body.get("title")),
                String.valueOf(body.get("description")),
                String.valueOf(body.get("location")),
                String.valueOf(body.get("category")),
                priority,
                createdBy
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
            int technicianId = parseInt(body.get("technicianId"), 0);
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
        int technicianId = parseInt(body.get("technicianId"), 0);
        assignmentService.acceptAssignmentByRequest(requestId, technicianId);

        Optional<ServiceRequest> request = requestService.getRequestById(requestId);
        HttpUtil.sendJson(exchange, 200, viewService.toView(request.orElseThrow()));
    }

    private void handleReject(HttpExchange exchange, int requestId)
            throws IOException {

        Map<String, Object> body = readMap(exchange);
        int technicianId = parseInt(body.get("technicianId"), 0);
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
        int assigned = 0;
        int accepted = 0;
        int inProgress = 0;
        int completed = 0;
        int cancelled = 0;

        List<TechnicianAssignment> allAssignments = assignmentRepository.findAll();
        java.util.Map<Integer, TechnicianAssignment> assignmentMap = new java.util.HashMap<>();
        for (int i = 0; i < allAssignments.size(); i++) {
            TechnicianAssignment a = allAssignments.get(i);
            assignmentMap.put(a.getRequestId(), a);
        }

        for (int i = 0; i < all.size(); i++) {
            ServiceRequest r = all.get(i);
            RequestStatus status = r.getStatus();
            TechnicianAssignment assign = assignmentMap.get(r.getRequestId());
            String assignStatus = assign != null && assign.getAssignmentStatus() != null
                    ? assign.getAssignmentStatus().getDbValue()
                    : null;

            if ("Accepted".equalsIgnoreCase(assignStatus) || status == RequestStatus.ACCEPTED) {
                accepted++;
            } else if (status == RequestStatus.PENDING) {
                pending++;
            } else if (status == RequestStatus.ASSIGNED) {
                assigned++;
            } else if (status == RequestStatus.IN_PROGRESS) {
                inProgress++;
            } else if (status == RequestStatus.COMPLETED) {
                completed++;
            } else if (status == RequestStatus.CANCELLED) {
                cancelled++;
            }
        }

        List<Technician> allTechnicians = technicianService.getAllTechnicians();
        int totalTechnicians = allTechnicians.size();
        int availableTechnicians = 0;
        int busyTechnicians = 0;
        for (int i = 0; i < allTechnicians.size(); i++) {
            if (allTechnicians.get(i).isAvailabilityStatus()) {
                availableTechnicians++;
            } else {
                busyTechnicians++;
            }
        }

        int totalLocations = locationRepository.findAll().size();
        int totalCategories = categoryRepository.findAll().size();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("assigned", assigned);
        stats.put("accepted", accepted);
        stats.put("inProgress", inProgress);
        stats.put("completed", completed);
        stats.put("cancelled", cancelled);
        stats.put("totalTechnicians", totalTechnicians);
        stats.put("availableTechnicians", availableTechnicians);
        stats.put("busyTechnicians", busyTechnicians);
        stats.put("totalLocations", totalLocations);
        stats.put("totalCategories", totalCategories);

        HttpUtil.sendJson(exchange, 200, stats);
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

        if (startLocationId <= 0 || endLocationId <= 0) {
            HttpUtil.sendError(exchange, 400, "Invalid location ID for route calculation.");
            return;
        }

        List<Integer> routeIds = routeService.findShortestRoute(
                startLocationId,
                endLocationId
        );
        List<String> routeNames = routeService.resolveLocationNames(routeIds);
        double distanceKm = routeService.calculateRouteDistance(
                startLocationId,
                endLocationId
        );

        String startName = !routeNames.isEmpty() ? routeNames.get(0) : routeService.getLocationName(startLocationId);
        String endName = !routeNames.isEmpty() ? routeNames.get(routeNames.size() - 1) : routeService.getLocationName(endLocationId);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("start", startName);
        payload.put("destination", endName);
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

    private int parseInt(Object val, int fallback) {
        if (val == null) {
            return fallback;
        }
        if (val instanceof Number n) {
            return n.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(val).trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private int parsePriority(Object val) {
        if (val == null) {
            return 3;
        }
        if (val instanceof Number n) {
            return n.intValue();
        }
        String str = String.valueOf(val).trim();
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return switch (str.toLowerCase()) {
                case "urgent", "critical", "emergency" -> 5;
                case "high" -> 4;
                case "medium" -> 3;
                case "low" -> 2;
                case "very low" -> 1;
                default -> 3;
            };
        }
    }

    private ArrayList<Object> toJavaList(List<?> source) {
        ArrayList<Object> target = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
        return target;
    }
}
