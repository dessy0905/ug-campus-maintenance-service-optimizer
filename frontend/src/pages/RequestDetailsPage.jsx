import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  getRequestById,
  getTechnicianById,
  getOptimizedRoute,
} from "../services/api";
import "../layouts/AppLayout.css";

function StatusStep({ label, active }) {
  return (
    <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
      <div
        style={{
          width: 18,
          height: 18,
          borderRadius: 18,
          background: active ? "#2d5bff" : "#dfe8ff",
        }}
      />
      <div style={{ color: active ? "#1f2b4d" : "#6b748d", fontWeight: active ? 600 : 400 }}>
        {label}
      </div>
    </div>
  );
}

function RequestDetailsPage() {
  const { id } = useParams();
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
        const [t, rt] = await Promise.all([
          getTechnicianById(r.assignedTechnician).catch(() => null),
          getOptimizedRoute(id).catch(() => null),
        ]);
        setTech(t);
        setRoute(rt);
      }
      setLoading(false);
    }
    load();
  }, [id]);

  if (loading) {
    return (
      <div className="page-card">
        <div className="placeholder-box">Loading request details…</div>
      </div>
    );
  }

  if (!request) {
    return (
      <div className="page-card">
        <div className="placeholder-box">Request #{id} not found.</div>
      </div>
    );
  }

  const statuses = ["Pending", "Assigned", "Accepted", "In Progress", "Completed"];
  const currentStatus = request.status || "Pending";
  const activeIndex =
    statuses.indexOf(currentStatus) >= 0
      ? statuses.indexOf(currentStatus)
      : statuses.indexOf("Assigned");

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <div>
          <h2>{request.title}</h2>
          <p>
            Request <strong>#{request.id}</strong> • Logged on: {request.date || "—"}
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
            Urgency: Level {request.priority}
          </span>
        </div>
      </div>

      <div
        style={{ display: "grid", gridTemplateColumns: "1fr 340px", gap: 20 }}
      >
        <div>
          <div className="summary-card">
            <h3>Request Information</h3>
            <table style={{ width: "100%", borderCollapse: "collapse", marginTop: 8 }}>
              <tbody>
                <tr>
                  <td style={{ padding: "6px 0", color: "#64748b", width: 140 }}>
                    <strong>Title:</strong>
                  </td>
                  <td style={{ padding: "6px 0", fontWeight: 600 }}>
                    {request.title}
                  </td>
                </tr>
                <tr>
                  <td style={{ padding: "6px 0", color: "#64748b" }}>
                    <strong>Category:</strong>
                  </td>
                  <td style={{ padding: "6px 0" }}>{request.category}</td>
                </tr>
                <tr>
                  <td style={{ padding: "6px 0", color: "#64748b" }}>
                    <strong>Location:</strong>
                  </td>
                  <td style={{ padding: "6px 0" }}>{request.location}</td>
                </tr>
                <tr>
                  <td style={{ padding: "6px 0", color: "#64748b" }}>
                    <strong>Urgency Level:</strong>
                  </td>
                  <td style={{ padding: "6px 0" }}>
                    Level {request.priority} (1 = Lowest, 5 = Critical)
                  </td>
                </tr>
                <tr>
                  <td style={{ padding: "6px 0", color: "#64748b" }}>
                    <strong>Status:</strong>
                  </td>
                  <td style={{ padding: "6px 0", fontWeight: 600 }}>
                    {request.status}
                  </td>
                </tr>
                {request.assignmentStatus && (
                  <tr>
                    <td style={{ padding: "6px 0", color: "#64748b" }}>
                      <strong>Assignment Status:</strong>
                    </td>
                    <td style={{ padding: "6px 0" }}>
                      {request.assignmentStatus}
                    </td>
                  </tr>
                )}
                <tr>
                  <td
                    style={{
                      padding: "6px 0",
                      color: "#64748b",
                      verticalAlign: "top",
                    }}
                  >
                    <strong>Description:</strong>
                  </td>
                  <td style={{ padding: "6px 0" }}>
                    {request.description || "No additional description provided."}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div className="secondary-section" style={{ marginTop: 20 }}>
            <h3>Request Lifecycle Progress</h3>
            <div style={{ display: "flex", gap: 14, alignItems: "center", flexWrap: "wrap" }}>
              {statuses.map((s, i) => (
                <StatusStep key={s} label={s} active={i <= activeIndex} />
              ))}
            </div>
          </div>
        </div>

        <div>
          <div className="summary-card">
            <h3>Assigned Technician</h3>
            {tech ? (
              <div>
                <p style={{ fontSize: "1.1rem", fontWeight: 700, margin: "4px 0" }}>
                  {tech.name}
                </p>
                <p style={{ color: "#475569", margin: "2px 0" }}>
                  {tech.specialization} Specialist
                </p>
                <p style={{ color: "#475569", margin: "2px 0" }}>
                  <strong>Contact:</strong> {tech.phone || "—"}
                </p>
                <div style={{ marginTop: 12 }}>
                  <p style={{ margin: "2px 0" }}>
                    <strong>Technician Location:</strong>
                  </p>
                  <p style={{ color: "#1e293b", fontWeight: 500 }}>
                    📍 {tech.location || route?.start || "Campus Depot"}
                  </p>
                </div>
                {route && (
                  <div style={{ marginTop: 10 }}>
                    <p style={{ margin: "2px 0" }}>
                      <strong>Route Distance:</strong>
                    </p>
                    <p style={{ color: "#2563eb", fontWeight: 600 }}>
                      {Number(route.distanceKm).toFixed(1)} km ({route.distanceMeters} m)
                    </p>
                  </div>
                )}
              </div>
            ) : (
              <div className="placeholder-box">
                Pending Assignment — no technician assigned yet.
              </div>
            )}
          </div>

          {route && (
            <div className="secondary-section" style={{ marginTop: 16 }}>
              <h3>Shortest Route Plan</h3>
              <p style={{ fontSize: "0.88rem", color: "#475569" }}>
                <strong>From:</strong> {route.start} <br />
                <strong>To:</strong> {route.destination}
              </p>
              <p style={{ fontSize: "0.88rem", color: "#475569" }}>
                ETA: {route.estimated?.driving} driving / {route.estimated?.walking} walking
              </p>
              <ol style={{ paddingLeft: 20, fontSize: "0.85rem", marginTop: 8 }}>
                {route.steps?.map((step, idx) => (
                  <li key={idx}>{step}</li>
                ))}
              </ol>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default RequestDetailsPage;
