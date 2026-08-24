import { useEffect, useMemo, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import {
  getTechnicianRequests,
  getTechnicians,
  acceptAssignment,
  rejectAssignment,
  updateRequestStatus,
} from "../services/api";

function TechnicianDashboard() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [assignedRequests, setAssignedRequests] = useState([]);
  const [technicianRoster, setTechnicianRoster] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(null);
  const [feedback, setFeedback] = useState({ message: "", type: "" });

  const loadDashboard = useCallback(async () => {
    if (!user || !user.id) {
      return;
    }

    setLoading(true);
    try {
      // Backend performs the filtering: GET /api/technicians/{technicianId}/requests
      const [techRequests, roster] = await Promise.all([
        getTechnicianRequests(user.id),
        getTechnicians().catch(() => []),
      ]);

      setAssignedRequests(techRequests || []);
      setTechnicianRoster(roster || []);
    } catch (err) {
      setFeedback({
        message: err.message || "Failed to load technician jobs.",
        type: "error",
      });
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    loadDashboard();
  }, [loadDashboard]);

  const statusCounts = useMemo(() => {
    const counts = {
      Total: assignedRequests.length,
      Assigned: 0,
      Accepted: 0,
      "In Progress": 0,
      Completed: 0,
    };

    assignedRequests.forEach((req) => {
      const status = req.status || "Pending";
      const assignStatus = req.assignmentStatus || "";

      if (status === "In Progress") {
        counts["In Progress"] += 1;
      } else if (status === "Completed" || assignStatus === "Completed") {
        counts["Completed"] += 1;
      } else if (status === "Accepted" || assignStatus === "Accepted") {
        counts["Accepted"] += 1;
      } else if (status === "Assigned" || assignStatus === "Assigned") {
        counts["Assigned"] += 1;
      }
    });

    return counts;
  }, [assignedRequests]);

  const handleAccept = async (requestId) => {
    setActionLoading(requestId);
    setFeedback({ message: "", type: "" });
    try {
      await acceptAssignment(requestId, user.id);
      setFeedback({
        message: `Request #${requestId} accepted successfully!`,
        type: "success",
      });
      await loadDashboard();
    } catch (err) {
      setFeedback({
        message: err.message || `Failed to accept request #${requestId}.`,
        type: "error",
      });
    } finally {
      setActionLoading(null);
    }
  };

  const handleReject = async (requestId) => {
    const reason = window.prompt("Optional rejection reason:") || "";
    setActionLoading(requestId);
    setFeedback({ message: "", type: "" });
    try {
      await rejectAssignment(requestId, reason, user.id);
      setFeedback({
        message: `Request #${requestId} rejected.`,
        type: "success",
      });
      await loadDashboard();
    } catch (err) {
      setFeedback({
        message: err.message || `Failed to reject request #${requestId}.`,
        type: "error",
      });
    } finally {
      setActionLoading(null);
    }
  };

  const handleStartWork = async (requestId) => {
    setActionLoading(requestId);
    setFeedback({ message: "", type: "" });
    try {
      await updateRequestStatus(requestId, "In Progress");
      setFeedback({
        message: `Request #${requestId} is now In Progress!`,
        type: "success",
      });
      await loadDashboard();
    } catch (err) {
      setFeedback({
        message: err.message || `Failed to start work on request #${requestId}.`,
        type: "error",
      });
    } finally {
      setActionLoading(null);
    }
  };

  const handleComplete = async (requestId) => {
    setActionLoading(requestId);
    setFeedback({ message: "", type: "" });
    try {
      await updateRequestStatus(requestId, "Completed");
      setFeedback({
        message: `Request #${requestId} marked as Completed!`,
        type: "success",
      });
      await loadDashboard();
    } catch (err) {
      setFeedback({
        message:
          err.message || `Failed to complete work on request #${requestId}.`,
        type: "error",
      });
    } finally {
      setActionLoading(null);
    }
  };

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <div>
          <h2>Technician Dashboard</h2>
          <p>
            Logged in as <strong>{user?.name || "Technician"}</strong> (ID: #
            {user?.id}) • Specialization:{" "}
            <strong>{user?.specialization || user?.category || "General"}</strong>
          </p>
        </div>
      </div>

      {feedback.message && (
        <div
          style={{
            padding: "10px 14px",
            marginBottom: "16px",
            borderRadius: "6px",
            background:
              feedback.type === "error"
                ? "rgba(239, 68, 68, 0.15)"
                : "rgba(34, 197, 94, 0.15)",
            border: `1px solid ${
              feedback.type === "error" ? "#ef4444" : "#22c55e"
            }`,
            color: feedback.type === "error" ? "#dc2626" : "#16a34a",
            fontWeight: 500,
          }}
        >
          {feedback.message}
        </div>
      )}

      {/* Summary KPI Cards */}
      <div className="dashboard-grid">
        <div className="summary-card">
          <h3>My Assigned Jobs</h3>
          <p>{statusCounts.Total} active</p>
        </div>
        <div className="summary-card">
          <h3>Assigned (Pending Accept)</h3>
          <p>{statusCounts.Assigned}</p>
        </div>
        <div className="summary-card">
          <h3>Accepted</h3>
          <p>{statusCounts.Accepted}</p>
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

      <div className="secondary-section" style={{ marginTop: "24px" }}>
        <h3>Jobs Assigned to You</h3>

        {loading ? (
          <div className="placeholder-box">Loading technician work queue…</div>
        ) : assignedRequests.length === 0 ? (
          <div className="placeholder-box">
            No active assignments currently assigned to you (Technician #{user?.id}: {user?.name}).
          </div>
        ) : (
          <div className="entity-table-wrapper">
            <table className="entity-table">
              <thead>
                <tr>
                  <th>Request ID</th>
                  <th>Request Title</th>
                  <th>Description</th>
                  <th>Category</th>
                  <th>Urgency / Priority</th>
                  <th>Request Location</th>
                  <th>Status</th>
                  <th>Assignment Status</th>
                  <th>Request Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {assignedRequests.map((request) => {
                  const isAssigned =
                    request.status === "Assigned" ||
                    request.assignmentStatus === "Assigned";
                  const isAccepted =
                    request.status === "Accepted" ||
                    request.assignmentStatus === "Accepted";
                  const isInProgress = request.status === "In Progress";
                  const isCompleted = request.status === "Completed";
                  const isBusy = actionLoading === request.id;

                  return (
                    <tr key={request.id}>
                      <td>
                        <strong>#{request.id}</strong>
                      </td>
                      <td>
                        <strong>{request.title}</strong>
                      </td>
                      <td style={{ maxWidth: 220, fontSize: "0.88rem" }}>
                        {request.description}
                      </td>
                      <td>{request.category}</td>
                      <td>
                        <span
                          className={`status-pill status-priority-${request.priority}`}
                        >
                          Level {request.priority}
                        </span>
                      </td>
                      <td>{request.location}</td>
                      <td>
                        <span
                          className={`status-pill status-${String(request.status).replace(/\s+/g, "-").toLowerCase()}`}
                        >
                          {request.status}
                        </span>
                      </td>
                      <td>
                        <span
                          className={`status-pill status-${String(request.assignmentStatus || "assigned").replace(/\s+/g, "-").toLowerCase()}`}
                        >
                          {request.assignmentStatus || "Assigned"}
                        </span>
                      </td>
                      <td>{request.date || "—"}</td>
                      <td style={{ whiteSpace: "nowrap" }}>
                        <div style={{ display: "flex", gap: "6px", flexWrap: "wrap" }}>
                          {isAssigned && (
                            <>
                              <button
                                onClick={() => handleAccept(request.id)}
                                disabled={isBusy}
                                style={{
                                  backgroundColor: "#16a34a",
                                  color: "#fff",
                                  padding: "4px 8px",
                                  fontSize: "0.85rem",
                                }}
                              >
                                {isBusy ? "Accepting…" : "Accept"}
                              </button>
                              <button
                                onClick={() => handleReject(request.id)}
                                disabled={isBusy}
                                style={{
                                  backgroundColor: "#dc2626",
                                  color: "#fff",
                                  padding: "4px 8px",
                                  fontSize: "0.85rem",
                                }}
                              >
                                Reject
                              </button>
                            </>
                          )}

                          {isAccepted && (
                            <button
                              onClick={() => handleStartWork(request.id)}
                              disabled={isBusy}
                              style={{
                                backgroundColor: "#2563eb",
                                color: "#fff",
                                padding: "4px 8px",
                                fontSize: "0.85rem",
                              }}
                            >
                              {isBusy ? "Starting…" : "Start Work"}
                            </button>
                          )}

                          {isInProgress && (
                            <button
                              onClick={() => handleComplete(request.id)}
                              disabled={isBusy}
                              style={{
                                backgroundColor: "#059669",
                                color: "#fff",
                                padding: "4px 8px",
                                fontSize: "0.85rem",
                              }}
                            >
                              {isBusy ? "Completing…" : "Mark Completed"}
                            </button>
                          )}

                          <button
                            onClick={() =>
                              navigate(`/technician/requests/${request.id}`)
                            }
                            style={{
                              padding: "4px 8px",
                              fontSize: "0.85rem",
                            }}
                          >
                            Route & Details
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="secondary-section" style={{ marginTop: "32px" }}>
        <h3>Technician Roster & Availability</h3>
        <div className="team-grid">
          {technicianRoster.map((tech) => (
            <div
              key={tech.id}
              className="team-card"
              style={{
                border: tech.id === user?.id ? "2px solid #2563eb" : undefined,
                background: tech.id === user?.id ? "#eff6ff" : undefined,
              }}
            >
              <strong>
                {tech.name} {tech.id === user?.id ? "(You)" : ""}
              </strong>
              <p>
                {tech.specialization} Specialist • Category: {tech.category}
              </p>
              <p>Location: {tech.location || "UG Campus"}</p>
              <p>
                Status:{" "}
                <span
                  className={`status-pill status-${tech.status === "Active" ? "completed" : "in-progress"}`}
                >
                  {tech.status}
                </span>
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default TechnicianDashboard;
