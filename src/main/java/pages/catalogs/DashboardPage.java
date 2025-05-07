package pages.catalogs;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;



public class DashboardPage {
    private final WebDriver driver;

    private final By mapLocator = By.cssSelector("div[aria-roledescription='map']");
    private final By successMessage = By.cssSelector("div[class='ajs-message ajs-success ajs-visible']");
    private final By mapControlButton = By.cssSelector("*[aria-label='Map camera controls']");
    private final By moveUP = By.cssSelector("*[aria-label='Move up']");
    private final By moveDown = By.cssSelector("[aria-label='Move down']");
    private final By zoomIn = By.cssSelector("[aria-label='Zoom in']");
    private final By zoomOut = By.cssSelector("[aria-label='Zoom out']");
    private final By moveLeft = By.cssSelector("[aria-label='Move left']");
    private final By moveRight = By.cssSelector("[aria-label='Move right']");


    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate using Maps")
    public void verifyMapNavigation() throws InterruptedException {
        Allure.step("Map Navigation");
        WaitUtils.waitForElementInvisible(successMessage);
        WaitUtils.waitForElementPresent(mapLocator);
        WaitUtils.waitForElementClickable(mapControlButton);
        driver.findElement(mapControlButton).click();
        WaitUtils.waitForElementClickable(moveUP);
        driver.findElement(zoomIn).click();
        driver.findElement(zoomIn).click();
        driver.findElement(zoomIn).click();
        driver.findElement(zoomIn).click();
        Thread.sleep(2000);
        driver.findElement(moveDown).click();
        driver.findElement(zoomIn).click();
        driver.findElement(moveLeft).click();
        driver.findElement(zoomIn).click();
        Thread.sleep(2000);
        driver.findElement(moveUP).click();
        driver.findElement(zoomIn).click();
        driver.findElement(zoomIn).click();
        driver.findElement(moveRight).click();
        Thread.sleep(3000);
    }
}
