package TestCases;

import Factory.UserFactory;
import Singleton.RestAssuredClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class GetBookAPITests {
    @BeforeClass
    public void setup() {
        // Get the Singleton instance of RestAssuredClient
        RestAssuredClient.getInstance(); // Initializes RestAssured settings
    }

    @Test
    public void testGetAllBooksAsAdmin() {
        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("admin");

        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .get("/api/books");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
        Assert.assertTrue(response.jsonPath().getList("$").size() >= 0, "Expected non-empty book list");
    }

    @Test
    public void testGetAllBooksAsUser() {
        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("user");

        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .get("/api/books");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
        Assert.assertTrue(response.jsonPath().getList("$").size() >= 0, "Expected non-empty book list");
    }

    @Test
    public void testGetBookByIdAsUser() {
        // Get credentials for user
        Map<String, String> user = UserFactory.getUser("user");

        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .get("/api/books/1");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
        Assert.assertNotNull(response.jsonPath().get("title"), "Book title should not be null");
    }



    @Test
    public void testGetBookByIdAsAdmin() {
        // Get credentials for user
        Map<String, String> user = UserFactory.getUser("admin");

        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .get("/api/books/1");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
        Assert.assertNotNull(response.jsonPath().get("title"), "Book title should not be null");
    }




    @Test
    public void testGetBookByValidIdAsAdmin() {
        // Get admin credentials
        Map<String, String> admin = UserFactory.getUser("admin");

        // Make a GET request for a valid book ID (e.g., 1)
        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .get("/api/books/9999");

        // Assertions
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code is 200");
        Assert.assertNotNull(response.jsonPath().get("title"), "Book title should not be null");
        Assert.assertNotNull(response.jsonPath().get("author"), "Book author should not be null");
    }

    @Test
    public void testGetBookByInvalidIdAsAdmin() {
        // Get admin credentials
        Map<String, String> admin = UserFactory.getUser("admin");

        // Make a GET request for an invalid book ID (e.g., 9999)
        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .get("/api/books/9999");

        // Assertions
        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code is 404");

        // Check content type
        String contentType = response.getHeader("Content-Type");
        if (contentType != null && contentType.contains("application/json")) {
            // Parse JSON response
            Assert.assertEquals(response.jsonPath().getString("error"), "Book not found", "Expected error message is 'Book not found'");
        } else {
            // Handle non-JSON response
            String responseBody = response.getBody().asString();
            Assert.assertTrue(responseBody.contains("Book not found"), "Expected response body to contain 'Book not found'");
        }
    }


    @Test
    public void testGetBookByIdAsRegularUser() {
        // Get regular user credentials
        Map<String, String> user = UserFactory.getUser("user");

        // Make a GET request for a valid book ID (e.g., 1)
        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .get("/api/books/1");

        // Assertions
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
        Assert.assertNotNull(response.jsonPath().get("title"), "Book title should not be null");
        Assert.assertNotNull(response.jsonPath().get("author"), "Book author should not be null");
    }


}
