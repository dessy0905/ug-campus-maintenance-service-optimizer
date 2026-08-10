import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { getUserRequests } from "../services/api";
import "../layouts/AppLayout.css";

function MyRequestsPage() {
  const { user } = useAuth();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState("All");
  const [q, setQ] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    async function load() {
      setLoading(true);
      const data = await getUserRequests(user.id);
      setRequests(data);
      setLoading(false);
    }
    load();
  }, [user]);

  const filtered = useMemo(() => {
    return requests.filter((r) => {
      if (statusFilter !== "All" && r.status !== statusFilter) return false;
      if (
        q &&
        !(
          `${r.title}`.toLowerCase().includes(q.toLowerCase()) ||
          String(r.id).toLowerCase().includes(q.toLowerCase())
        )
      )
        return false;
      return true;
    });
  }, [requests, statusFilter, q]);

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>My Requests</h2>
        <p>All requests you have submitted (mock data).</p>
      </div>

      <div style={{ display: "flex", gap: 12, marginBottom: 12 }}>
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option>All</option>
          <option>Pending</option>
          <option>Assigned</option>
          <option>In Progress</option>
          <option>Completed</option>
          <option>Cancelled</option>
        </select>
        <input
          placeholder="Search by title or id"
          value={q}
          onChange={(e) => setQ(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="placeholder-box">Loading your requests…</div>
      ) : filtered.length === 0 ? (
        <div className="placeholder-box">No requests match your filter.</div>
      ) : (
        <div className="entity-table-wrapper">
          <table className="entity-table">
            <thead>
              <tr>
                <th>Request ID</th>
                <th>Title</th>
                <th>Location</th>
                <th>Category</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Date</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filtered.map((r) => (
                <tr key={r.id}>
                  <td>{r.id}</td>
                  <td>{r.title}</td>
                  <td>{r.location}</td>
                  <td>{r.category}</td>
                  <td>
                    <span
                      className={`status-pill status-priority-${r.priority}`}
                    >
                      {r.priority}
                    </span>
                  </td>
                  <td>
                    <span
                      className={`status-pill status-${String(r.status).replace(/\s+/g, "-").toLowerCase()}`}
                    >
                      {r.status}
                    </span>
                  </td>
                  <td>{r.date}</td>
                  <td>
                    <button onClick={() => navigate(`/user/requests/${r.id}`)}>
                      View Details
                    </button>
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

export default MyRequestsPage;
