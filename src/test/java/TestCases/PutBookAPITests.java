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
        // Get the Singleton instance of RestAssuredClient
        RestAssuredClient.getInstance(); // Initializes RestAssured settings
    }

}
