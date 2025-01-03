package UITestCases;

import Pages.HomePage;
import Pages.LoginPage;
import Pages.ProfilePage;
import Pages.RegisterPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class BuggyCarsUITests extends BaseTest {

    @Test
    @Description("Verify user registration with valid details.")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserRegistration() {
        HomePage homePage = new HomePage(driver);
        RegisterPage registerPage = new RegisterPage(driver);

        homePage.clickRegister();
        registerPage.enterUsername("testuser123");
        registerPage.enterFirstName("Test");
        registerPage.enterLastName("User");
        registerPage.enterPassword("Password123!");
        registerPage.enterConfirmPassword("Password123!");
        registerPage.submitRegistration();

        Assert.assertTrue(driver.getPageSource().contains("Registration is successful"));
    }

    @Test
    public void testLoginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterLogin("kirusanthanr");
        loginPage.enterPassword("Malathi75#");
        loginPage.clickSubmit();

        By welcomeMessageLocator = By.xpath("/html/body/my-app/header/nav/div/my-login/div/ul/li[1]/span");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait up to 10 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(welcomeMessageLocator));

        // Step 4: Assert that the welcome message is displayed
        boolean isWelcomeMessageDisplayed = driver.findElement(welcomeMessageLocator).isDisplayed();
        Assert.assertTrue(isWelcomeMessageDisplayed, "Welcome message is not displayed after login.");
    }


    //  Test Accessibility of All Links
    @Test
    public void testAccessibilityOfLinks() {
        driver.findElements(By.tagName("a")).forEach(link -> {
            Assert.assertTrue(link.isDisplayed(), "Link is not accessible: " + link.getText());
        });
    }

    @Test
    public void testMissingFieldsInRegistration() {
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String PageSource = driver.getPageSource();
        System.out.println("PageSource = " + PageSource);
        Assert.assertTrue(driver.getPageSource().contains("This field is required"));
    }

    //  Verify UI Responsiveness Across Viewports
    @Test
    public void testResponsiveUI() {
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1024, 768)); // Tablet size
        Assert.assertTrue(driver.getPageSource().contains("Buggy Cars Rating"));

        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667)); // Mobile size
        Assert.assertTrue(driver.getPageSource().contains("Buggy Cars Rating"));
    }

    @Test
    public void testDuplicateRegistration() {
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("username")).sendKeys("testuser123"); // Existing username
        driver.findElement(By.id("firstName")).sendKeys("Test");
        driver.findElement(By.id("lastName")).sendKeys("User");
        driver.findElement(By.id("password")).sendKeys("Password123!");
        driver.findElement(By.id("confirmPassword")).sendKeys("Password123!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Username already exists"));
    }

    @Test
    public void testWeakPasswordRegistration() {
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("username")).sendKeys("testuser456");
        driver.findElement(By.id("firstName")).sendKeys("Weak");
        driver.findElement(By.id("lastName")).sendKeys("Password");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.id("confirmPassword")).sendKeys("123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Password strength is too weak"));
    }

    @Test
    public void testProfilePageAccessibility() {
        testLoginWithValidCredentials();
        driver.findElement(By.linkText("Profile")).click();
        Assert.assertTrue(driver.getPageSource().contains("Your Profile"));
    }

    @Test
    public void testBrokenLinksOnHomepage() {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            Assert.assertNotNull(href, "Link is broken: " + link.getText());
        }
    }

    @Test
    public void testCommentCharacterLimit() {
        testLoginWithValidCredentials();
        driver.findElement(By.linkText("Overall Rating")).click();
        driver.findElement(By.linkText("Buggy Car Model")).click();
        driver.findElement(By.id("comment")).sendKeys("a".repeat(1001)); // Exceeding limit
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Comment exceeds maximum length"));
    }

    @Test
    public void testLogoutFunctionality() {
        HomePage homePage = new HomePage(driver);

        testLoginWithValidCredentials();
        homePage.clickProfile();
        driver.findElement(By.linkText("Logout")).click();

        Assert.assertTrue(driver.getPageSource().contains("You have been logged out"));
    }

    //  Verify Page Title
    @Test
    public void testPageTitle() {
        Assert.assertEquals(driver.getTitle(), "Buggy Cars Rating", "Page title is incorrect!");
    }
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
    private void log(String message) {
        System.out.println("[LOG] " + message);
    }
}


