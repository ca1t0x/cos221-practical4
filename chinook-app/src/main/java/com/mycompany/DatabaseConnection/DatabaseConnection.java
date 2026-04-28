package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String proto    = System.getenv("CHINOOK_DB_PROTO");
            String host     = System.getenv("CHINOOK_DB_HOST");
            String port     = System.getenv("CHINOOK_DB_PORT");
            String name     = System.getenv("CHINOOK_DB_NAME");
            String username = System.getenv("CHINOOK_DB_USERNAME");
            String password = System.getenv("CHINOOK_DB_PASSWORD");

            if ("jdbc:sqlite".equals(proto)) {
                connection = DriverManager.getConnection(proto + ":" + name);
            } else {
                String url = proto + "://" + host + ":" + port + "/" + name;
                connection = DriverManager.getConnection(url, username, password);
            }
        }
        return connection;
    }
}
