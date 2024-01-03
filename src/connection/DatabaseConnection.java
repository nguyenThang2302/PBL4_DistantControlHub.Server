package connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {

    }

    public void connectToDatabase() throws SQLException, IOException {
    	String server = "localhost";
        String port = "5432"; // PostgreSQL mặc định sử dụng cổng 5432
        String database = "pbl4.distantcontrolhub";
        String userName = "postgres";
        String password = "23022003";
        String jdbcUrl = "jdbc:postgresql://" + server + ":" + port + "/" + database;
        connection = DriverManager.getConnection(jdbcUrl, userName, password);
        if (connection != null) {
            config.Logger.logger("Server connected database postgreSQL succesfully");
        } else {
            System.out.println("Connected to database Failed");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
