import { useEffect, useMemo, useState } from "react";
import {
  getAdminServiceRequests,
  getAdminTechnicians,
  getAdminLocations,
  getAdminCategories,
  getStats,
} from "../services/api";

function AdminDashboard() {
  const [requests, setRequests] = useState([]);
  const [technicians, setTechnicians] = useState([]);
  const [locations, setLocations] = useState([]);
  const [categories, setCategories] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadAdminDashboard() {
      setLoading(true);
      setError(null);
      try {
        const [
          allRequests,
          allTechnicians,
          allLocations,
          allCategories,
          summaryStats,
        ] = await Promise.all([
          getAdminServiceRequests(),
          getAdminTechnicians(),
          getAdminLocations(),
          getAdminCategories(),
          getStats(),
        ]);

        setRequests(allRequests || []);
        setTechnicians(allTechnicians || []);
        setLocations(allLocations || []);
        setCategories(allCategories || []);
        setStats(summaryStats || null);
      } catch (err) {
        setError(err.message || "Failed to load admin analytics.");
      } finally {
        setLoading(false);
      }
    }

    loadAdminDashboard();
  }, []);

  const requestCounts = useMemo(() => {
    const counts = {
      total: requests.length,
      pending: 0,
      assigned: 0,
      accepted: 0,
      inProgress: 0,
      completed: 0,
      cancelled: 0,
    };

    requests.forEach((r) => {
      const status = r.status || "Pending";
      const assignStatus = r.assignmentStatus || "";

      if (status === "Pending") counts.pending += 1;
      else if (status === "In Progress") counts.inProgress += 1;
      else if (status === "Completed") counts.completed += 1;
      else if (status === "Cancelled") counts.cancelled += 1;
      else if (status === "Accepted" || assignStatus === "Accepted")
        counts.accepted += 1;
      else if (status === "Assigned") counts.assigned += 1;
    });

    return counts;
  }, [requests]);

  const technicianCounts = useMemo(() => {
    let available = 0;
    let busy = 0;

    technicians.forEach((tech) => {
      if (tech.status === "Active") available += 1;
      else busy += 1;
    });

    return {
      total: technicians.length,
      available,
      busy,
    };
  }, [technicians]);

  const categoryCounts = useMemo(
    () =>
      requests.reduce((acc, request) => {
        const cat = request.category || "Uncategorized";
        acc[cat] = (acc[cat] || 0) + 1;
        return acc;
      }, {}),
    [requests],
  );

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <div>
          <h2>Admin Dashboard</h2>
          <p>
            Real-time university maintenance operations, technician capacity,
            campus locations, and service metrics.
          </p>
        </div>
      </div>

      {error && (
        <div
          style={{
            padding: "10px 14px",
            marginBottom: "16px",
            borderRadius: "6px",
            background: "rgba(239, 68, 68, 0.15)",
            border: "1px solid #ef4444",
            color: "#dc2626",
            fontWeight: 500,
          }}
        >
          {error}
        </div>
      )}

      {loading ? (
        <div className="placeholder-box">Loading admin analytics…</div>
      ) : (
        <>
          {/* 1. SERVICE REQUESTS SECTION */}
          <div className="secondary-section">
            <h3>Service Requests Breakdown</h3>
            <div className="dashboard-grid">
              <div className="summary-card">
                <h3>Total Requests</h3>
                <p>{stats?.total ?? requestCounts.total}</p>
              </div>
              <div className="summary-card">
                <h3>Pending</h3>
                <p>{stats?.pending ?? requestCounts.pending}</p>
              </div>
              <div className="summary-card">
                <h3>Assigned</h3>
                <p>{stats?.assigned ?? requestCounts.assigned}</p>
              </div>
              <div className="summary-card">
                <h3>Accepted</h3>
                <p>{stats?.accepted ?? requestCounts.accepted}</p>
              </div>
              <div className="summary-card">
                <h3>In Progress</h3>
                <p>{stats?.inProgress ?? requestCounts.inProgress}</p>
              </div>
              <div className="summary-card">
                <h3>Completed</h3>
                <p>{stats?.completed ?? requestCounts.completed}</p>
              </div>
            </div>
          </div>

          {/* 2. TECHNICIANS SECTION */}
          <div className="secondary-section" style={{ marginTop: "24px" }}>
            <h3>Technicians Capacity & Roster</h3>
            <div className="dashboard-grid" style={{ marginBottom: "16px" }}>
              <div className="summary-card">
                <h3>Total Technicians</h3>
                <p>{stats?.totalTechnicians ?? technicianCounts.total}</p>
              </div>
              <div className="summary-card">
                <h3>Available Technicians</h3>
                <p style={{ color: "#16a34a" }}>
                  {stats?.availableTechnicians ?? technicianCounts.available}
                </p>
              </div>
              <div className="summary-card">
                <h3>Assigned / Unavailable</h3>
                <p style={{ color: "#d97706" }}>
                  {stats?.busyTechnicians ?? technicianCounts.busy}
                </p>
              </div>
            </div>

            <div className="entity-table-wrapper">
              <table className="entity-table">
                <thead>
                  <tr>
                    <th>Technician ID</th>
                    <th>Technician Name</th>
                    <th>Specialization / Category</th>
                    <th>Current Location</th>
                    <th>Phone</th>
                    <th>Availability Status</th>
                  </tr>
                </thead>
                <tbody>
                  {technicians.map((tech) => (
                    <tr key={tech.id}>
                      <td>#{tech.id}</td>
                      <td>
                        <strong>{tech.name}</strong>
                      </td>
                      <td>
                        {tech.specialization} • {tech.category}
                      </td>
                      <td>{tech.location || "Campus Central"}</td>
                      <td>{tech.phone || "—"}</td>
                      <td>
                        <span
                          className={`status-pill status-${tech.status === "Active" ? "completed" : "in-progress"}`}
                        >
                          {tech.status === "Active" ? "Available" : "Assigned (Busy)"}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* 3. CAMPUS LOCATIONS & CATEGORIES SECTION */}
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              gap: "20px",
              marginTop: "24px",
            }}
          >
            <div className="secondary-section">
              <h3>
                Campus Locations (Total: {stats?.totalLocations ?? locations.length})
              </h3>
              <div
                style={{
                  display: "flex",
                  flexWrap: "wrap",
                  gap: "8px",
                  maxHeight: "220px",
                  overflowY: "auto",
                  padding: "8px 0",
                }}
              >
                {locations.map((loc, idx) => (
                  <span
                    key={idx}
                    style={{
                      padding: "6px 12px",
                      background: "#f1f5f9",
                      borderRadius: "6px",
                      fontSize: "0.88rem",
                      fontWeight: 500,
                    }}
                  >
                    📍 {typeof loc === "string" ? loc : loc.name || loc.locationName}
                  </span>
                ))}
              </div>
            </div>

            <div className="secondary-section">
              <h3>
                Service Categories (Total:{" "}
                {stats?.totalCategories ?? categories.length})
              </h3>
              <div className="category-grid">
                {categories.map((cat, idx) => {
                  const catName =
                    typeof cat === "string"
                      ? cat
                      : cat.name || cat.categoryName;
                  const count = categoryCounts[catName] || 0;
                  return (
                    <div key={idx} className="category-card">
                      <strong>{catName}</strong>
                      <p>{count} requests logged</p>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>

          {/* 4. RECENT SERVICE REQUESTS OVERVIEW */}
          <div className="secondary-section" style={{ marginTop: "24px" }}>
            <h3>Recent Maintenance Requests</h3>
            <div className="entity-table-wrapper">
              <table className="entity-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Location</th>
                    <th>Category</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Assigned Tech</th>
                  </tr>
                </thead>
                <tbody>
                  {requests.slice(0, 10).map((request) => (
                    <tr key={request.id}>
                      <td>#{request.id}</td>
                      <td>{request.title}</td>
                      <td>{request.location}</td>
                      <td>{request.category}</td>
                      <td>
                        <span
                          className={`status-pill status-priority-${request.priority}`}
                        >
                          Level {request.priority}
                        </span>
                      </td>
                      <td>
                        <span
                          className={`status-pill status-${String(request.status).replace(/\s+/g, "-").toLowerCase()}`}
                        >
                          {request.status}
                        </span>
                      </td>
                      <td>
                        {request.assignedTechnician
                          ? `Tech #${request.assignedTechnician}`
                          : "Unassigned"}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default AdminDashboard;
