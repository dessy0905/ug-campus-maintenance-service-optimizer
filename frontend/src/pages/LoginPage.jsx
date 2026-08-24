import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { loginUser, getTechnicians } from "../services/api";
import "./LoginPage.css";
import backgroundImage from "../assets/background.jpg";

function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const [technicians, setTechnicians] = useState([]);
  const [selectedTechId, setSelectedTechId] = useState("");
  const [showTechSelect, setShowTechSelect] = useState(false);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      const redirectPath =
        user.role === "Admin"
          ? "/admin"
          : user.role === "Technician"
            ? "/technician"
            : "/user";
      navigate(redirectPath, { replace: true });
    }
  }, [user, navigate]);

  useEffect(() => {
    async function loadTechs() {
      try {
        const techs = await getTechnicians();
        if (techs && techs.length > 0) {
          setTechnicians(techs);
          setSelectedTechId(String(techs[0].id));
        }
      } catch {
        // Fallback gracefully if API not ready
      }
    }
    loadTechs();
  }, []);

  const handleLogin = async (role, techId = null) => {
    setError(null);
    setLoading(true);

    try {
      const payload = { role };
      if (role === "Technician" && techId) {
        payload.technicianId = Number(techId);
      }
      const userData = await loginUser(payload);
      login(userData);
      const redirectPath =
        userData.role === "Admin"
          ? "/admin"
          : userData.role === "Technician"
            ? "/technician"
            : "/user";
      navigate(redirectPath, { replace: true });
    } catch (loginError) {
      setError(loginError.message || "Unable to login. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="login-page"
      style={{ backgroundImage: `url(${backgroundImage})` }}
    >
      <div className="login-card">
        <div className="login-header">
          <h2>UG Campus Maintenance Optimizer</h2>
          <p>Role-based access Login.</p>
        </div>

        <div className="login-buttons">
          <button
            type="button"
            onClick={() => handleLogin("Campus User")}
            disabled={loading}
          >
            Campus User
          </button>

          {!showTechSelect ? (
            <button
              type="button"
              onClick={() => {
                if (technicians.length > 0) {
                  setShowTechSelect(true);
                } else {
                  handleLogin("Technician");
                }
              }}
              disabled={loading}
            >
              Technician
            </button>
          ) : (
            <div
              style={{
                display: "flex",
                flexDirection: "column",
                gap: "8px",
                width: "100%",
                padding: "8px",
                background: "rgba(255, 255, 255, 0.08)",
                borderRadius: "8px",
              }}
            >
              <label
                style={{
                  fontSize: "0.85rem",
                  color: "#e2e8f0",
                  textAlign: "left",
                }}
              >
                Select Technician:
              </label>
              <select
                value={selectedTechId}
                onChange={(e) => setSelectedTechId(e.target.value)}
                style={{
                  padding: "8px 12px",
                  borderRadius: "6px",
                  border: "1px solid #cbd5e1",
                  background: "#ffffff",
                  color: "#1e293b",
                  fontWeight: 500,
                  width: "100%",
                }}
              >
                {technicians.map((t) => (
                  <option key={t.id} value={t.id}>
                    #{t.id} - {t.name} ({t.specialization})
                  </option>
                ))}
              </select>
              <div style={{ display: "flex", gap: "6px" }}>
                <button
                  type="button"
                  onClick={() => handleLogin("Technician", selectedTechId)}
                  disabled={loading}
                  style={{ flex: 1 }}
                >
                  Sign In as Tech #{selectedTechId}
                </button>
                <button
                  type="button"
                  onClick={() => setShowTechSelect(false)}
                  style={{
                    background: "transparent",
                    border: "1px solid rgba(255,255,255,0.3)",
                    color: "#fff",
                    padding: "0 12px",
                  }}
                >
                  Cancel
                </button>
              </div>
            </div>
          )}

          <button
            type="button"
            onClick={() => handleLogin("Admin")}
            disabled={loading}
          >
            Admin
          </button>
        </div>

        {error ? <div className="form-error">{error}</div> : null}
        {loading ? <div className="form-note">Signing in…</div> : null}
      </div>
    </div>
  );
}

export default LoginPage;
