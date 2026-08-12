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
  const [filters, setFilters] = useState({
    status: "All",
    priority: "All",
    category: "All",
  });

  const load = async () => {
    setLoading(true);
    const [all, technicians] = await Promise.all([
      getAllRequests(),
      getTechnicians(),
    ]);
    setRequests(all);
    setTechs(technicians);
    setLoading(false);
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
    const techId = Number(
      prompt("Enter technician ID to assign (choose from list)"),
    );
    if (!techId) return;
    await assignTechnicianToRequest(requestId, techId);
    await load();
  };

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>Requests Management</h2>
      </div>
      <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
        <select
          value={filters.status}
          onChange={(e) => setFilters({ ...filters, status: e.target.value })}
        >
          <option>All</option>
          <option>Pending</option>
          <option>Assigned</option>
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
            <option key={p}>{p}</option>
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
          <option>Carpentry</option>
          <option>Masonry</option>
        </select>
      </div>

      {loading ? (
        <div className="placeholder-box">Loading requests…</div>
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
                  <td>{r.id}</td>
                  <td>{r.title}</td>
                  <td>{r.location}</td>
                  <td>
                    <span
                      className={`status-pill status-priority-${r.priority}`}
                    >
                      {r.priority}
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
                    {r.assignedTechnician
                      ? techs.find((t) => t.id === r.assignedTechnician)
                          ?.name || r.assignedTechnician
                      : "—"}
                  </td>
                  <td>
                    <button onClick={() => onAssign(r.id)}>
                      Assign Technician
                    </button>
                    <button style={{ marginLeft: 8 }}>View Details</button>
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
