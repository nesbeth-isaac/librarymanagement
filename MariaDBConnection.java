import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection {

    public static void main(String[] args) {
        // URL to connect to the MariaDB database
        String url = "jdbc:mariadb://localhost:3306/librarymanagement";
        // Database credentials
        String user = "root";
        String password = "root";

        Connection connection = null;

        try {
            // Establish the connection
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to MariaDB established successfully!");

            // Perform database operations here

        } catch (SQLException e) {
            System.err.println("Failed to connect to MariaDB.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
