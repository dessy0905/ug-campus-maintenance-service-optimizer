import { useEffect, useMemo, useState } from "react";
import {
  getAllRequests,
  getTechnicians,
  assignTechnicianToRequest,
} from "../services/api";
import "../layouts/AppLayout.css";

function AdminRequestsPage() {
  const [requests, setRequests] = useState([]);
  const [techs, setTechs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [assignMessage, setAssignMessage] = useState(null);
  const [filters, setFilters] = useState({
    status: "All",
    priority: "All",
    category: "All",
  });

  const load = async () => {
    setLoading(true);
    setError(null);
    try {
      const [all, technicians] = await Promise.all([
        getAllRequests(),
        getTechnicians(),
      ]);
      setRequests(all || []);
      setTechs(technicians || []);
    } catch {
      setError("Unable to load requests. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const filtered = useMemo(() => {
    let res = [...requests];
    if (filters.status !== "All")
      res = res.filter((r) => r.status === filters.status);
    if (filters.priority !== "All")
      res = res.filter((r) => Number(r.priority) === Number(filters.priority));
    if (filters.category !== "All")
      res = res.filter((r) => r.category === filters.category);
    return res;
  }, [requests, filters]);

  const onAssign = async (requestId) => {
    setAssignMessage(null);
    try {
      const res = await assignTechnicianToRequest(requestId);
      if (res && res.assignedTechnician) {
        setAssignMessage({
          type: "success",
          text: `Technician #${res.assignedTechnician} successfully assigned to Request #${requestId}.`,
        });
      }
      await load();
    } catch (err) {
      const msg =
        err.message && err.message.toLowerCase().includes("no technician")
          ? "No available technician is currently available for this category."
          : err.message || "Failed to assign technician.";
      setAssignMessage({ type: "error", text: msg });
    }
  };

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>Requests Management</h2>
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

      {assignMessage && (
        <div
          style={{
            padding: "10px 14px",
            marginBottom: "16px",
            borderRadius: "6px",
            background:
              assignMessage.type === "success"
                ? "rgba(34, 197, 94, 0.15)"
                : "rgba(239, 68, 68, 0.15)",
            border:
              assignMessage.type === "success"
                ? "1px solid #22c55e"
                : "1px solid #ef4444",
            color: assignMessage.type === "success" ? "#16a34a" : "#dc2626",
            fontWeight: 500,
          }}
        >
          {assignMessage.text}
        </div>
      )}

      <div style={{ display: "flex", gap: 8, marginBottom: 12, flexWrap: "wrap" }}>
        <select
          value={filters.status}
          onChange={(e) => setFilters({ ...filters, status: e.target.value })}
        >
          <option>All</option>
          <option>Pending</option>
          <option>Assigned</option>
          <option>Accepted</option>
          <option>In Progress</option>
          <option>Completed</option>
          <option>Cancelled</option>
        </select>
        <select
          value={filters.priority}
          onChange={(e) => setFilters({ ...filters, priority: e.target.value })}
        >
          <option>All</option>
          {[1, 2, 3, 4, 5].map((p) => (
            <option key={p} value={p}>
              Priority {p}
            </option>
          ))}
        </select>
        <select
          value={filters.category}
          onChange={(e) => setFilters({ ...filters, category: e.target.value })}
        >
          <option>All</option>
          <option>Plumbing</option>
          <option>Electrical</option>
          <option>HVAC</option>
          <option>AC services</option>
          <option>Carpentry</option>
          <option>Cleaning</option>
          <option>Security</option>
        </select>
      </div>

      {loading ? (
        <div className="placeholder-box">Loading requests...</div>
      ) : filtered.length === 0 ? (
        <div className="placeholder-box">No service requests found.</div>
      ) : (
        <div className="entity-table-wrapper">
          <table className="entity-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Location</th>
                <th>Priority</th>
                <th>Category</th>
                <th>Status</th>
                <th>Technician</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((r) => (
                <tr key={r.id}>
                  <td>#{r.id}</td>
                  <td>{r.title}</td>
                  <td>{r.location}</td>
                  <td>
                    <span
                      className={`status-pill status-priority-${r.priority}`}
                    >
                      Level {r.priority}
                    </span>
                  </td>
                  <td>{r.category}</td>
                  <td>
                    <span
                      className={`status-pill status-${String(r.status).replace(/\s+/g, "-").toLowerCase()}`}
                    >
                      {r.status}
                    </span>
                  </td>
                  <td>
                    {r.assignedTechnician ? (
                      techs.find((t) => t.id === r.assignedTechnician)?.name ||
                      `Tech #${r.assignedTechnician}`
                    ) : (
                      <span style={{ color: "#94a3b8", fontStyle: "italic" }}>
                        Waiting for technician assignment.
                      </span>
                    )}
                  </td>
                  <td>
                    {!r.assignedTechnician ? (
                      <button onClick={() => onAssign(r.id)}>
                        Auto-Assign Nearest
                      </button>
                    ) : (
                      <span style={{ fontSize: "0.85rem", color: "#16a34a", fontWeight: 500 }}>
                        Assigned
                      </span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default AdminRequestsPage;
