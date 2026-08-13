import { Outlet, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import "./AppLayout.css";

function AppLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const onLogout = () => {
    logout();
    navigate("/login");
  };

  const links = {
    "Campus User": [
      { label: "Dashboard", path: "/user" },
      { label: "Create Request", path: "/user/create-request" },
      { label: "My Requests", path: "/user/my-requests" },
    ],
    Technician: [
      { label: "Dashboard", path: "/technician" },
      { label: "Assignments", path: "/technician/assignments" },
    ],
    Admin: [
      { label: "Dashboard", path: "/admin" },
      { label: "Request Management", path: "/admin/requests" },
      { label: "Technician Management", path: "/admin/technicians" },
      { label: "System Optimization", path: "/admin/optimization" },
    ],
  };

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">UG Campus Maintenance</div>
        <div className="sidebar-user">
          <div className="user-avatar">{user?.avatar || "UG"}</div>
          <div>
            <p className="user-name">{user?.name}</p>
            <p className="user-role">{user?.role}</p>
          </div>
        </div>
        <nav>
          {links[user?.role]?.map((link) => (
            <NavLink
              key={link.path + link.label}
              to={link.path}
              className={({ isActive }) =>
                isActive ? "nav-link active" : "nav-link"
              }
            >
              {link.label}
            </NavLink>
          ))}
        </nav>
        <button className="logout-button" onClick={onLogout}>
          Logout
        </button>
      </aside>

      <div className="main-panel">
        <header className="page-header">
          <div>
            <h1>UG Campus Maintenance Service Optimizer</h1>
            <p>Smart system mock frontend</p>
          </div>
          <div className="header-actions">
            <span className="role-badge">{user?.role}</span>
          </div>
        </header>
        <main className="page-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default AppLayout;
