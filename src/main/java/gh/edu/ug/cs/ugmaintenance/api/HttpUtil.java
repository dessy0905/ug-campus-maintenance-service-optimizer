package gh.edu.ug.cs.ugmaintenance.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.sun.net.httpserver.HttpExchange;

import gh.edu.ug.cs.ugmaintenance.datastructures.hash.Map;
import gh.edu.ug.cs.ugmaintenance.datastructures.hash.Set;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;

public final class HttpUtil {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(
                    List.class,
                    (JsonSerializer<List<?>>) (src, typeOfSrc, context) -> {
                        JsonArray array = new JsonArray();
                        if (src != null) {
                            for (int i = 0; i < src.size(); i++) {
                                array.add(context.serialize(src.get(i)));
                            }
                        }
                        return array;
                    }
            )
            .registerTypeHierarchyAdapter(
                    Map.class,
                    (JsonSerializer<Map>) (src, typeOfSrc, context) -> {
                        JsonObject obj = new JsonObject();
                        if (src != null) {
                            List keys = src.keySet();
                            for (int i = 0; i < keys.size(); i++) {
                                Object key = keys.get(i);
                                obj.add(String.valueOf(key), context.serialize(src.get(key)));
                            }
                        }
                        return obj;
                    }
            )
            .registerTypeHierarchyAdapter(
                    Set.class,
                    (JsonSerializer<Set<?>>) (src, typeOfSrc, context) -> {
                        JsonArray array = new JsonArray();
                        if (src != null) {
                            List<?> list = src.toList();
                            for (int i = 0; i < list.size(); i++) {
                                array.add(context.serialize(list.get(i)));
                            }
                        }
                        return array;
                    }
            )
            .create();

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
