import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerApp {
    public static void main(String[] args) throws IOException {
        final int PORT = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", new StaticFileHandler("src/static"));
        server.createContext("/api/login", new LoginHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + PORT);
    }
}
