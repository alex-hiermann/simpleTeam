package Utils;

import Client.User;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 * Last updated by Alexander Hiermann on 06/01/2021
 * @author Alexander Hiermann
 * @author Maximilian burger
 * @since 2021
 */
public class BasicFunctionLibrary {

    public static String findValueFromArgs(String forKey, String[] args) {
        for (String entry : args) {
            if (entry.split("=")[0].equals(forKey)) {
                return entry.split("=")[1].replaceAll("ꠦ", "").trim();
            }
        }
        return "";
    }

    /**
     * @param password Password
     * @return MD5 hash
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return String.format("%032x", new BigInteger(1, digest)).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User extractUserFromArgs(String[] args) {
        return new User(
                findValueFromArgs("username", args),
                findValueFromArgs("name", args),
                findValueFromArgs("lastname", args),
                findValueFromArgs("email", args),
                LocalDate.parse(findValueFromArgs("birth", args)),
                findValueFromArgs("password", args));
    }

    /**
     * This method is used to create all files needed for sqlite3
     */
    public void createServerFolderStructure() {
        try {
            if (!Files.exists(Paths.get(Configuration.ST_DIR_PATH + "java"))) {
                Files.createDirectories(Paths.get(Configuration.ST_DIR_PATH + "java"));
            }
            if (!Files.exists(Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\net\\sqlitetutorial\\"))) {
                Files.createDirectories(Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\net\\sqlitetutorial\\"));
            }
            if (!Files.exists(Paths.get(Configuration.ST_DIR_PATH + "db\\"))) {
                Files.createDirectories(Paths.get(Configuration.ST_DIR_PATH + "db\\"));
            }

            if (!Files.exists(Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\sqlite-jdbc-3.34.0.jar"))) {
                Files.copy(Paths.get(new File("resources/drivers/sqlite-jdbc-3.34.0.jar").getAbsolutePath()),
                        Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\sqlite-jdbc-3.34.0.jar"));
            }

            if (!Files.exists(Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\net\\sqlitetutorial\\Connection.class"))) {
                Files.copy(Paths.get(new File("out/production/simpleTeam/Utils/SQLite/Connection.class").getAbsolutePath()),
                        Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\net\\sqlitetutorial\\Connection.class"));
            }

            if (!Files.exists(Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\net\\sqlitetutorial\\Connection.java"))) {
                Files.copy(Paths.get(new File("src/Utils/SQLite/Connection.java").getAbsolutePath()),
                        Paths.get(Configuration.ST_DIR_PATH + "java\\connect\\net\\sqlitetutorial\\Connection.java"));
            }
        } catch (Exception e) {
            System.err.println("An error occurred while creating the Server-Folder-Structure:");
            e.printStackTrace();
        }
    }
}