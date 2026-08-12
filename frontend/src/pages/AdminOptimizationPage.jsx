import { useEffect, useState } from "react";
import {
  getPriorityQueue,
  getOptimizationMetrics,
  getAllRequests,
} from "../services/api";
import "../layouts/AppLayout.css";

function AdminOptimizationPage() {
  const [queue, setQueue] = useState([]);
  const [metrics, setMetrics] = useState(null);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    const [q, m] = await Promise.all([
      getPriorityQueue(),
      getOptimizationMetrics(),
    ]);
    setQueue(q);
    setMetrics(m);
    setLoading(false);
  }

  useEffect(() => {
    load();
  }, []);

  return (
    <div className="page-card">
      <div className="dashboard-header">
        <h2>DSA & System Optimization</h2>
      </div>

      {loading ? (
        <div className="placeholder-box">Loading metrics…</div>
      ) : (
        <>
          <div className="secondary-section">
            <h3>Priority Queue (Max-Heap)</h3>
            <div className="placeholder-box">
              <p>
                Head of Queue:{" "}
                {queue.length ? `${queue[0].id} (${queue[0].priority})` : "—"}
              </p>
              <ol>
                {queue.map((r) => (
                  <li key={r.id}>
                    {r.id} — {r.title} — Priority {r.priority}
                  </li>
                ))}
              </ol>
              <div
                style={{
                  marginTop: 8,
                  padding: 8,
                  background: "#eef3ff",
                  borderRadius: 8,
                }}
              >
                Backend implementation: Max Heap / PriorityQueue Data Structure.
              </div>
            </div>
          </div>

          <div className="secondary-section">
            <h3>Smart Technician Matching</h3>
            <div className="placeholder-box">
              <p>
                Unassigned request → Matching technician (specialization +
                proximity)
              </p>
              <button
                style={{ marginTop: 8 }}
                onClick={async () => {
                  // naive mock: assign highest priority pending to first available technician
                  const pending = await getPriorityQueue();
                  const all = await getAllRequests();
                  alert("Auto-assignment mocked for demo");
                }}
              >
                Run Auto-Assignment Algorithm
              </button>
            </div>
          </div>

          <div className="secondary-section">
            <h3>Campus Route Shortest-Path Optimizer</h3>
            <div className="placeholder-box">
              <p>
                Map placeholder — nodes: Main Gate, Balme Library, JCT, Volta
                Hall, Night Market, CCB
              </p>
              <p>
                Example path: Main Gate → JCT → Balme Library — Distance: 0.85
                km
              </p>
              <div
                style={{
                  marginTop: 8,
                  padding: 8,
                  background: "#fff7e6",
                  borderRadius: 8,
                }}
              >
                Backend implementation: Dijkstra's / A* Graph Shortest Path
                Algorithm.
              </div>
            </div>
          </div>

          <div className="dashboard-note" style={{ marginTop: 12 }}>
            Metrics: Average response {metrics.averageResponseMinutes} minutes,
            Pending Queue {metrics.pendingPriorityQueueLength}
          </div>
        </>
      )}
    </div>
  );
}

export default AdminOptimizationPage;
