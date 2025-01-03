package UITestCases;

import Pages.HomePage;
import Pages.LoginPage;
import Pages.RegisterPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

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

        loginPage.enterLogin("testuser123");
        loginPage.enterPassword("Password123!");
        loginPage.clickSubmit();

        Assert.assertTrue(driver.getPageSource().contains("Hi, Test"));
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

}


