package Utils;

import Client.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Date;

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
                BasicFunctionLibrary.findValueFromArgs("username", args),
                BasicFunctionLibrary.findValueFromArgs("name", args),
                BasicFunctionLibrary.findValueFromArgs("lastname", args),
                BasicFunctionLibrary.findValueFromArgs("email", args),
                LocalDate.parse(BasicFunctionLibrary.findValueFromArgs("birth", args)),
                BasicFunctionLibrary.findValueFromArgs("password", args));
    }

}