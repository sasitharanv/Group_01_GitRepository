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

public class PutBookAPITests {

    @BeforeClass
    public void setup() {
        // Initialize RestAssured settings via Singleton
        RestAssuredClient.getInstance();
    }

    @Test
    public void testUpdateBookWithValidDataAsAdmin() {
        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("admin");

        // Prepare valid book data
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", 1);
        bookData.put("title", "UpdatedBookTitle102");
        bookData.put("author", "UpdatedAuthor101");

        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(bookData)
                .put("/api/books/1");

        System.out.println("Response Status Code: " + response.getStatusCode());
        response.prettyPrint(); // Print response body

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
        Assert.assertEquals(response.jsonPath().getString("title"), "UpdatedBookTitle102", "Book title should be updated");
        Assert.assertEquals(response.jsonPath().getString("author"), "UpdatedAuthor101", "Book author should be updated");
    }

    // TC13: Update a Book with Valid Data (User)

    @Test
    public void testUpdateBookWithValidDataAsUser() {
        // Get credentials for regular user
        Map<String, String> user = UserFactory.getUser("user");

        // Prepare valid book data
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", 1);
        bookData.put("title", "Updated Book Title");
        bookData.put("author", "Updated Author");

        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .contentType("application/json")
                .body(bookData)
                .put("/api/books/1");

        Assert.assertEquals(response.getStatusCode(), 403, "Expected status code is 403");
        Assert.assertTrue(response.asString().contains("Forbidden"), "Response should indicate forbidden access");
    }

    //TC14: Update a Book with Non-existent ID (Admin)

    @Test
    public void testUpdateBookWithInvalidIdAsAdmin() {
        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("admin");

        // Prepare valid book data
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", 999); // Non-existent ID
        bookData.put("title", "Non-existent Book Title");
        bookData.put("author", "Non-existent Author");

        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(bookData)
                .put("/api/books/999");

        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code is 404");
        Assert.assertTrue(response.asString().contains("Book not found"), "Response should indicate book not found");

    }
    //Test Case 18: Update a book with missing author
    @Test
    public void testUpdateBookWithEmptyAuthor() {
        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("admin");

        // Prepare invalid data with another book's ID
        int bookId=2;
        Map<String, Object> invalidAuthorEmpty = new HashMap<>();
        invalidAuthorEmpty.put("id", bookId);
        invalidAuthorEmpty.put("title", "Valid Title3");
        invalidAuthorEmpty.put("author", "");

        // Send PUT request with empty author
        Response responseEmptyAuthor = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(invalidAuthorEmpty)
                .put("/api/books/{bookId}",bookId);

        Assert.assertEquals(responseEmptyAuthor.getStatusCode(), 400, "Expected status code is 400 for empty author");
    }

    @Test
    public void testUpdateBookWithAnotherBooksId() {

        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("admin");

        // Prepare invalid data with another book's ID
        Map<String, Object> invalidIdUpdate = new HashMap<>();
        invalidIdUpdate.put("id", 2);
        invalidIdUpdate.put("title", "Valid Title");
        invalidIdUpdate.put("author", "Valid Author");

        // Send PUT request
        Response responseInvalidId = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(invalidIdUpdate)
                .put("/api/books/1");

        Assert.assertEquals(responseInvalidId.getStatusCode(), 400, "Expected status code is 400 for updating ID");
        Assert.assertTrue(responseInvalidId.getBody().asString().contains("Book id is not matched"), "Error message should mention 'Book id is not matched'");
    }

    @Test
    public void testUpdateBookWithMissingTitleAndAuthor() {

        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("admin");

        // Prepare invalid data with null title and author
        Map<String, Object> missingTitleAuthor = new HashMap<>();
        missingTitleAuthor.put("id", 1);
        missingTitleAuthor.put("title", null);
        missingTitleAuthor.put("author", null);

        // Send PUT request
        Response responseMissingFields = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .contentType("application/json")
                .body(missingTitleAuthor)
                .put("/api/books/1");

        Assert.assertEquals(responseMissingFields.getStatusCode(), 400, "Expected status code is 400 for missing fields");
        Assert.assertTrue(responseMissingFields.getBody().asString().contains("Mandatory parameters should not be null"), "Error message should mention 'Mandatory parameters should not be null'");

    }
}