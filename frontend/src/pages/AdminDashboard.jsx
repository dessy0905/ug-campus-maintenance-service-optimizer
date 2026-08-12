import { useEffect, useMemo, useState } from "react";
import { getRequests, getTechnicians, getStats } from "../services/api";

function AdminDashboard() {
  const [requests, setRequests] = useState([]);
  const [technicians, setTechnicians] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadAdminDashboard() {
      setLoading(true);
      const [allRequests, allTechnicians, summary] = await Promise.all([
        getRequests(),
        getTechnicians(),
        getStats(),
      ]);
      setRequests(allRequests);
      setTechnicians(allTechnicians);
      setStats(summary);
      setLoading(false);
    }

    loadAdminDashboard();
  }, []);

  const topPending = useMemo(
    () =>
      requests.filter((request) => request.status === "Pending").slice(0, 4),
    [requests],
  );

  const categoryCounts = useMemo(
    () =>
      requests.reduce((acc, request) => {
        acc[request.category] = (acc[request.category] || 0) + 1;
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
            Monitor system health, review requests, and oversee technician
            capacity.
          </p>
        </div>
      </div>

      <div className="dashboard-grid">
        <div className="summary-card">
          <h3>Total Requests</h3>
          <p>{stats?.total ?? "—"}</p>
        </div>
        <div className="summary-card">
          <h3>Pending</h3>
          <p>{stats?.pending ?? "—"}</p>
        </div>
        <div className="summary-card">
          <h3>Completed</h3>
          <p>{stats?.completed ?? "—"}</p>
        </div>
        <div className="summary-card">
          <h3>Technicians</h3>
          <p>{technicians.length}</p>
        </div>
      </div>

      {loading ? (
        <div className="placeholder-box">Loading admin analytics…</div>
      ) : (
        <>
          <div className="secondary-section">
            <h3>Top Pending Requests</h3>
            <div className="entity-table-wrapper">
              <table className="entity-table">
                <thead>
                  <tr>
                    <th>Title</th>
                    <th>Location</th>
                    <th>Priority</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {topPending.map((request) => (
                    <tr key={request.id}>
                      <td>{request.title}</td>
                      <td>{request.location}</td>
                      <td>{request.priority}</td>
                      <td>
                        <span
                          className={`status-pill status-${request.status.replace(/\s+/g, "-").toLowerCase()}`}
                        >
                          {request.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="secondary-section">
            <h3>Requests by Category</h3>
            <div className="category-grid">
              {Object.entries(categoryCounts).map(([category, count]) => (
                <div key={category} className="category-card">
                  <strong>{category}</strong>
                  <p>{count} requests</p>
                </div>
              ))}
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default AdminDashboard;
