import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import {
  getRequestById,
  getTechnicianById,
  acceptAssignment,
  rejectAssignment,
  updateRequestStatus,
  getOptimizedRoute,
} from "../services/api";
import "../layouts/AppLayout.css";

function TechnicianRequestDetailsPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const [request, setRequest] = useState(null);
  const [tech, setTech] = useState(null);
  const [route, setRoute] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      setLoading(true);
      const r = await getRequestById(id);
      setRequest(r);
      if (r && r.assignedTechnician) {
        const t = await getTechnicianById(r.assignedTechnician);
        setTech(t);
      }
      try {
        const rt = await getOptimizedRoute(id);
        setRoute(rt);
      } catch (e) {
        setRoute(null);
      }
      setLoading(false);
    }
    load();
  }, [id]);

  const [actionError, setActionError] = useState(null);

  const doAccept = async () => {
    setActionError(null);
    try {
      await acceptAssignment(id, user.id);
      const r = await getRequestById(id);
      setRequest(r);
    } catch (err) {
      setActionError(err.message || "Failed to accept assignment.");
    }
  };

  const doReject = async () => {
    const reason = prompt("Optional rejection reason");
    setActionError(null);
    try {
      await rejectAssignment(id, reason, user.id);
      const r = await getRequestById(id);
      setRequest(r);
    } catch (err) {
      setActionError(err.message || "Failed to reject assignment.");
    }
  };

  const startWork = async () => {
    setActionError(null);
    try {
      await updateRequestStatus(id, "In Progress");
      const r = await getRequestById(id);
      setRequest(r);
    } catch (err) {
      setActionError(err.message || "Failed to start work.");
    }
  };

  const completeWork = async () => {
    setActionError(null);
    try {
      await updateRequestStatus(id, "Completed");
      const r = await getRequestById(id);
      setRequest(r);
    } catch (err) {
      setActionError(err.message || "Failed to complete work.");
    }
  };

  if (loading)
    return (
      <div className="page-card">
        <div className="placeholder-box">Loading…</div>
      </div>
    );
  if (!request)
    return (
      <div className="page-card">
        <div className="placeholder-box">Request not found.</div>
      </div>
    );

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <div>
          <h2>{request.title}</h2>
          <p>
            ID: <strong>{request.id}</strong>
          </p>
        </div>
        <div>
          <span
            className={`status-pill status-${String(request.status).replace(/\s+/g, "-").toLowerCase()}`}
          >
            {request.status}
          </span>
          <span
            style={{ marginLeft: 8 }}
            className={`status-pill status-priority-${request.priority}`}
          >
            Priority: {request.priority}
          </span>
        </div>
      </div>

      <div
        style={{ display: "grid", gridTemplateColumns: "1fr 360px", gap: 16 }}
      >
        <div>
          <div className="summary-card">
            <h3>Problem & Location</h3>
            <p>
              <strong>Category:</strong> {request.category}
            </p>
            <p>
              <strong>Location:</strong> {request.location}
            </p>
            <p>
              <strong>Date:</strong> {request.date}
            </p>
            <div style={{ marginTop: 12 }}>{request.description}</div>
          </div>

          <div className="secondary-section" style={{ marginTop: 18 }}>
            <h3>Action Panel</h3>
            {actionError && (
              <div
                style={{
                  padding: "8px 12px",
                  marginBottom: 10,
                  borderRadius: 6,
                  background: "rgba(239, 68, 68, 0.15)",
                  color: "#dc2626",
                  fontSize: "0.9rem",
                  fontWeight: 500,
                }}
              >
                {actionError}
              </div>
            )}
            <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
              {(request.status === "Assigned" ||
                request.assignmentStatus === "Assigned" ||
                request.status === "Pending") &&
                request.status !== "Accepted" &&
                request.assignmentStatus !== "Accepted" && (
                <>
                  <button onClick={doAccept} style={{ backgroundColor: "#16a34a", color: "#fff" }}>
                    Accept Assignment
                  </button>
                  <button onClick={doReject} style={{ backgroundColor: "#dc2626", color: "#fff" }}>
                    Reject Assignment
                  </button>
                </>
              )}
              {(request.status === "Accepted" ||
                request.assignmentStatus === "Accepted" ||
                request.status === "Assigned") && (
                <button onClick={startWork} style={{ backgroundColor: "#2563eb", color: "#fff" }}>
                  Start Work (In Progress)
                </button>
              )}
              {request.status === "In Progress" && (
                <button onClick={completeWork} style={{ backgroundColor: "#059669", color: "#fff" }}>
                  Mark as Completed
                </button>
              )}
            </div>
          </div>
        </div>

        <div>
          <div className="summary-card">
            <h3>Assignment</h3>
            {tech ? (
              <div>
                <strong>{tech.name}</strong>
                <p>{tech.specialization}</p>
                <p>{tech.phone}</p>
              </div>
            ) : (
              <div className="placeholder-box">Pending Assignment</div>
            )}
          </div>

          <div className="secondary-section" style={{ marginTop: 12 }}>
            <h3>Route</h3>
            {route ? (
              <div>
                <p>
                  <strong>{route.start}</strong> →{" "}
                  <strong>{route.destination}</strong>
                </p>
                <p>
                  Distance: {route.distanceMeters} m ({Number(route.distanceKm).toFixed(2)} km)
                </p>
                <p>
                  ETA: walking {route.estimated.walking} / driving{" "}
                  {route.estimated.driving}
                </p>
                <ol>
                  {route.steps.map((s, i) => (
                    <li key={i}>{s}</li>
                  ))}
                </ol>
                <div
                  style={{
                    marginTop: 8,
                    padding: 8,
                    background: "#fff7e6",
                    borderRadius: 8,
                  }}
                >
                  <strong>Note:</strong> Route calculated using Dijkstra's /
                  Shortest Path Graph Algorithm on Java Backend.
                </div>
              </div>
            ) : (
              <div className="placeholder-box">No route available.</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default TechnicianRequestDetailsPage;
