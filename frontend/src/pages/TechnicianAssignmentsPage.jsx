import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import {
  getTechnicianAssignments,
  acceptAssignment,
  rejectAssignment,
  updateRequestStatus,
} from "../services/api";
import "../layouts/AppLayout.css";

function TechnicianAssignmentsPage() {
  const { user } = useAuth();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState("All");
  const navigate = useNavigate();

  const load = async () => {
    if (!user || !user.id) return;
    setLoading(true);
    try {
      const data = await getTechnicianAssignments(user.id);
      setRequests(data || []);
    } catch {
      setRequests([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [user]);

  const filtered = useMemo(() => {
    if (tab === "All") return requests;
    if (tab === "Pending Acceptance")
      return requests.filter(
        (r) =>
          (r.status === "Assigned" || r.assignmentStatus === "Assigned") &&
          r.status !== "Accepted" &&
          r.assignmentStatus !== "Accepted",
      );
    if (tab === "Accepted")
      return requests.filter(
        (r) => r.status === "Accepted" || r.assignmentStatus === "Accepted",
      );
    if (tab === "Active")
      return requests.filter((r) => r.status === "In Progress");
    if (tab === "Completed")
      return requests.filter((r) => r.status === "Completed");
    return requests;
  }, [requests, tab]);

  const onAccept = async (id) => {
    await acceptAssignment(id, user.id);
    await load();
  };

  const onReject = async (id) => {
    const reason = prompt("Optional rejection reason");
    await rejectAssignment(id, reason, user.id);
    await load();
  };

  const onStart = async (id) => {
    await updateRequestStatus(id, "In Progress");
    await load();
  };

  const onComplete = async (id) => {
    await updateRequestStatus(id, "Completed");
    await load();
  };

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>My Assignments</h2>
        <p>Assigned maintenance work for you.</p>
      </div>

      <div style={{ marginBottom: 12, display: "flex", gap: 8 }}>
        {["All", "Pending Acceptance", "Accepted", "Active", "Completed"].map((t) => (
          <button
            key={t}
            onClick={() => setTab(t)}
            style={{ fontWeight: tab === t ? 700 : 400 }}
          >
            {t}
          </button>
        ))}
      </div>

      {loading ? (
        <div className="placeholder-box">Loading assignments…</div>
      ) : filtered.length === 0 ? (
        <div className="placeholder-box">No assignments found.</div>
      ) : (
        <div className="entity-table-wrapper">
          <table className="entity-table">
            <thead>
              <tr>
                <th>Request ID</th>
                <th>Title</th>
                <th>Location</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((r) => {
                const isPendingAccept =
                  (r.status === "Assigned" || r.assignmentStatus === "Assigned") &&
                  r.status !== "Accepted" &&
                  r.assignmentStatus !== "Accepted";
                const isAccepted =
                  r.status === "Accepted" || r.assignmentStatus === "Accepted";

                return (
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
                    <td>
                      <span
                        className={`status-pill status-${String(r.status).replace(/\s+/g, "-").toLowerCase()}`}
                      >
                        {r.status}
                      </span>
                    </td>
                    <td>
                      {isPendingAccept ? (
                        <>
                          <button onClick={() => onAccept(r.id)}>
                            Accept Assignment
                          </button>
                          <button
                            onClick={() => onReject(r.id)}
                            style={{ marginLeft: 6 }}
                          >
                            Reject
                          </button>
                        </>
                      ) : null}

                      {isAccepted ? (
                        <button onClick={() => onStart(r.id)}>
                          Start Work (In Progress)
                        </button>
                      ) : null}

                      {r.status === "In Progress" ? (
                        <button onClick={() => onComplete(r.id)}>
                          Mark as Completed
                        </button>
                      ) : null}

                      <button
                        onClick={() => navigate(`/technician/requests/${r.id}`)}
                        style={{ marginLeft: 8 }}
                      >
                        View Route & Details
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default TechnicianAssignmentsPage;
