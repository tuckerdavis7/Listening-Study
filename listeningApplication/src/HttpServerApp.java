import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

// Creates HTTP server
public class HttpServerApp {
    public static void main(String[] args) throws IOException {
        final int PORT = 8080;

        // Creates instance of HTTP server
        // Binds the server to PORT and says to use system's default number of threads
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Sets up route to "/" and will serve those files
        server.createContext("/", new StaticFileHandler("src/static"));

        // Use the default thread pool
        server.setExecutor(null);
        
        server.start();
        System.out.println("Server started at http://localhost:" + PORT);
    }
}