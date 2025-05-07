package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class BreadcrumbsPage {
    private final WebDriver driver;

    private final By homeOption = By.cssSelector("nav[aria-label = 'breadcrumb'] ol li[class = 'breadcrumb-item'] a[routerlink='/admin']");
    private final By menuOption = By.cssSelector("li[class = 'breadcrumb-item'] + [aria-current = 'page']");
    private final By englishSpan = By.xpath("//nav//span[text()='Inglés']");
    private final By spanishSpan = By.xpath("//nav//span[text()='Spanish']");
    private final By exitButton = By.cssSelector("button[class='btn']");

    public BreadcrumbsPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Go back to principal menu")
    public void goBack() {
        Allure.step("Go back to principal Menu");
        driver.findElement(homeOption).click();

    }
    @Step("Verify the user is in a menu page")
    public void verifyUserIsInAMenuPage(String pageSelected){
        Allure.step("Verify user is in a " + pageSelected + " page");
        WaitUtils.waitForElementPresent(homeOption);
        String titleDisplayed = driver.findElement(menuOption).getText();
        assert titleDisplayed.equalsIgnoreCase(pageSelected);
    }

    @Step("Change language to english")
    public void changeLanguageToEnglish(){
        Allure.step("Change the language to English");
        WaitUtils.waitForElementPresent(englishSpan);
        driver.findElement(englishSpan).click();
        WaitUtils.waitForElementPresent(spanishSpan);
    }
}
