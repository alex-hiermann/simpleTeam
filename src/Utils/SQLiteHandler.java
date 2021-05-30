package Utils;

import java.sql.*;

/**
 * template used from sqlitetutorial.net
 */
public class SQLiteHandler {

    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
    public static void createNewDatabase(String fileName) {
        try (Connection conn = DriverManager.getConnection(Configuration.ST_DIR_PATH + fileName)) {
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
        // SQL statement for creating a new table
        String sql = """
                CREATE TABLE IF NOT EXISTS User (
                	pk_user_id INT PRIMARY KEY NOT NULL,
                	username CHAR(32) NOT NULL,
                	name CHAR(32) NOT NULL,
                	lastname CHAR(32) NOT NULL,
                	email CHAR(64) NOT NULL,
                	birth DATE NOT NULL,
                	password CHAR(20) NOT NULL
                );
                CREATE TABLE IF NOT EXISTS Chatroom (
                	pk_chatroom_id INT PRIMARY KEY NOT NULL
                );
                CREATE TABLE IF NOT EXISTS Message (
                	pk_message_id INT PRIMARY KEY NOT NULL,
                	text TEXT NOT NULL,
                	date DATE NOT NULL,
                	fk_pk_user_id INT NOT NULL,
                	fk_pk_chatroom INT NOT NULL,
                	FOREIGN KEY (fk_pk_user_id) REFERENCES User (pk_user_id),
                    FOREIGN KEY (fk_pk_chatroom) REFERENCES Chatroom (pk_chatroom_id)
                );
                CREATE TABLE IF NOT EXISTS Team (
                	pk_team_id INT PRIMARY KEY NOT NULL,
                	name CHAR(32) NOT NULL,
                	description CHAR(128) NOT NULL,
                	fk_admin_id INT NOT NULL,
                	fk_pk_chatroom_id INT NOT NULL,
                	FOREIGN KEY (fk_admin_id) REFERENCES User (pk_user_id),
                    FOREIGN KEY (fk_pk_chatroom_id) REFERENCES Chatroom (pk_chatroom_id)
                );
                CREATE TABLE IF NOT EXISTS Team_User (
                    fk_pk_team_id INT NOT NULL,
                    fk_pk_user_id INT NOT NULL,
                    FOREIGN KEY (fk_pk_team_id) REFERENCES Team (pk_team_id),
                    FOREIGN KEY (fk_pk_user_id) REFERENCES User (pk_user_id)
                );
                """;

//        TODO Add table "Task" to the database structure, template:
//
//        CREATE TABLE IF NOT EXISTS Task (
//                pk_task_id INT PRIMARY KEY NOT NULL,
//                name CHAR(32) NOT NULL,
//        description CHAR(128) NOT NULL,
//        fk_admin_id INT NOT NULL,
//                );
//
//        TODO Add table for the connection between "Task" and "User" to the database structure, template:
//
//        CREATE TABLE IF NOT EXISTS Task_User (
//                fk_pk_task_id INT NOT NULL,
//                fk_pk_user_id INT NOT NULL,
//                );
//
//        TODO Add table "Serverconfig" to the database structure, template:
//
//        CREATE TABLE IF NOT EXISTS Serverconfig (
//                pk_srvconf_id INT PRIMARY KEY NOT NULL,
//                ???
//                ???
//                ???
//                ???
//                );
//
        try (Connection conn = DriverManager.getConnection(Configuration.DATABASE_URL);
             Statement stmt = conn.createStatement()) {
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