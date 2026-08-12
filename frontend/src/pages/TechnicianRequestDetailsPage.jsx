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

  const doAccept = async () => {
    await acceptAssignment(id, user.id);
    const r = await getRequestById(id);
    setRequest(r);
  };

  const doReject = async () => {
    const reason = prompt("Optional rejection reason");
    await rejectAssignment(id, reason);
    const r = await getRequestById(id);
    setRequest(r);
  };

  const startWork = async () => {
    await updateRequestStatus(id, "In Progress");
    const r = await getRequestById(id);
    setRequest(r);
  };

  const completeWork = async () => {
    await updateRequestStatus(id, "Completed");
    const r = await getRequestById(id);
    setRequest(r);
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
            <div style={{ display: "flex", gap: 8 }}>
              {(request.status === "Assigned" ||
                request.status === "Pending") && (
                <button onClick={doAccept}>Accept Assignment</button>
              )}
              {(request.status === "Assigned" ||
                request.status === "Pending") && (
                <button onClick={doReject}>Reject Assignment</button>
              )}
              {request.status === "Assigned" && (
                <button onClick={startWork}>Start Work (In Progress)</button>
              )}
              {request.status === "In Progress" && (
                <button onClick={completeWork}>Mark as Completed</button>
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
                  Distance: {route.distanceMeters} m ({route.distanceKm} km)
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
