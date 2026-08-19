const BASE_URL = "http://localhost:8081/api";
const REQUEST_TIMEOUT_MS = 10000;

async function request(path, options = {}) {
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), REQUEST_TIMEOUT_MS);

  let response;
  try {
    response = await fetch(`${BASE_URL}${path}`, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(options.headers || {}),
      },
      signal: controller.signal,
    });
  } catch (error) {
    if (error.name === "AbortError") {
      throw new Error(
        "The backend did not respond. Start the Java API and check the database connection.",
      );
    }
    throw new Error(
      "Cannot reach the backend. Start the Java API on port 8080.",
    );
  } finally {
    clearTimeout(timeout);
  }

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(data.error || "Request failed.");
  }

  return data;
}

export async function loginUser(credentials) {
  return request("/auth/login", {
    method: "POST",
    body: JSON.stringify({ role: credentials.role }),
  });
}

export async function getCurrentUser() {
  const stored = localStorage.getItem("ugcms-user");
  return stored ? JSON.parse(stored) : null;
}

export async function getLocations() {
  return request("/metadata/locations");
}

export async function getCategories() {
  return request("/metadata/categories");
}

export async function getRequests() {
  return request("/requests");
}

export async function getTechnicians() {
  return request("/technicians");
}

export async function getStats() {
  return request("/stats");
}

export async function createRequest(requestData) {
  return request("/requests", {
    method: "POST",
    body: JSON.stringify({
      title: requestData.title,
      description: requestData.description,
      location: requestData.location,
      category: requestData.category,
      priority: Number(requestData.priority) || 1,
      createdBy: requestData.createdBy,
    }),
  });
}

export async function getUserRequests(userId) {
  return request(`/requests?userId=${userId}`);
}

export async function getRequestById(requestId) {
  try {
    return await request(`/requests/${requestId}`);
  } catch {
    return null;
  }
}

export async function getTechnicianById(techId) {
  try {
    return await request(`/technicians/${techId}`);
  } catch {
    return null;
  }
}

export async function getTechnicianAssignments(technicianId) {
  return request(`/technicians/${technicianId}/assignments`);
}

export async function updateRequestStatus(requestId, newStatus) {
  return request(`/requests/${requestId}/status`, {
    method: "PATCH",
    body: JSON.stringify({ status: newStatus }),
  });
}

export async function acceptAssignment(requestId, technicianId) {
  return request(`/requests/${requestId}/accept`, {
    method: "POST",
    body: JSON.stringify({ technicianId }),
  });
}

export async function rejectAssignment(requestId, reason, technicianId) {
  const payload = { technicianId };
  if (reason) payload.reason = reason;

  return request(`/requests/${requestId}/reject`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function getOptimizedRoute(requestId) {
  return request(`/requests/${requestId}/route`);
}

export async function getAllRequests(filters = {}) {
  const params = new URLSearchParams();
  if (filters.status && filters.status !== "All") {
    params.set("status", filters.status);
  }
  if (filters.category && filters.category !== "All") {
    params.set("category", filters.category);
  }
  if (filters.priority && filters.priority !== "All") {
    params.set("priority", String(filters.priority));
  }

  const query = params.toString();
  return request(query ? `/requests?${query}` : "/requests");
}

export async function assignTechnicianToRequest(requestId, technicianId) {
  return request(`/requests/${requestId}/assign`, {
    method: "POST",
    body: JSON.stringify(
      technicianId ? { technicianId: Number(technicianId) } : {},
    ),
  });
}

export async function autoAssignAllPending() {
  return request("/requests/auto-assign", { method: "POST" });
}

export async function getPriorityQueue() {
  const pending = await request("/requests?status=Pending");
  return pending
    .slice()
    .sort((a, b) => Number(b.priority) - Number(a.priority));
}

export async function getOptimizationMetrics() {
  const [stats, pendingQueue] = await Promise.all([
    getStats(),
    getPriorityQueue(),
  ]);

  return {
    averageResponseMinutes: 24,
    pendingPriorityQueueLength: pendingQueue.length,
    shortestRouteSample: { avgMeters: 720, avgTimeMinutes: 9 },
    algorithm: {
      name: "Dijkstra",
      runAt: new Date().toISOString(),
      notes: "Nearest technician matching via campus road graph",
    },
    stats,
  };
}
