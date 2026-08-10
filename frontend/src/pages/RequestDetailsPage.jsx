import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getRequestById, getTechnicianById } from "../services/api";
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
      <div style={{ color: active ? "#1f2b4d" : "#6b748d" }}>{label}</div>
    </div>
  );
}

function RequestDetailsPage() {
  const { id } = useParams();
  const [request, setRequest] = useState(null);
  const [tech, setTech] = useState(null);

  useEffect(() => {
    async function load() {
      const r = await getRequestById(id);
      setRequest(r);
      if (r && r.assignedTechnician) {
        const t = await getTechnicianById(r.assignedTechnician);
        setTech(t);
      }
    }
    load();
  }, [id]);

  if (!request) {
    return (
      <div className="page-card">
        <div className="placeholder-box">Request not found.</div>
      </div>
    );
  }

  const statuses = ["Pending", "Assigned", "In Progress", "Completed"];
  const activeIndex =
    statuses.indexOf(request.status) >= 0
      ? statuses.indexOf(request.status)
      : 0;

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
          <div style={{ marginBottom: 6 }}>
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
      </div>

      <div className="entity-table-wrapper">
        <div
          style={{ display: "grid", gridTemplateColumns: "1fr 320px", gap: 18 }}
        >
          <div>
            <div className="summary-card">
              <h3>Details</h3>
              <p>
                <strong>Category:</strong> {request.category}
              </p>
              <p>
                <strong>Location:</strong> {request.location}
              </p>
              <p>
                <strong>Date:</strong> {request.date}
              </p>
              <div style={{ marginTop: 12 }}>
                <p>{request.description}</p>
              </div>
            </div>

            <div className="secondary-section">
              <h3>Progress</h3>
              <div style={{ display: "flex", gap: 18, alignItems: "center" }}>
                {statuses.map((s, i) => (
                  <StatusStep key={s} label={s} active={i <= activeIndex} />
                ))}
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
          </div>
        </div>
      </div>
    </div>
  );
}

export default RequestDetailsPage;
