package UITestCases;

import Pages.HomePage;
import Pages.LoginPage;
import Pages.ProfilePage;
import Pages.RegisterPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class BuggyCarsUITests extends BaseTest {

    //UITC1
    @Test
    public void testSuccessfulRegistration() {
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);
        homePage.clickRegister();
        registerPage.enterUsername("sasitharan123");
        registerPage.enterFirstName("Test");
        registerPage.enterLastName("User");
        registerPage.enterPassword("Password123!");
        registerPage.enterConfirmPassword("Password123!");

        // Click the Register button
        driver.findElement(By.cssSelector("button.btn.btn-default[type='submit']")).click();

        // Wait for the success message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/my-app/div/main/my-register/div/div/form/div[6]")));

        // Verify success message
        Assert.assertTrue(successMessage.isDisplayed(), "Success message is not displayed!");
        Assert.assertEquals(successMessage.getText(), "Registration is successful");
    }

    //UITC2
    @Test
    public void testLoginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterLogin("sasvaratharasa540@gmail.com");
        loginPage.enterPassword("Malathi75$");
        loginPage.clickSubmit();

        By welcomeMessageLocator = By.xpath("/html/body/my-app/header/nav/div/my-login/div/ul/li[1]/span");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait up to 10 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(welcomeMessageLocator));

        // Step 4: Assert that the welcome message is displayed
        boolean isWelcomeMessageDisplayed = driver.findElement(welcomeMessageLocator).isDisplayed();
        Assert.assertTrue(isWelcomeMessageDisplayed, "Welcome message is not displayed after login.");
    }

    //UITC11
    //  Test Accessibility of All Links
    @Test
    public void testAccessibilityOfLinks() {
        driver.findElements(By.tagName("a")).forEach(link -> {
            Assert.assertTrue(link.isDisplayed(), "Link is not accessible: " + link.getText());
        });
    }

    //UITC4
    @Test
    public void testMissingFieldsInRegistration() {
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String PageSource = driver.getPageSource();
        System.out.println("PageSource = " + PageSource);
        Assert.assertTrue(driver.getPageSource().contains("This field is required"));
    }

    //UITC5
    //  Verify UI Responsiveness Across Viewports
    @Test
    public void testResponsiveUI() {
        HomePage homePage = new HomePage(driver);

        // Test for tablet screen size
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1024, 768)); // Tablet size
        Assert.assertTrue(driver.getPageSource().contains("Buggy Cars Rating"), "Buggy Cars Rating is not displayed in tablet view!");

        // Test for mobile screen size
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667)); // Mobile size
        Assert.assertTrue(driver.getPageSource().contains("Buggy Cars Rating"), "Buggy Cars Rating is not displayed in mobile view!");
    }

    //UITC6
    //verify the Registration with existing username
    @Test
    public void testunSuccessfulRegistrationWhileExistingUsername() {
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        homePage.clickRegister();
        registerPage.enterUsername("testuser123");
        registerPage.enterFirstName("Test");
        registerPage.enterLastName("User");
        registerPage.enterPassword("Password123!");
        registerPage.enterConfirmPassword("Password123!");

        // Click the Register button
        driver.findElement(By.cssSelector("button.btn.btn-default[type='submit']")).click();

        // Wait for the success message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/my-app/div/main/my-register/div/div/form/div[6]")));

        // Verify success message
        Assert.assertTrue(successMessage.isDisplayed(), "Success message is not displayed!");
        Assert.assertEquals(successMessage.getText(), "UsernameExistsException: User already exists");
    }

    // UITC7
// Verify registration with a weak password
    @Test
    public void testWeakPasswordRegistration() {
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        homePage.clickRegister();
        registerPage.enterUsername("testuser456");
        registerPage.enterFirstName("Weak");
        registerPage.enterLastName("Password");
        registerPage.enterPassword("123456");
        registerPage.enterConfirmPassword("123456");

        // Click the Register button
        driver.findElement(By.cssSelector("button.btn.btn-default[type='submit']")).click();

        // Wait for the error message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/my-app/div/main/my-register/div/div/form/div[6]")));

        // Verify error message
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message is not displayed!");
        Assert.assertEquals(errorMessage.getText(), "InvalidPasswordException: Password did not conform with policy: Password not long enough");
    }

    // UITC8
    @Test
    public void testProfilePageAccessibility() {
        testLoginWithValidCredentials();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/my-app/header/nav/div/my-login/div/ul/li[1]/span")));
        profileLink.click();
        WebElement greetingText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Hi, Varatharasa')]")));
        Assert.assertTrue(greetingText.isDisplayed(), "Greeting text 'Hi, Varatharasa' is not displayed!");
        Assert.assertEquals(greetingText.getText(), "Hi, Varatharasa", "Greeting text does not match the expected value!");
    }

    // UITC9
    @Test
    public void testBrokenLinksOnHomepage() {
        // Fetch all anchor tags from the homepage
        List<WebElement> links = driver.findElements(By.tagName("a"));

        // Iterate through each link and verify it is not broken
        for (WebElement link : links) {
            String href = link.getAttribute("href");

            // Assert that the href attribute is not null
            Assert.assertNotNull(href, "Broken link found: " + (link.getText().isEmpty() ? "Unnamed link" : link.getText()));

            // Optionally, you can log each link for debugging
            System.out.println("Verified link: " + href);
        }
    }

    // UITC10
    @Test
    public void testCommentCharacterLimit() {
        testLoginWithValidCredentials();
        driver.findElement(By.linkText("Overall Rating")).click();
        System.out.println("ryun");
        driver.findElement(By.linkText("Buggy Car Model")).click();
        System.out.println("ryun");
        driver.findElement(By.id("comment")).sendKeys("a".repeat(1001)); // Exceeding limit
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Comment exceeds maximum length"));
    }

    // UIT11
    @Test
    public void testLogoutFunctionality() {
        // Instantiate the HomePage
        HomePage homePage = new HomePage(driver);
        // Log in with valid credentials
        testLoginWithValidCredentials();

        // Click on the profile and logout
        homePage.clickProfile();
        driver.findElement(By.linkText("Logout")).click();

        // Wait for the Login button to become visible after logging out
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/my-app/header/nav/div/my-login/div/form/button")));

        // Assert that the Login button is visible
        Assert.assertTrue(loginButton.isDisplayed(), "The Login button is not visible after logging out!");

        // Verify that the button text is "Login"
        Assert.assertEquals(loginButton.getText().trim(), "Login", "The button text is not as expected after logging out!");
    }

    // UITC12
    //  Verify Page Title
    @Test
    public void testPageTitle() {
        Assert.assertEquals(driver.getTitle(), "Buggy Cars Rating", "Page title is incorrect!");
    }

    // UITC13
    @Test
    public void testChangePasswordSuccessfully() {
        // Step 1: Log in with valid credentials
        log("Logging in with valid credentials...");
        testLoginWithValidCredentials();

        // Step 2: Navigate to the profile page
        log("Navigating to the profile page...");
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.clickProfile();

        // Step 3: Fill the change password form
        log("Filling the change password form...");
        profilePage.fillChangePasswordForm("Malathi75@", "Malathi75#", "Malathi75#");

        // Step 4: Wait for the success message to appear
        log("Waiting for the success message to appear...");

        // Use a more reliable XPath for the success message
        By successMessageLocator = By.xpath("//div[contains(@class, 'alert-success') and contains(text(), 'The profile has been saved successful')]");

        // Wait for the success message to be visible (not just present in the DOM)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Wait up to 20 seconds
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageLocator));

        // Step 5: Get the text of the success message
        log("Retrieving the success message text...");
        String successMessageText = successMessage.getText();
        log("Success message text: " + successMessageText);

        // Step 6: Assert that the success message is displayed and contains the expected text
        log("Asserting the success message...");
        String expectedMessage = "The profile has been saved successful"; // Replace with the expected message
        Assert.assertTrue(successMessageText.contains(expectedMessage), "Success message does not match the expected text.");

        // Step 7: Log the result
        log("Test completed successfully. Password changed and success message verified.");
    }

    // UITC14
    @Test
    public void testChangePasswordWithInvalidCurrentPassword() {
        // Step 1: Log in with valid credentials
        log("Logging in with valid credentials...");
        testLoginWithValidCredentials();

        // Step 2: Navigate to the profile page
        log("Navigating to the profile page...");
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.clickProfile();

        // Step 3: Fill the change password form with invalid current password
        log("Filling the change password form with invalid current password...");
        profilePage.fillChangePasswordForm("faultrest", "Malathi75#", "Malathi75#");

        // Step 4: Wait for the failure message to appear
        log("Waiting for the failure message to appear...");

        // Use a reliable XPath for the failure message
        By failureMessageLocator = By.xpath("//div[contains(@class, 'alert-danger') and contains(text(), 'Incorrect')]");

        // Wait for the failure message to be visible (not just present in the DOM)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50)); // Wait up to 20 seconds
        WebElement failureMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(failureMessageLocator));

        // Step 5: Get the text of the failure message
        log("Retrieving the failure message text...");
        String failureMessageText = failureMessage.getText();
        log("Failure message text: " + failureMessageText);

        // Step 6: Assert that the failure message is displayed and contains the expected text
        log("Asserting the failure message...");
        String expectedMessage = "NotAuthorizedException: Incorrect username or password"; // Replace with the expected message
        Assert.assertTrue(failureMessageText.contains(expectedMessage), "Failure message does not match the expected text.");

        // Step 7: Log the result
        log("Test completed successfully. Importance of current password double-checked.");
    }

    //UITC12
    @Test
    public void viewListOffAllRegisteredVehicles() {
        // Wait for the image element to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement imageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@href='/overall']/img")
        ));

        // Click on the image
        imageElement.click();

        // Wait for the page to load and URL to update
        wait.until(ExpectedConditions.urlToBe("https://buggy.justtestit.org/overall"));

        // Verify the URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://buggy.justtestit.org/overall", "Navigation to the Overall Rating page failed!");
    }

    private void log(String message) {
        System.out.println("[LOG] " + message);
    }
}


