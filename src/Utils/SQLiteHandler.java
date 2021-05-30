package Utils;

import java.sql.*;

/**
 * @author sqlitetutorial.net
 */
public class SQLiteHandler {

    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:/ProgramFiles/simpleTeam/db/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create a new table in the test database
     */
    public static void createDefaultTables() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/ProgramFiles/simpleTeam/db/simpleTeam.db";

        // SQL statement for creating a new table
        String sql = """
                CREATE TABLE IF NOT EXISTS st_User (
                	pk_user_id INT PRIMARY KEY,
                	username CHAR(32) NOT NULL,
                	name CHAR(32) NOT NULL,
                	lastname CHAR(32) NOT NULL,
                	email CHAR(64) NOT NULL,
                	birth DATE NOT NULL,
                	password CHAR(20) NOT NULL
                );
                CREATE TABLE IF NOT EXISTS st_Team (
                	pk_team_id INT PRIMARY KEY,
                	name CHAR(32) NOT NULL,
                	description CHAR(128) NOT NULL,
                	fk_admin_id INT NOT NULL,
                	fk_chatroom_id INT NOT NULL
                );
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        createNewDatabase("simpleTeam.db");
        createDefaultTables();
    }
}