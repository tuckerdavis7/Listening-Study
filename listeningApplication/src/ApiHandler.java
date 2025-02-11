import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ApiHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        if ("GET".equalsIgnoreCase(method)) {
            response = handleGetRequest(exchange);
        }
        else if ("POST".equalsIgnoreCase(method)) {
            response = handlePostRequest(exchange);
        }
        else {
            response = "405 Method Not Allowed";
            exchange.sendResponseHeaders(405, response.length());
        }

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String handleGetRequest(HttpExchange exchange) {
        return "{ \"message\": \"This is a GET response\" }";
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        return "{ \"message\": \"POST request received\" }";
    }
}
