package pages;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.WaitUtils;

public class LoginPage {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    private final WebDriver driver;

    private final By usernameField = By.cssSelector("input[formcontrolname = 'username']");
    private final By passwordField = By.cssSelector("input[formcontrolname = 'password']");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By successMessage = By.cssSelector("div[class='ajs-message ajs-success ajs-visible']");
    private final By errorMessage = By.cssSelector("div[class='ajs-message ajs-error ajs-visible']");
    private final By closeSessionButton = By.cssSelector("button[class='btn']");
    private final By titleLogin = By.cssSelector("h2[class='text-primary-color-3']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
       WaitUtils waitUtils = new WaitUtils(driver);
    }

    @Step("Login using valid credentials")
    public void login(String username, String password) {
        Allure.step("Typing username");
        driver.findElement(usernameField).sendKeys(username);
        Allure.step("Typing password");
        driver.findElement(passwordField).sendKeys(password);
        Allure.step("Clicking enter");
        driver.findElement(loginButton).click();
    }

    @Step("Get success login message")
    public String getSuccessMessage(){
        Allure.step("Waiting for login message");
        WaitUtils.waitForElementPresent(successMessage);
        Allure.step("Getting login message text");
        return driver.findElement(successMessage).getText();
    }

    @Step("Validate login success message is dismissed")
    public void validateLoginSuccessMessageIsDismissed() {
        Allure.step("Waiting for login message dismissed");
        WaitUtils.waitForElementInvisible(successMessage);
    }

    public String getErrorMessage() {
        Allure.step("Waiting for login message");
        WaitUtils.waitForElementPresent(errorMessage);
        Allure.step("Getting login message text");
        return driver.findElement(errorMessage).getText();
    }

    public void closeSession(){
        Allure.step("Close session");
        driver.findElement(closeSessionButton).click();
    }

    public String getTitle() {
        Allure.step(("Get Title"));
        return  driver.findElement(titleLogin).getText();
    }
}