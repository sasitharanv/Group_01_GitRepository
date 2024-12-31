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
}
