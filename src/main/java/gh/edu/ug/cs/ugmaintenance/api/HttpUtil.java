package gh.edu.ug.cs.ugmaintenance.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

public final class HttpUtil {

    private static final Gson GSON = new GsonBuilder().create();

    private HttpUtil() {
    }

    public static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Origin",
                "*"
        );
        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, PATCH, DELETE, OPTIONS"
        );
        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );
    }

    public static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static void sendJson(
            HttpExchange exchange,
            int statusCode,
            Object body) throws IOException {

        addCorsHeaders(exchange);
        byte[] payload = GSON.toJson(body).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, payload.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(payload);
        }
    }

    public static void sendError(
            HttpExchange exchange,
            int statusCode,
            String message) throws IOException {

        sendJson(exchange, statusCode, java.util.Map.of("error", message));
    }

    public static void handleOptions(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        exchange.sendResponseHeaders(204, -1);
    }

    public static Gson gson() {
        return GSON;
    }
}
