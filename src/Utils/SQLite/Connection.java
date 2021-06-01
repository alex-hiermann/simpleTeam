package Utils.SQLite;

import Utils.Configuration;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Last updated by Alexander Hiermann on 06/01/2021
 * different templates used from sqlitetutorial.net
 */
public class Connection {

    static java.sql.Connection connection;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();
    }

    /**
     * Connection to a sample database
     */
    public static void connect() {
        try {
            connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void close() {
        connection = null;
        System.err.println("Connection to SQLite has been closed.");
    }

    public static void connectIfAbsent() {
        if (getConnection() == null) {
            connect();
            System.out.println("Connection to SQLite has been established again.");
        }
    }

    public static java.sql.Connection getConnection() {
        return connection;
    }

    public static void setConnection(java.sql.Connection connection) {
        Connection.connection = connection;
    }
}