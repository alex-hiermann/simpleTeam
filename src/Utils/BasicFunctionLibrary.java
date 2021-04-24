package Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BasicFunctionLibrary {

    public static String findValueFromArgs(String forKey, String[] args) {
        for (String entry : args) {
            if (entry.split("=")[0].equals(forKey)) {
                return entry.split("=")[1].replaceAll("'", "");
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
            String hash = String.format("%032x", new BigInteger(1, digest)).toUpperCase();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
