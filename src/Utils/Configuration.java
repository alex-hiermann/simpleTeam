package Utils;

public class Configuration {

    /**
     * This is the default port which is used, if no other port is defined by the user
     */
    public final static int DEFAULT_PORT = 7274;

    /**
     * This regex is used to collect only relevant input for a valid invitation
     */
    public final static String INVITATION_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";


    /*
     * Variables used for the database structure following
     */
    /**
     * URL used to define the path for the database
     */
    public final static String DATABASE_URL = "jdbc:sqlite:C:/ProgramData/simpleTeam/db/simpleTeam.db";

    /**
     * This path defines the directory path in general, where different things are saved
     */
    public final static String ST_DIR_PATH = "C:/ProgramData/simpleTeam/";
}
