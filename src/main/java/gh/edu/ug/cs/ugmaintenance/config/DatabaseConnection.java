package gh.edu.ug.cs.ugmaintenance.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DatabaseConnection {

    private static final int POOL_SIZE = 10;
    private static final BlockingQueue<Connection> POOL = new ArrayBlockingQueue<>(POOL_SIZE);
    private static volatile boolean poolInitialized = false;
    private static final Object INIT_LOCK = new Object();

    private DatabaseConnection() {
    }

    private static void initPool() {
        if (poolInitialized) {
            return;
        }
        synchronized (INIT_LOCK) {
            if (poolInitialized) {
                return;
            }
            for (int i = 0; i < 3; i++) {
                try {
                    Connection raw = createRawConnection();
                    POOL.offer(raw);
                } catch (SQLException e) {
                    System.err.println("Warning: Pre-warming DB connection failed: " + e.getMessage());
                }
            }
            poolInitialized = true;
        }
    }

    private static Connection createRawConnection() throws SQLException {
        String host = DatabaseConfig.getProperty("db.host");
        String port = DatabaseConfig.getProperty("db.port");
        String database = DatabaseConfig.getProperty("db.name");

        String username = DatabaseConfig.getProperty("db.username");
        String password = DatabaseConfig.getProperty("db.password");

        boolean ssl = Boolean.parseBoolean(
                DatabaseConfig.getProperty("db.ssl")
        );

        String url = "jdbc:mysql://"
                + host
                + ":"
                + port
                + "/"
                + database
                + "?serverTimezone=UTC"
                + "&connectTimeout=10000"
                + "&socketTimeout=30000"
                + "&autoReconnect=true"
                + "&sslMode="
                + (ssl ? "REQUIRED" : "DISABLED");

        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection() throws SQLException {
        initPool();

        Connection rawConn = null;
        try {
            rawConn = POOL.poll(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (rawConn == null || rawConn.isClosed() || !rawConn.isValid(2)) {
            if (rawConn != null && !rawConn.isClosed()) {
                try {
                    rawConn.close();
                } catch (Exception ignored) {
                }
            }
            rawConn = createRawConnection();
        }

        final Connection finalRaw = rawConn;
        return (Connection) Proxy.newProxyInstance(
                DatabaseConnection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                new InvocationHandler() {
                    private boolean closed = false;

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("close".equals(method.getName())) {
                            if (!closed) {
                                closed = true;
                                if (!finalRaw.isClosed()) {
                                    if (!POOL.offer(finalRaw)) {
                                        finalRaw.close();
                                    }
                                }
                            }
                            return null;
                        }
                        if ("isClosed".equals(method.getName())) {
                            return closed || finalRaw.isClosed();
                        }
                        return method.invoke(finalRaw, args);
                    }
                }
        );
    }
}