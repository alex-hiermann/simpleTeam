package Utils.SQLite;

import Utils.BasicFunctionLibrary;
import Utils.Configuration;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Last updated by Alexander Hiermann on 06/01/2021
 * different templates used from sqlitetutorial.net
 */
public class SQLiteHandler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new BasicFunctionLibrary().createServerFolderStructure();
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
                	pk_message_id INT AUTOINCREMENT PRIMARY KEY NOT NULL,
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
                    fk_admin_id INT,
                    FOREIGN KEY (fk_admin_id) REFERENCES User (pk_user_id)
                );
                CREATE TABLE IF NOT EXISTS Task_User (
                    fk_pk_task_id INT NOT NULL,
                    fk_pk_user_id INT NOT NULL,
                    FOREIGN KEY (fk_pk_task_id) REFERENCES Task (pk_task_id),
                    FOREIGN KEY (fk_pk_user_id) REFERENCES User (pk_user_id)
                );
                """;

//        TODO Add table "Serverconfig" to the database structure, template:
//        CREATE TABLE IF NOT EXISTS Serverconfig (
//            pk_srvconf_id INT PRIMARY KEY NOT NULL,
//            userId INT,
//            teamId INT
//            ???
//            ???
//        );

        try (java.sql.Connection conn = DriverManager.getConnection(Configuration.DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("An error occurred during ");
        }
    }
}