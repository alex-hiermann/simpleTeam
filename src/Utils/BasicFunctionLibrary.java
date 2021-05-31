package Utils;

import Client.User;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public class BasicFunctionLibrary {

    public static String findValueFromArgs(String forKey, String[] args) {
        for (String entry : args) {
            if (entry.split("=")[0].equals(forKey)) {
                return entry.split("=")[1].replaceAll("'", "").trim();
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
    protected void createFolderStructure() {
        try {
            File javaDir = new File(Configuration.ST_DIR_PATH + "java/");
            if (javaDir.mkdir()) {
                System.out.println("Directory created: " + javaDir.getAbsolutePath());
            } else {
                System.out.println("Directory already exists.");
            }

            File connectDirs = new File(Configuration.ST_DIR_PATH + "java/connect/net/sqlitetutorial");
            if (connectDirs.mkdir()) {
                System.out.println("Directory created: " + connectDirs.getAbsolutePath());
            } else {
                System.out.println("Directory already exists.");
            }

            Files.copy(Path.of(String.valueOf(this.getClass().getResource("/drivers/sqlite-jdbc-3.34.0.jar"))),
                    Path.of(Configuration.ST_DIR_PATH + "java/connect/sqlite-jdbc-3.34.0.jar"));

        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
    }
}