import java.security.MessageDigest;

/**
 * Hash is used to generate hashes for strings
 */
public class Hash {
    private static final String salt = "IdqFR4qOzG";

    /**
     * Get SHA-256 hash of password with salt using hexadecimal format
     *
     * @param password to be hashed
     * @return hexadecimal hash of password and salt
     */
    static public String getHash(String password) {
        password += salt;
        try {
            // Get hash of salted password in bytes
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuilder builder = new StringBuilder();
            // Convert hash to hexadecimal format
            for (byte b : digest) {
                String hex = Integer.toHexString((0xff & b));
                // toHexString removes leading zeros. This adds them back on
                if (hex.length() == 1) {
                    builder.append("0");
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (Exception ignored) {
            return "";
        }
    }
}