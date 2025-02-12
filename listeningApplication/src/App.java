public class App {
    public static void main(String[] args) throws Exception {
        try {
            HttpServerApp.main(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            DatabaseConnectionApp.main(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
