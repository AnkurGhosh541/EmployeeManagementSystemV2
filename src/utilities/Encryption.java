package utilities;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    public static String getHashedString(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] b = md.digest(text.getBytes(StandardCharsets.UTF_8));

        BigInteger num = new BigInteger(1, b);
        StringBuilder sb = new StringBuilder(num.toString(16));

        while (sb.length() < 64) {
            sb.insert(0, "0");
        }

        return sb.toString();
    }
}
