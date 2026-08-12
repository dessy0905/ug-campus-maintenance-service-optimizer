import { useEffect, useState } from "react";
import { getTechnicians } from "../services/api";
import "../layouts/AppLayout.css";

function AdminTechniciansPage() {
  const [techs, setTechs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      setLoading(true);
      const t = await getTechnicians();
      setTechs(t);
      setLoading(false);
    }
    load();
  }, []);

  const toggleAvailability = (id) => {
    const idx = techs.findIndex((x) => x.id === id);
    if (idx === -1) return;
    const copy = [...techs];
    copy[idx].status =
      copy[idx].status === "Available" ? "On Leave" : "Available";
    setTechs(copy);
  };

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>Technician Management</h2>
      </div>
      {loading ? (
        <div className="placeholder-box">Loading technicians…</div>
      ) : (
        <div className="team-grid">
          {techs.map((t) => (
            <div key={t.id} className="team-card">
              <strong>{t.name}</strong>
              <p>
                {t.specialization} • {t.category}
              </p>
              <p>{t.phone}</p>
              <p>Status: {t.status || "Available"}</p>
              <p>Active Tasks: {t.assignedCount || 0}</p>
              <div style={{ marginTop: 8 }}>
                <button onClick={() => toggleAvailability(t.id)}>
                  Toggle Availability
                </button>
                <button style={{ marginLeft: 8 }}>View Assigned Tasks</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default AdminTechniciansPage;
