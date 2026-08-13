import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { getRequests, getTechnicians, getStats } from "../services/api";

function TechnicianDashboard() {
  const { user } = useAuth();
  const [assignedRequests, setAssignedRequests] = useState([]);
  const [technicianRoster, setTechnicianRoster] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadTechnicianDashboard() {
      if (!user) {
        return;
      }

      setLoading(true);
      const [allRequests, technicians, summary] = await Promise.all([
        getRequests(),
        getTechnicians(),
        getStats(),
      ]);

      setAssignedRequests(
        allRequests.filter((request) => request.assignedTechnician === user.id),
      );
      setTechnicianRoster(technicians);
      setStats(summary);
      setLoading(false);
    }

    loadTechnicianDashboard();
  }, [user]);

  const statusCounts = useMemo(
    () =>
      assignedRequests.reduce(
        (acc, request) => {
          acc[request.status] = (acc[request.status] || 0) + 1;
          return acc;
        },
        { Assigned: 0, "In Progress": 0, Completed: 0 },
      ),
    [assignedRequests],
  );

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <div>
          <h2>Technician Dashboard</h2>
          <p>
            Manage assignments, inspect active jobs, and view team workload.
          </p>
        </div>
      </div>

      <div className="dashboard-grid">
        <div className="summary-card">
          <h3>My Assignments</h3>
          <p>{assignedRequests.length} active jobs</p>
        </div>
        <div className="summary-card">
          <h3>Assigned</h3>
          <p>{statusCounts.Assigned}</p>
        </div>
        <div className="summary-card">
          <h3>In Progress</h3>
          <p>{statusCounts["In Progress"]}</p>
        </div>
        <div className="summary-card">
          <h3>Completed</h3>
          <p>{statusCounts.Completed}</p>
        </div>
      </div>

      {loading ? (
        <div className="placeholder-box">Loading technician work queue…</div>
      ) : assignedRequests.length === 0 ? (
        <div className="placeholder-box">
          No active assignments currently assigned to you.
        </div>
      ) : (
        <div className="entity-table-wrapper">
          <table className="entity-table">
            <thead>
              <tr>
                <th>Request</th>
                <th>Location</th>
                <th>Category</th>
                <th>Priority</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {assignedRequests.map((request) => (
                <tr key={request.id}>
                  <td>{request.title}</td>
                  <td>{request.location}</td>
                  <td>{request.category}</td>
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
      )}

      {stats ? (
        <div className="dashboard-note">
          Summary across the system: {stats.total} requests, {stats.pending}{" "}
          pending, {stats.completed} completed.
        </div>
      ) : null}

      <div className="secondary-section">
        <h3>Technician Roster</h3>
        <div className="team-grid">
          {technicianRoster.map((tech) => (
            <div key={tech.id} className="team-card">
              <strong>{tech.name}</strong>
              <p>{tech.specialization} Specialist</p>
              <p>{tech.status}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default TechnicianDashboard;
