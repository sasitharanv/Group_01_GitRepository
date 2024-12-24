package Singleton;

import io.restassured.RestAssured;

public class RestAssuredClient {

    private static RestAssuredClient instance;

    // Private constructor to prevent instantiation
    private RestAssuredClient() {
        // Set base URI for the RestAssured client
        RestAssured.baseURI = "http://localhost:7081";  // Replace with your base URL
    }

    // Public method to provide access to the Singleton instance
    public static RestAssuredClient getInstance() {
        if (instance == null) {
            synchronized (RestAssuredClient.class) {
                if (instance == null) {
                    instance = new RestAssuredClient();
                }
            }
        }
        return instance;
    }

    // Optionally, add other RestAssured configurations like headers, authentication, etc.
    public void setupAuthentication(String username, String password) {
        RestAssured.authentication = RestAssured.basic(username, password);
    }
}
