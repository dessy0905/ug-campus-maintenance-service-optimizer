import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { getRequests, getStats } from "../services/api";

function UserDashboard() {
  const { user } = useAuth();
  const [requests, setRequests] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadDashboard() {
      if (!user) {
        return;
      }

      setLoading(true);
      const [allRequests, summary] = await Promise.all([
        getRequests(),
        getStats(),
      ]);
      setRequests(
        allRequests.filter((request) => request.createdBy === user.id),
      );
      setStats(summary);
      setLoading(false);
    }

    loadDashboard();
  }, [user]);

  const requestCounts = useMemo(
    () =>
      requests.reduce(
        (acc, request) => {
          acc[request.status] = (acc[request.status] || 0) + 1;
          return acc;
        },
        {
          Pending: 0,
          Assigned: 0,
          "In Progress": 0,
          Completed: 0,
          Cancelled: 0,
        },
      ),
    [requests],
  );

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <div>
          <h2>Campus User Dashboard</h2>
          <p>
            Track your service requests and follow progress through mock data.
          </p>
        </div>
      </div>

      <div className="dashboard-grid">
        <div className="summary-card">
          <h3>My Requests</h3>
          <p>{requests.length} total requests submitted</p>
        </div>
        <div className="summary-card">
          <h3>Pending</h3>
          <p>{requestCounts.Pending}</p>
        </div>
        <div className="summary-card">
          <h3>In Progress</h3>
          <p>{requestCounts["In Progress"]}</p>
        </div>
        <div className="summary-card">
          <h3>Completed</h3>
          <p>{requestCounts.Completed}</p>
        </div>
      </div>

      {loading ? (
        <div className="placeholder-box">Loading request details…</div>
      ) : requests.length === 0 ? (
        <div className="placeholder-box">
          No requests found for your account yet.
        </div>
      ) : (
        <div className="entity-table-wrapper">
          <table className="entity-table">
            <thead>
              <tr>
                <th>Title</th>
                <th>Location</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((request) => (
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
                  <td>{request.date}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {stats ? (
        <div className="dashboard-note">
          System summary: {stats.total} total requests, {stats.pending} pending,{" "}
          {stats.completed} completed.
        </div>
      ) : null}
    </div>
  );
}

export default UserDashboard;
