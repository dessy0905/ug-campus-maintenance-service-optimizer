import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { createRequest } from "../services/api";
import "../layouts/AppLayout.css";

const LOCATIONS = [
  "Balme Library",
  "Volta Hall",
  "Night Market",
  "JCT",
  "Main Gate",
  "Computer Science Department",
];
const CATEGORIES = [
  "Plumbing",
  "Electrical",
  "HVAC",
  "Carpentry",
  "Masonry",
  "General Maintenance",
];

function CreateRequestPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [location, setLocation] = useState(LOCATIONS[0]);
  const [category, setCategory] = useState(CATEGORIES[0]);
  const [priority, setPriority] = useState(3);
  const [loading, setLoading] = useState(false);
  const [created, setCreated] = useState(null);

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
      alert("Failed to create request.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>Create Maintenance Request</h2>
        <p>Submit a new service request — mock-only flow.</p>
      </div>

      {!created ? (
        <form onSubmit={submit} className="create-request-form">
          <div className="form-row">
            <label>Title</label>
            <input
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>

          <div className="form-row">
            <label>Description</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={5}
              required
            />
          </div>

          <div className="form-grid">
            <div className="form-row">
              <label>Location</label>
              <select
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              >
                {LOCATIONS.map((l) => (
                  <option key={l} value={l}>
                    {l}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-row">
              <label>Service Category</label>
              <select
                value={category}
                onChange={(e) => setCategory(e.target.value)}
              >
                {CATEGORIES.map((c) => (
                  <option key={c} value={c}>
                    {c}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-row">
              <label>Priority</label>
              <select
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

          <div style={{ marginTop: 18 }}>
            <button type="submit" disabled={loading}>
              Submit Request
            </button>
            <button
              type="button"
              style={{ marginLeft: 8 }}
              onClick={() => navigate(-1)}
            >
              Cancel
            </button>
          </div>
        </form>
      ) : (
        <div className="placeholder-box">
          <h3>Request Created</h3>
          <p>
            Your request has been created with ID <strong>{created.id}</strong>.
          </p>
          <div style={{ marginTop: 12 }}>
            <button onClick={() => navigate(`/user/requests/${created.id}`)}>
              View Request Details
            </button>
            <button
              onClick={() => navigate("/user/my-requests")}
              style={{ marginLeft: 8 }}
            >
              Go to My Requests
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default CreateRequestPage;
