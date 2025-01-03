package TestCases;

import Factory.UserFactory;
import Singleton.RestAssuredClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class DeleteBookAPITests {

    @BeforeClass
    public void setup() {
        // Initialize RestAssured settings via Singleton
        RestAssuredClient.getInstance();
    }

    // TC22: Delete a book with valid ID (Admin)
    @Test
    public void testDeleteBookWithValidIdAsAdmin() {
        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("admin");

        // Delete a book with valid ID
        int validBookId = 16;

        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .delete("/api/books/" + validBookId);

        System.out.println("Response Status Code: " + response.getStatusCode());
        response.prettyPrint(); // Print response body

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200 for valid book deletion");
        Assert.assertTrue(response.asString().contains("Book deleted successfully"), "Response should indicate successful deletion");
    }

    // TC23: Delete a book with invalid/non-existent ID (Admin)
    @Test
    public void testDeleteBookWithInvalidId() {
        // Get credentials for admin user
        Map<String, String> admin = UserFactory.getUser("user");

        // Attempt to delete a book with an invalid/non-existent ID
        int invalidBookId = 999; // Non-existent ID

        Response response = RestAssured.given()
                .auth().basic(admin.get("username"), admin.get("password"))
                .delete("/api/books/" + invalidBookId);

        System.out.println("Response Status Code: " + response.getStatusCode());
        response.prettyPrint(); // Print response body

        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code is 404 for non-existent book deletion");
        Assert.assertTrue(response.asString().contains("Book not found"), "Response should indicate book not found");
    }

    // TC3: Delete a book (User)
    @Test
    public void testDeleteBookAsUser() {
        // Get credentials for regular user
        Map<String, String> user = UserFactory.getUser("user");

        // Attempt to delete a book as a regular user
        int bookId = 19;

        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .delete("/api/books/" + bookId);

        System.out.println("Response Status Code: " + response.getStatusCode());
        response.prettyPrint(); // Print response body

        Assert.assertEquals(response.getStatusCode(), 403, "Expected status code is 403 for forbidden access");
        Assert.assertTrue(response.asString().contains("Forbidden"), "Response should indicate forbidden access");
    }
}