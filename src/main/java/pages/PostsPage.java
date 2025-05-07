package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class PostsPage {

    private final WebDriver driver;

    private final By postToFind = By.xpath("//tr//td[contains(text(),'1636')]");
    private final By postToEdit = By.cssSelector("*[href='/admin/post/view/1636']");
    private final By postTitle = By.cssSelector("h5");
    private final By video = By.cssSelector("video[src]");
    private final By cancelButton = By.cssSelector("button[type= 'button']");
    private final By successMessage = By.cssSelector("div[class='ajs-message ajs-success ajs-visible']");



    public PostsPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Select a Post")
    public void navigateToAPost() throws InterruptedException {
        Allure.step("Navigate To a Post");
        WaitUtils.waitForElementPresent(postToFind);
        driver.findElement(postToEdit).click();
        WaitUtils.waitForElementPresent(postTitle);
        validateLoginSuccessMessageIsDismissed();
        WaitUtils.waitForElementPresent(video);
        Thread.sleep(5000);
        WaitUtils.waitForElementClickable(cancelButton);
        driver.findElement(cancelButton).click();
    }

    private void validateLoginSuccessMessageIsDismissed() {
        Allure.step("Waiting for login message dismissed");
        WaitUtils.waitForElementInvisible(successMessage);
    }

}
