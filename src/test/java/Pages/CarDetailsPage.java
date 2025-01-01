package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CarDetailsPage {
    WebDriver driver;
    WebDriverWait wait;

    public CarDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addComment(String comment) {
        driver.findElement(By.id("comment")).sendKeys(comment);
    }

    public void submitComment() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public void voteForCar() {
        driver.findElement(By.id("vote-button")).click();
    }


}