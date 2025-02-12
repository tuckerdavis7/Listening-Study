import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class StaticFileHandler implements HttpHandler {
    private final String ROOT_DIRECTORY;

    public StaticFileHandler(String rootDirectory) {
        this.ROOT_DIRECTORY = rootDirectory;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/"))
            path = "/html/index.html"; // Default to index.html, login page
        Path filePath = Path.of(ROOT_DIRECTORY, path);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            byte[] fileBytes = Files.readAllBytes(filePath);

            exchange.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
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