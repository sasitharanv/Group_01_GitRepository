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
    public void testGetBookByIdAsUser() {
        // Get credentials for user
        Map<String, String> user = UserFactory.getUser("user");

        Response response = RestAssured.given()
                .auth().basic(user.get("username"), user.get("password"))
                .get("/api/books/1");

        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
        Assert.assertNotNull(response.jsonPath().get("title"), "Book title should not be null");
    }
}
