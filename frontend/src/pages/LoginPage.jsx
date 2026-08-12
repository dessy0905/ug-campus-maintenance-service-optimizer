import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { loginUser } from "../services/api";
import "./LoginPage.css";
import backgroundImage from "../assets/background.jpg";

function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
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

  const handleLogin = async (role) => {
    setError(null);
    setLoading(true);

    try {
      const userData = await loginUser({ role });
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
    <div className="login-page" style={{ backgroundImage: `url(${backgroundImage})` }}>
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
          <button
            type="button"
            onClick={() => handleLogin("Technician")}
            disabled={loading}
          >
            Technician
          </button>
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
