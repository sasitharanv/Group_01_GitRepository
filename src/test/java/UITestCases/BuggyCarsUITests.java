package UITestCases;

import Pages.CarDetailsPage;
import Pages.HomePage;
import Pages.LoginPage;
import Pages.RegisterPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class BuggyCarsUITests extends BaseTest {

    @Test
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

    


}
