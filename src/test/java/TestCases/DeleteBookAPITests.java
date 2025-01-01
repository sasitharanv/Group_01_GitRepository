package TestCases;

import Singleton.RestAssuredClient;
import org.testng.annotations.BeforeClass;

public class DeleteBookAPITests {
    @BeforeClass
    public void setup() {
        // Get the Singleton instance of RestAssuredClient
        RestAssuredClient.getInstance(); // Initializes RestAssured settings
    }
}
