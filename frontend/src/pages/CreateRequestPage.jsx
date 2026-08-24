import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { createRequest, getCategories, getLocations } from "../services/api";
import "../layouts/AppLayout.css";
import "./CreateRequestPage.css";

const FALLBACK_CATEGORIES = [
  "Plumbing",
  "Electrical",
  "ICT Support",
  "Carpentry",
  "Cleaning",
  "Security",
  "AC Services",
];

const FALLBACK_LOCATIONS = [
  "Maths department",
  "Computer science department",
  "Statistics department",
  "ISSSER",
  "Law school",
  "School of engineering",
  "Balme library",
  "UG bookshop",
  "Volta hall",
  "UGBS",
  "UG clinic",
  "Akuafo hall",
  "Legon hall",
  "Mensah sarbah hall",
  "Central cafeteria (CC)",
  "Great Hall",
];

function CreateRequestPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [locations, setLocations] = useState([]);
  const [categories, setCategories] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [location, setLocation] = useState("");
  const [category, setCategory] = useState("");
  const [priority, setPriority] = useState(3);
  const [loading, setLoading] = useState(false);
  const [created, setCreated] = useState(null);
  const [metadataWarning, setMetadataWarning] = useState("");

  useEffect(() => {
    async function loadMetadata() {
      const [locationResult, categoryResult] = await Promise.allSettled([
        getLocations(),
        getCategories(),
      ]);

      const locationNames =
        locationResult.status === "fulfilled" && locationResult.value.length
          ? locationResult.value
          : FALLBACK_LOCATIONS;
      const categoryNames =
        categoryResult.status === "fulfilled" && categoryResult.value.length
          ? categoryResult.value
          : FALLBACK_CATEGORIES;

      setLocations(locationNames);
      setCategories(categoryNames);
      setLocation(locationNames[0] || "");
      setCategory(categoryNames[0] || "");

      if (
        locationResult.status === "rejected" ||
        categoryResult.status === "rejected"
      ) {
        setMetadataWarning(
          "Some options are being shown from the local campus list. The server must be running to submit a request.",
        );
      }
    }

    loadMetadata();
  }, []);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const payload = {
        title: title.trim(),
        description: description.trim(),
        location,
        category,
        priority: Number(priority),
        createdBy: user.id,
      };

      const result = await createRequest(payload);
      setCreated(result);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to create request.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>Create Maintenance Request</h2>
        <p>
          Submit a new service request. The nearest available technician will be
          assigned automatically.
        </p>
      </div>

      {!created ? (
        <form onSubmit={submit} className="create-request-form">
          {metadataWarning ? (
            <div className="form-note">{metadataWarning}</div>
          ) : null}
          <div className="form-group">
            <label htmlFor="request-title">Title</label>
            <input
              id="request-title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Brief summary of the issue"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="request-description">Description</label>
            <textarea
              id="request-description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={5}
              placeholder="Describe the maintenance issue in detail"
              required
            />
          </div>

          <div className="form-grid">
            <div className="form-group">
              <label htmlFor="request-location">Location</label>
              <select
                id="request-location"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                required
              >
                {locations.map((l) => (
                  <option key={l} value={l}>
                    {l}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="request-category">Service Category</label>
              <select
                id="request-category"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                required
              >
                {categories.map((c) => (
                  <option key={c} value={c}>
                    {c}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="request-priority">Priority</label>
              <select
                id="request-priority"
                value={priority}
                onChange={(e) => setPriority(Number(e.target.value))}
              >
                {[1, 2, 3, 4, 5].map((p) => (
                  <option key={p} value={p}>
                    {p} {p === 1 ? "(Lowest)" : p === 5 ? "(Highest)" : ""}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" disabled={loading || !location || !category}>
              {loading ? "Submitting…" : "Submit Request"}
            </button>
            <button type="button" onClick={() => navigate(-1)}>
              Cancel
            </button>
          </div>
        </form>
      ) : (
        <div className="placeholder-box" style={{ textAlign: "left", padding: "24px", maxWidth: 640 }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "1px solid #e2e8f0", paddingBottom: 12 }}>
            <h3 style={{ margin: 0, color: "#16a34a" }}>✓ Request Submitted Successfully</h3>
            <span
              className={`status-pill status-${String(created.status || "Pending").replace(/\s+/g, "-").toLowerCase()}`}
            >
              Status: {created.status || "Pending"}
            </span>
          </div>

          <table style={{ width: "100%", borderCollapse: "collapse", marginTop: 14 }}>
            <tbody>
              <tr>
                <td style={{ padding: "6px 0", color: "#64748b", width: 140 }}>
                  <strong>Request ID:</strong>
                </td>
                <td style={{ padding: "6px 0", fontWeight: 700 }}>
                  #{created.id}
                </td>
              </tr>
              <tr>
                <td style={{ padding: "6px 0", color: "#64748b" }}>
                  <strong>Title:</strong>
                </td>
                <td style={{ padding: "6px 0", fontWeight: 600 }}>
                  {created.title}
                </td>
              </tr>
              <tr>
                <td style={{ padding: "6px 0", color: "#64748b" }}>
                  <strong>Category:</strong>
                </td>
                <td style={{ padding: "6px 0" }}>{created.category}</td>
              </tr>
              <tr>
                <td style={{ padding: "6px 0", color: "#64748b" }}>
                  <strong>Location:</strong>
                </td>
                <td style={{ padding: "6px 0" }}>{created.location}</td>
              </tr>
              <tr>
                <td style={{ padding: "6px 0", color: "#64748b" }}>
                  <strong>Priority:</strong>
                </td>
                <td style={{ padding: "6px 0" }}>
                  <span className={`status-pill status-priority-${created.priority}`}>
                    Level {created.priority}
                  </span>
                </td>
              </tr>
              <tr>
                <td style={{ padding: "6px 0", color: "#64748b" }}>
                  <strong>Current Status:</strong>
                </td>
                <td style={{ padding: "6px 0", fontWeight: 600 }}>
                  {created.status || "Pending"}
                </td>
              </tr>
              <tr>
                <td style={{ padding: "6px 0", color: "#64748b" }}>
                  <strong>Technician:</strong>
                </td>
                <td style={{ padding: "6px 0" }}>
                  {created.assignedTechnician
                    ? `Assigned to Technician #${created.assignedTechnician}`
                    : "Auto-assignment matching initiated..."}
                </td>
              </tr>
            </tbody>
          </table>

          <div className="success-actions" style={{ display: "flex", gap: "10px", marginTop: 20 }}>
            <button
              onClick={() => navigate(`/user/requests/${created.id}`)}
              style={{ backgroundColor: "#2563eb", color: "#fff" }}
            >
              View Request Details
            </button>
            <button onClick={() => navigate("/user/my-requests")}>
              Go to My Requests
            </button>
            <button
              onClick={() => {
                setCreated(null);
                setTitle("");
                setDescription("");
              }}
              style={{ background: "transparent", border: "1px solid #cbd5e1" }}
            >
              Create Another Request
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default CreateRequestPage;
