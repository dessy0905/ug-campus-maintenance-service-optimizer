package gh.edu.ug.cs.ugmaintenance.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class ApiServer {

    private static final int DEFAULT_PORT = 8081;

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(
                System.getProperty("api.port", String.valueOf(DEFAULT_PORT))
        );
        start(port);
    }

    public static void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api", new ApiRouter());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("UG Campus Maintenance API running on http://localhost:" + port + "/api");
    }
}
