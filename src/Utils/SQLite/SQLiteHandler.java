package Utils.SQLite;

import Client.Chat.Message;
import Client.Task;
import Client.Team;
import Client.User;
import Server.Server;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;

import java.sql.*;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
                /* SQLite for Messages */
                CREATE TABLE IF NOT EXISTS Message (
                	pk_message_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                	text TEXT NOT NULL,
                	date DATE NOT NULL,
                	fk_pk_user_id INT NOT NULL,
                	fk_pk_team INT NOT NULL,
                	FOREIGN KEY (fk_pk_user_id) REFERENCES User (pk_user_id),
                    FOREIGN KEY (fk_pk_team) REFERENCES Team (pk_team_id)
                );
                /* SQLite for the Teams */
                CREATE TABLE IF NOT EXISTS Team (
                	pk_team_id INT PRIMARY KEY NOT NULL,
                	name CHAR(32),
                	description CHAR(128),
                	fk_admin_id INT NOT NULL,
                	FOREIGN KEY (fk_admin_id) REFERENCES User (pk_user_id)
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

            return rs.getInt("USERID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int retrieveTeamId() {
        Connection.connectIfAbsent();
        String sql = "SELECT MAX(pk_team_id) AS 'TEAMID' FROM Team";

        try {
            java.sql.Connection conn = Connection.connection;
            ResultSet rs = conn.createStatement().executeQuery(sql);

            return rs.getInt("TEAMID");
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

    public static User retrieveUser(String email) {
        String sql = "SELECT * FROM User WHERE email LIKE ?";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return new User(
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getDate("birth").toLocalDate(),
                    rs.getString("password"),
                    rs.getInt("pk_user_id"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static User retrieveUser(int userId) {
        Connection.connectIfAbsent();
        String sql = "SELECT * FROM User WHERE pk_user_id = ?";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return new User(
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getDate("birth").toLocalDate(),
                    rs.getString("password"),
                    rs.getInt("pk_user_id"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public static void getAllUsers() {
        Connection.connectIfAbsent();
        String sql = "SELECT * FROM User";

        try {
            java.sql.Connection connection = Connection.connection;
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            Server.users.clear();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getDate("birth").toLocalDate(),
                        resultSet.getString("password"),
                        resultSet.getInt("pk_user_id"));

                List<Team> teams = Objects.requireNonNull(retrieveTeamsForUser(
                        resultSet.getInt("pk_user_id")))
                        .stream()
                        .map(SQLiteHandler::retrieveTeam)
                        .collect(Collectors.toList());
                user.myTeams.addAll(teams);

                Server.users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static LinkedList<Integer> retrieveTeamsForUser(int userId) {
        Connection.connectIfAbsent();
        String sql = "SELECT fk_pk_team_id FROM Team_User WHERE fk_pk_user_id = ?";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            LinkedList<Integer> returnList = new LinkedList<>();
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                returnList.add(rs.getInt("fk_pk_team_id"));
            }
            return returnList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static Team retrieveTeam(int teamId) {
        Connection.connectIfAbsent();
        String sql = "SELECT * FROM Team WHERE pk_team_id = ?";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            Team team = new Team(rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("pk_team_id"));
            team.setAdmin(retrieveUser(rs.getInt("fk_admin_id")));
            return team;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void addNewTeamToDatabase(Team team) {
        Connection.connectIfAbsent();
        String sql = "INSERT INTO Team(pk_team_id, name, description, fk_admin_id) VALUES (?,?,?,?)";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, team.getId());
            stmt.setString(2, team.getName());
            stmt.setString(3, team.getDescription());
            stmt.setInt(4, team.getAdmin().getId());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addUserToTeam(User user, Team team) {
        Connection.connectIfAbsent();
        String sql = "INSERT INTO Team_User(fk_pk_team_id, fk_pk_user_id) VALUES (?,?)";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, team.getId());
            stmt.setInt(2, user.getId());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void getAllTeams() {
        Connection.connectIfAbsent();
        String sql = "SELECT * FROM Team";

        try {

            java.sql.Connection connection = Connection.connection;
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            Server.teams.clear();
            while (resultSet.next()) {

                User admin = retrieveUser(resultSet.getInt("fk_admin_id"));

                Team team = new Team(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("pk_team_id"));
                team.setAdmin(admin);

                List<User> users = Objects.requireNonNull(retrieveUsersForTeam(
                        resultSet.getInt("pk_team_id")))
                        .stream()
                        .map(SQLiteHandler::retrieveUserForTeamId)
                        .collect(Collectors.toList());
                team.members.addAll(users);

                Server.teams.add(team);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static LinkedList<Integer> retrieveUsersForTeam(int teamId) {
        Connection.connectIfAbsent();
        String sql = "SELECT fk_pk_user_id FROM Team_User WHERE fk_pk_team_id = ?";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            LinkedList<Integer> returnList = new LinkedList<>();
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                returnList.add(rs.getInt("fk_pk_user_id"));
            }
            return returnList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static User retrieveUserForTeamId(int userId) {
        Connection.connectIfAbsent();
        String sql = "SELECT * FROM User WHERE pk_user_id = ?";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return new User(
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getDate("birth").toLocalDate(),
                    rs.getString("password"),
                    rs.getInt("pk_user_id"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public static void addNewMessageToDatabase(Message message, int teamId) {
        Connection.connectIfAbsent();
        String sql = "INSERT INTO Message(text, date, fk_pk_user_id, fk_pk_team) VALUES (?,?,?,?)";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, message.getText());
            stmt.setDate(2, Date.valueOf(message.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            stmt.setInt(3, message.getUser().getId());
            stmt.setInt(4, teamId);
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void getAllMessages() {
        Connection.connectIfAbsent();
        String sql = "SELECT * FROM Message";

        try {

            java.sql.Connection connection = Connection.connection;
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            Server.teams.forEach(l -> l.getChatroom().messages.clear());

            while (resultSet.next()) {

                User user = retrieveUser(resultSet.getInt("fk_pk_user_id"));

                Message message = new Message(
                        user,
                        resultSet.getString("text"),
                        resultSet.getTimestamp("date"));

                BasicFunctionLibrary.getEntryFromLinkedList(Server.teams,
                        new Team(resultSet.getInt("fk_pk_team")))
                        .getChatroom().addMessage(message);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addNewTaskToDatabase(Task task) {
        Connection.connectIfAbsent();
        String sql = "INSERT INTO Task(pk_task_id, " +
                "name, " +
                "note, " +
                "till_date, " +
                "type, " +
                "state, " +
                "difficulty, " +
                "fk_team_id, " +
                "fk_team_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            java.sql.Connection connection = Connection.connection;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, task.getTaskId());
            stmt.setString(2, task.getName());
            stmt.setString(3, task.getNote());
            stmt.setDate(4, Date.valueOf(task.getTill()));
            stmt.setString(5, task.getType().toString());
            stmt.setString(6, task.getState().toString());
            stmt.setString(7, task.getDifficulty().toString());
            stmt.setInt(8, task.getTeam_id());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void getAllTasks() {
        Connection.connectIfAbsent();
        String sql = "SELECT * FROM Task";

        try {

            java.sql.Connection connection = Connection.connection;
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            Server.teams.forEach(l -> l.tasks.clear());

            while (resultSet.next()) {

                User user = retrieveUser(resultSet.getInt("fk_user_id"));

                Task task = new Task(
                        resultSet.getString("name"),
                        resultSet.getString("note"),
                        resultSet.getDate("till_date").toLocalDate(),
                        BasicFunctionLibrary.extractTaskTypeFromText(resultSet.getString("type")),
                        BasicFunctionLibrary.extractTaskDifficultyFromText(resultSet.getString("difficulty"))
                );

                task.setUser(user);

                BasicFunctionLibrary.getEntryFromLinkedList(Server.teams,
                        new Team(resultSet.getInt("fk_team_id")))
                        .tasks.add(task);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}