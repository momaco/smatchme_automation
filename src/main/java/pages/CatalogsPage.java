package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class CatalogsPage {
    private final WebDriver driver;

    private final By sportOption = By.cssSelector("div[routerlink='/admin/catalogs/cat-sport'] div[class= 'btn-admin-label bg-strapp']");

    public CatalogsPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Select a menu option")
    public void selectSportsOption() {
        WaitUtils.waitForElementPresent(sportOption);
        driver.findElement(sportOption).click();
    }

}
