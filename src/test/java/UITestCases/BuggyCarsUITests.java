package UITestCases;

import Pages.HomePage;
import Pages.LoginPage;
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

    //UITC1
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

    //UITC2
    @Test
    public void testLoginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterLogin("testuser123");
        loginPage.enterPassword("Password123!");
        loginPage.clickSubmit();

        Assert.assertTrue(driver.getPageSource().contains("Hi, Test"));
    }

    //UITC3
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

}


