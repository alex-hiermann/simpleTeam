package Utils.SQLite;

import Utils.Configuration;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 * <p>
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
            System.out.println(Configuration.ANSI_BLUE + "Connection to SQLite has been established." + Configuration.ANSI_RESET);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * used to close the connection
     * @throws SQLException If database access error occurs
     */
    public static void close() throws SQLException {
        connection.close();
        connection = null;
        System.err.println(Configuration.ANSI_BLUE + "Connection to SQLite has been closed." + Configuration.ANSI_RESET);
    }

    /**
     * used to force-connect
     */
    public static void connectIfAbsent() {
        if (getConnection() == null) {
            connect();
            System.out.println(Configuration.ANSI_BLUE + "Connection to SQLite has been established again." + Configuration.ANSI_RESET);
        }
    }

    /**
     * @return the connection
     */
    public static java.sql.Connection getConnection() {
        return connection;
    }

    /**
     * sets the connection
     *
     * @param connection connection
     */
    public static void setConnection(java.sql.Connection connection) {
        Connection.connection = connection;
    }
}