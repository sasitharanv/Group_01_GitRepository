package TestCases;

import Factory.UserFactory;
import Singleton.RestAssuredClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PostBookAPITests {

    @BeforeClass
    public void setup() {
        // Get the Singleton instance of RestAssuredClient
        RestAssuredClient.getInstance(); // Initializes RestAssured settings
    }
    @Test
    public void testCreateBookWithValidData() {
        Map<String, String> user = UserFactory.getUser("user");

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", 1);
        bookData.put("title", "Test Book");
        bookData.put("author", "Test Author");

        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .contentType("application/json")
                .body(bookData)
                .post("/api/books");

        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code is 201");
        Assert.assertEquals(response.jsonPath().getInt("id"), 1, "Book ID should match");
        Assert.assertEquals(response.jsonPath().getString("title"), "Test Book", "Book title should match");
        Assert.assertEquals(response.jsonPath().getString("author"), "Test Author", "Book author should match");
    }

    @Test
    public void testCreateBookWithMissingAuthor() {

        Map<String, String> user = UserFactory.getUser("user");

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", 1);
        bookData.put("title", "Test Book2");
        bookData.put("author", null);

        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .contentType("application/json")
                .body(bookData)
                .post("/api/books");

        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code is 400");
        Assert.assertTrue(response.getBody().asString().contains("Author is a mandatory parameter"),
                "Error message should indicate missing author");
    }

    @Test
    public void testCreateBookWithDuplicateTitle() {
        Map<String, String> admin = UserFactory.getUser("admin");

        // Create initial book
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", 1);
        bookData.put("title", "Test Book");
        bookData.put("author", "Original Author");

        RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(bookData)
                .post("/api/books");

        // Attempt to create a duplicate book
        bookData.put("author", "New Author"); // Change author but keep title

        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(bookData)
                .post("/api/books");

        // Verify response
        Assert.assertEquals(response.getStatusCode(), 208, "Expected status code is 208");
        Assert.assertTrue(response.getBody().asString().contains("Book Already Exists"),
                "Error message should indicate Book Already Exists");
    }

}