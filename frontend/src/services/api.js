import { users, requests, technicians } from "../data/mockData";

// const BASE_URL = 'http://localhost:8080/api'; // ready for future Java backend
const BASE_URL = "http://localhost:8080/api";

const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

export async function loginUser(credentials) {
  await delay(350);

  const user = users.find((item) => item.role === credentials.role);
  if (!user) {
    throw new Error("User not found.");
  }

  return { ...user, role: user.role };
}

export async function getCurrentUser() {
  await delay(200);
  const stored = localStorage.getItem("ugcms-user");
  return stored ? JSON.parse(stored) : null;
}

export async function getRequests() {
  await delay(300);
  return requests;
}

export async function getTechnicians() {
  await delay(300);
  return technicians;
}

export async function getStats() {
  await delay(220);
  const total = requests.length;
  const pending = requests.filter((item) => item.status === "Pending").length;
  const completed = requests.filter(
    (item) => item.status === "Completed",
  ).length;

  return { total, pending, completed };
}

// Create a new request and append to the in-memory mock list
export async function createRequest(requestData) {
  await delay(450);

  // ensure priority is integer
  const priority = Number(requestData.priority) || 1;

  // derive next id from existing requests
  const numericIds = requests
    .map((r) => {
      const m = String(r.id).match(/REQ-(\d+)/);
      return m ? parseInt(m[1], 10) : null;
    })
    .filter(Boolean);

  const next = numericIds.length ? Math.max(...numericIds) + 1 : 1001;
  const newId = `REQ-${next}`;

  const today = new Date().toISOString().split("T")[0];

  const newRequest = {
    id: newId,
    title: requestData.title,
    description: requestData.description,
    location: requestData.location,
    category: requestData.category,
    priority: priority,
    status: "Pending",
    date: today,
    createdBy: requestData.createdBy,
    assignedTechnician: null,
  };

  requests.push(newRequest);
  return newRequest;
}

export async function getUserRequests(userId) {
  await delay(220);
  return requests.filter((r) => r.createdBy === userId);
}

export async function getRequestById(requestId) {
  await delay(180);
  const idStr = String(requestId);
  return requests.find((r) => String(r.id) === idStr) || null;
}

export async function getTechnicianById(techId) {
  await delay(140);
  return technicians.find((t) => t.id === techId) || null;
}

export async function getTechnicianAssignments(technicianId) {
  await delay(220);
  return requests.filter((r) => r.assignedTechnician === technicianId);
}

export async function updateRequestStatus(requestId, newStatus) {
  await delay(180);
  const idStr = String(requestId);
  const r = requests.find((item) => String(item.id) === idStr);
  if (!r) throw new Error("Request not found");
  r.status = newStatus;
  return r;
}

export async function acceptAssignment(requestId, technicianId) {
  await delay(200);
  const idStr = String(requestId);
  const r = requests.find((item) => String(item.id) === idStr);
  if (!r) throw new Error("Request not found");
  r.assignedTechnician = technicianId;
  r.status = "Assigned";
  return r;
}

export async function rejectAssignment(requestId, reason) {
  await delay(200);
  const idStr = String(requestId);
  const r = requests.find((item) => String(item.id) === idStr);
  if (!r) throw new Error("Request not found");
  r.assignedTechnician = null;
  r.status = "Pending";
  r.rejectReason = reason || null;
  return r;
}

export async function getOptimizedRoute(requestId) {
  await delay(260);
  const r = requests.find((item) => String(item.id) === String(requestId));
  if (!r) throw new Error("Request not found");

  // Mock route data
  const route = {
    start: "Main Maintenance Workshop",
    destination: r.location,
    distanceMeters: 850 + (r.priority || 0) * 10,
    distanceKm: ((850 + (r.priority || 0) * 10) / 1000).toFixed(2),
    estimated: {
      walking: "11 minutes",
      driving: "3 minutes",
    },
    steps: [
      "Head North from Workshop",
      "Turn East towards JCT",
      `Follow campus road to ${r.location}`,
      "Arrive at destination",
    ],
  };

  return route;
}

// Admin APIs
export async function getAllRequests(filters = {}) {
  await delay(220);
  let result = [...requests];
  if (filters.status)
    result = result.filter((r) => r.status === filters.status);
  if (filters.category)
    result = result.filter((r) => r.category === filters.category);
  if (filters.priority)
    result = result.filter(
      (r) => Number(r.priority) === Number(filters.priority),
    );
  return result;
}

export async function assignTechnicianToRequest(requestId, technicianId) {
  await delay(200);
  const idStr = String(requestId);
  const r = requests.find((item) => String(item.id) === idStr);
  if (!r) throw new Error("Request not found");
  r.assignedTechnician = technicianId;
  r.status = "Assigned";

  // increment technician assigned count (store locally)
  const tech = technicians.find((t) => t.id === technicianId);
  if (tech) {
    tech.assignedCount = (tech.assignedCount || 0) + 1;
    tech.status = "Busy";
  }

  return r;
}

export async function getPriorityQueue() {
  await delay(160);
  return requests
    .filter((r) => r.status === "Pending")
    .slice()
    .sort((a, b) => Number(b.priority) - Number(a.priority));
}

export async function getOptimizationMetrics() {
  await delay(200);
  const pendingQueue = await getPriorityQueue();
  const avgResponse = 24; // mock minutes
  const shortestRouteSample = { avgMeters: 720, avgTimeMinutes: 9 };

  return {
    averageResponseMinutes: avgResponse,
    pendingPriorityQueueLength: pendingQueue.length,
    shortestRouteSample,
    algorithm: {
      name: "Dijkstra",
      runAt: new Date().toISOString(),
      notes: "Mock results for demo",
    },
  };
}
