import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { createRequest } from "../services/api";
import "../layouts/AppLayout.css";
import "./CreateRequestPage.css";

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
              >
                {LOCATIONS.map((l) => (
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
              >
                {CATEGORIES.map((c) => (
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
            <button type="submit" disabled={loading}>
              {loading ? "Submitting…" : "Submit Request"}
            </button>
            <button type="button" onClick={() => navigate(-1)}>
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
          <div className="success-actions">
            <button onClick={() => navigate(`/user/requests/${created.id}`)}>
              View Request Details
            </button>
            <button onClick={() => navigate("/user/my-requests")}>
              Go to My Requests
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default CreateRequestPage;
