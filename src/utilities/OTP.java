package utilities;

public class OTP {
    public static String getOTP() {
        int random = (int) Math.floor(Math.random() * 1000000);
        return String.valueOf(random);
    }
}
