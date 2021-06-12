package Utils.SQLite;

import Client.User;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;

import java.io.IOException;
import java.sql.*;

/**
 * Last updated by Alexander Hiermann on 06/01/2021
 * different templates used from sqlitetutorial.net
 */
public class SQLiteHandler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicFunctionLibrary.createServerFolderStructure();
        createNewDatabase();
        createDefaultTables();
        Connection.connect();
    }

    /**
     * Connection to a database
     */
    public static void createNewDatabase() {
        try (java.sql.Connection conn = DriverManager.getConnection(Configuration.DATABASE_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println(Configuration.ANSI_BLUE + "The driver name is " + meta.getDriverName() + ".");
                System.out.println("A new database has been created." + Configuration.ANSI_RESET);
            } else System.err.println(Configuration.ANSI_RED + "No connection found!" + Configuration.ANSI_RESET);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create all needed tables in a provided database
     */
    public static void createDefaultTables() {
        // SQL statement for creating a new table
        String sql = """
                /* SQLite for the Users */
                CREATE TABLE IF NOT EXISTS User (
                	pk_user_id INT PRIMARY KEY NOT NULL,
                	username CHAR(32),
                	name CHAR(32),
                	lastname CHAR(32),
                	email CHAR(64) NOT NULL,
                	birth DATE,
                	password CHAR(20) NOT NULL
                );
                /* SQLite for the Chatrooms and Messages */
                CREATE TABLE IF NOT EXISTS Chatroom (
                	pk_chatroom_id INT PRIMARY KEY NOT NULL
                );
                CREATE TABLE IF NOT EXISTS Message (
                	pk_message_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                	text TEXT NOT NULL,
                	date DATE NOT NULL,
                	fk_pk_user_id INT NOT NULL,
                	fk_pk_chatroom INT NOT NULL,
                	FOREIGN KEY (fk_pk_user_id) REFERENCES User (pk_user_id),
                    FOREIGN KEY (fk_pk_chatroom) REFERENCES Chatroom (pk_chatroom_id)
                );
                /* SQLite for the Teams */
                CREATE TABLE IF NOT EXISTS Team (
                	pk_team_id INT PRIMARY KEY NOT NULL,
                	name CHAR(32),
                	description CHAR(128),
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
                /* SQLite for the Tasks */
                CREATE TABLE IF NOT EXISTS Task (
                    pk_task_id INT PRIMARY KEY NOT NULL,
                    name CHAR(32),
                    note CHAR(128),
                    till_date DATE,
                    type CHAR(32) NOT NULL,
                    state CHAR(32) NOT NULL,
                    difficulty CHAR(32) NOT NULL,
                    team INT,
                    fk_team_id INT,
                    FOREIGN KEY (fk_team_id) REFERENCES Team (pk_team_id)
                );
                """;

//        TODO Add table "Serverconfig" to the database structure, template:
//        CREATE TABLE IF NOT EXISTS Serverconfig (
//            unique_user_id    UNSIGNED INT,
//            unique_team_id    UNSIGNED INT
//        );

        try (java.sql.Connection conn = DriverManager.getConnection(Configuration.DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("An error occurred during ");
        }
    }

    public static int retrieveUserID() {
        Connection.connectIfAbsent();
        String sql = "SELECT MAX(pk_user_id) AS 'USERID' FROM User";

        try {
            java.sql.Connection conn = Connection.connection;
            ResultSet rs = conn.createStatement().executeQuery(sql);
            int userid = rs.getInt("USERID");

            return userid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void closeConnection() {
        try {
            Connection.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addNewUserToDatabase(User user) {
        Connection.connectIfAbsent();
        String sql = "INSERT INTO User(pk_user_id, username, name, lastname, email, birth, password) VALUES (?,?,?,?,?,?,?)";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setDate(6, Date.valueOf(user.getBirth()));
            stmt.setString(7, user.getPassword());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}