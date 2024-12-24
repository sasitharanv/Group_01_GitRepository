package Factory;

import java.util.HashMap;
import java.util.Map;

public class UserFactory {

    // Factory method to return user credentials based on the role
    public static Map<String, String> getUser(String role) {
        Map<String, String> userCredentials = new HashMap<>();

        switch (role.toLowerCase()) {
            case "admin":
                userCredentials.put("username", "admin");
                userCredentials.put("password", "password");
                break;
            case "user":
                userCredentials.put("username", "user");
                userCredentials.put("password", "password");
                break;
            default:
                throw new IllegalArgumentException("Role " + role + " is not supported.");
        }

        return userCredentials;
    }
}
