import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

// Handles serving static files
class StaticFileHandler implements HttpHandler {
    private final String ROOT_DIRECTORY;

    public StaticFileHandler(String rootDirectory) {
        this.ROOT_DIRECTORY = rootDirectory;
    }

    // Called whenever an HTTP request is made
    // exchange: represents the request and the subsequent response to be generated
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        System.out.println("Requested: " + path);

        if (path.equals("/"))
            path = "/html/index.html"; // Default to index.html
        Path filePath = Path.of(ROOT_DIRECTORY, path);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Sets HTTP 200 response code and file size
            exchange.sendResponseHeaders(200, fileBytes.length);
            // Write file content to HTTP response body
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
                System.out.println("Served: " + path);
            }
        }
        else {
            String response = "404 Not Found";
            exchange.sendResponseHeaders(404, response.length());
            System.out.println(filePath + " does not exist.");

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}