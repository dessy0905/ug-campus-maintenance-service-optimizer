import { Routes, Route, Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./contexts/AuthContext";
import AppLayout from "./layouts/AppLayout";
import LoginPage from "./pages/LoginPage";
import UserDashboard from "./pages/UserDashboard";
import TechnicianDashboard from "./pages/TechnicianDashboard";
import AdminDashboard from "./pages/AdminDashboard";
import CreateRequestPage from "./pages/CreateRequestPage";
import MyRequestsPage from "./pages/MyRequestsPage";
import RequestDetailsPage from "./pages/RequestDetailsPage";
import TechnicianAssignmentsPage from "./pages/TechnicianAssignmentsPage";
import TechnicianRequestDetailsPage from "./pages/TechnicianRequestDetailsPage";
import AdminRequestsPage from "./pages/AdminRequestsPage";
import AdminTechniciansPage from "./pages/AdminTechniciansPage";
import AdminOptimizationPage from "./pages/AdminOptimizationPage";

function App() {
  const { user, loading } = useAuth();

  if (loading) {
    return <div className="loading-screen">Loading...</div>;
  }
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<AppLayout />}>
        <Route
          path="user"
          element={
            user?.role === "Campus User" ? (
              <Outlet />
            ) : (
              <Navigate replace to="/login" />
            )
          }
        >
          <Route index element={<UserDashboard />} />
          <Route path="create-request" element={<CreateRequestPage />} />
          <Route path="my-requests" element={<MyRequestsPage />} />
          <Route path="requests/:id" element={<RequestDetailsPage />} />
        </Route>

        <Route
          path="technician"
          element={
            user?.role === "Technician" ? (
              <Outlet />
            ) : (
              <Navigate replace to="/login" />
            )
          }
        >
          <Route index element={<TechnicianDashboard />} />
          <Route path="assignments" element={<TechnicianAssignmentsPage />} />
          <Route
            path="requests/:id"
            element={<TechnicianRequestDetailsPage />}
          />
        </Route>
        <Route
          path="admin"
          element={
            user?.role === "Admin" ? (
              <Outlet />
            ) : (
              <Navigate replace to="/login" />
            )
          }
        >
          <Route index element={<AdminDashboard />} />
          <Route path="requests" element={<AdminRequestsPage />} />
          <Route path="technicians" element={<AdminTechniciansPage />} />
          <Route path="optimization" element={<AdminOptimizationPage />} />
        </Route>
        <Route
          path=""
          element={
            <Navigate
              replace
              to={
                user
                  ? `/${user.role === "Admin" ? "admin" : user.role === "Technician" ? "technician" : "user"}`
                  : "/login"
              }
            />
          }
        />
      </Route>
      <Route path="*" element={<Navigate replace to="/login" />} />
    </Routes>
  );
}

export default App;
