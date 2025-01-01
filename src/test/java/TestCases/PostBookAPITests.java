package TestCases;

import Factory.UserFactory;
import Singleton.RestAssuredClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class PostBookAPITests {

    @BeforeClass
    public void setup() {
        // Get the Singleton instance of RestAssuredClient
        RestAssuredClient.getInstance(); // Initializes RestAssured settings
    }

    @Test
    public void testCreateBookWithValidDataAsAdmin() {
        // Get admin credentials
        Map<String, String> admin = UserFactory.getUser("admin");

        // Request body with valid data
        String requestBody = "{ \"title\": \"Valid Books\", \"author\": \"Valid Authors\" }";

        // Make a POST request to create a book
        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(requestBody)
                .post("/api/books");

        // Assertions
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code is 201");
        Assert.assertNotNull(response.jsonPath().get("id"), "Book ID should not be null");
        Assert.assertEquals(response.jsonPath().get("title"), "Valid Books", "Book title should match");
        Assert.assertEquals(response.jsonPath().get("author"), "Valid Authors", "Book author should match");
    }

    @Test
    public void testCreateBookWithMissingTitleAsAdmin() {
        // Get admin credentials
        Map<String, String> admin = UserFactory.getUser("admin");

        // Request body with missing title
        String requestBody = "{ \"id\": 1, \"author\": \"Author Names\" }";

        // Make a POST request to create a book
        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(requestBody)
                .post("/api/books");

        // Assertions
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code is 400");
        Assert.assertEquals(response.jsonPath().getString("error"), "Invalid | Empty Input Parameters in the Request", "Expected error message for missing title");
    }

    @Test
    public void testCreateBookWithDuplicateIdAsAdmin() {
        // Get admin credentials
        Map<String, String> admin = UserFactory.getUser("admin");

        // First, create a book with ID 1
        String initialRequestBody = "{ \"id\": 1, \"title\": \"Existing Bok\", \"author\": \"Existing Author\" }";
        RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(initialRequestBody)
                .post("/api/books");

        // Attempt to create another book with the same ID
        String duplicateRequestBody = "{ \"id\": 1, \"title\": \"New Bok\", \"author\": \"New Author\" }";
        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(duplicateRequestBody)
                .post("/api/books");

        // Assertions
        Assert.assertEquals(response.getStatusCode(), 409, "Expected status code is 400");
        Assert.assertEquals(response.jsonPath().getString("error"), "Duplicate ID provided", "Expected error message for duplicate ID");
    }


}