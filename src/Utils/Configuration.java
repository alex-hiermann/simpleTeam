package Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class with global configurations used by multiple other classes
 * <p>
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class Configuration {

    /**
     * This is the default port which is used, if no other port is defined by the user
     */
    public static final int DEFAULT_PORT = 7274;

    /**
     * This regex is used to collect only relevant input for a valid invitation
     */
    public static final String CHECK_EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    /**
     * This regex is used to collect only relevant input for a valid ip and port
     */
    public static final String CHECK_IP_AND_PORT_REGEX = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):\\d+)|(localhost:\\d+)";

    /*
     * Variables used for the database structure following
     */
    /**
     * Path used to define the path for the database on the server
     */
    public static Path SERVER_DB_PATH = Paths.get("C:\\ProgramData\\simpleTeam\\db\\simpleTeam.db");

    /**
     * URL used to define the path for the database
     */
    public static final String DATABASE_URL = "jdbc:sqlite:" + SERVER_DB_PATH;

    /**
     * This path defines the directory path in general, where different things are saved
     */
    public static final String ST_DIR_PATH = "C:\\ProgramData\\simpleTeam\\";

    /*
     * Variables used in the SQLite Database following
     */
    /**
     * Unique user id counter. (Starts at 4, because of the test users!)
     */
    public static int userId = 4;

    /**
     * Unique team id counter
     */
    public static int teamId = 1;

    /**
     * Uniqze task id counter
     */
    public static int taskId = 0;

    /*
     * Variables used for colorful outputs following
     */
    /**
     * Color-code used to reset the color of the output
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Color-code used for black outputs
     */
    public static final String ANSI_BLACK = "\u001B[30m";

    /**
     * Color-code used for red outputs
     */
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * Color-code used for green outputs
     */
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * Color-code used for yellow outputs
     */
    public static final String ANSI_YELLOW = "\u001B[33m";

    /**
     * Color-code used for blue outputs
     */
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Color-code used for purple outputs
     */
    public static final String ANSI_PURPLE = "\u001B[35m";

    /**
     * Color-code used for cyan outputs
     */
    public static final String ANSI_CYAN = "\u001B[36m";

    /**
     * Color-code used for white outputs
     */
    public static final String ANSI_WHITE = "\u001B[37m";

    /*
     * Variables used to print lines on top of an existing line following
     */
    /**
     * provided by professor
     */
    public static final String CTR_BEFORE = new String(new byte[]{0x0b, 0x1b, '[', '1', 'A', 0x1b, '7', 0x1b, '[', '1', 'L', '\r'});

    /**
     * provided by professor
     */
    public static final String CTR_AFTER = new String(new byte[]{0x1b, '8', 0x1b, '[', '1', 'B'});
}