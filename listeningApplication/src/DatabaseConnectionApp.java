import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionApp {
    public static void main(String[] args) throws IOException {
        String url = "jdbc:mysql://localhost:3306;databaseName=MusicApp;encrypt=true;trustServerCertificate=true";
        String user = "LENXG456/tdavi";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Connected to SQL Server successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
