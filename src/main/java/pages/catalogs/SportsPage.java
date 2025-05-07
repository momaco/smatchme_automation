package pages.catalogs;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SportsPage {

    private final WebDriver driver;

    private final By createSportButton = By.cssSelector("button[routerlink='/admin/catalogs/cat-sport/create']");
    private final By searchInput = By.cssSelector("*[class= 'pi pi-search'] + input[class='p-inputtext']");
    private final By tableResults = By.cssSelector("*[role ='grid'] [class = 'p-datatable-tbody'] tr");
    private final By sportsTitle = By.cssSelector("h5");
    private final By inputName = By.cssSelector("input[formcontrolname= 'name']");
    private final By inputNameEn = By.cssSelector("input[formcontrolname= 'name_en']");
    private final By selectCategoryOption = By.xpath("//select[@formcontrolname='cat_category_sport_id']/option[text()='BAILE']");
    private final By submitButton = By.cssSelector("*[type= 'submit']");
    private final By deleteButton = By.cssSelector("button[class = 'btn btn-default']:last-child");
    private final By successMessage = By.cssSelector("div[class = 'ajs-message ajs-success ajs-visible']");
    private final By deletePopUp = By.cssSelector("div[class = 'ajs-header']");
    private final By okDeleteButton = By.cssSelector("*[class = 'ajs-button ajs-ok']");
    private final By listResutls = By.xpath("//tr//td[contains(text(),'Twerk')]");



    public SportsPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Click in create sport")
    public void clickInCreateSportButton() {
        Allure.step("Click on create sport button");
        WaitUtils.waitForElementPresent(createSportButton);
        driver.findElement(createSportButton).click();
    }

    @Step("search a sport")
    public void searchASport(String sportToSearch) throws InterruptedException {
        Allure.step("Type sport");
        WaitUtils.waitForElementPresent(createSportButton);
        driver.findElement(searchInput).sendKeys(sportToSearch);
        Thread.sleep(2000);
    }

    @Step("validate sport does not exist")
    public void validateSportDoesNotExists() {
        Allure.step("validate sport does not exist");
        int tableSize = driver.findElements(tableResults).size();
       assertEquals(0, tableSize);
    }

    @Step("Validate sport does exist")
    public void validateSportDoesExists() {
        Allure.step("validate sport does exist");
        int tableSize = driver.findElements(tableResults).size();
        assertNotEquals(0, tableSize);
    }

    @Step("create a Sport")
    public void  createASport(String sport) throws InterruptedException {
        Allure.step("Typing name");
        WaitUtils.waitForElementPresent(sportsTitle);
        driver.findElement(inputName).sendKeys(sport);
        Allure.step("Typing name in english");
        driver.findElement(inputNameEn).sendKeys(sport);
        Allure.step("Select category");
        WaitUtils.waitForElementPresent(selectCategoryOption);
        WebElement option = driver.findElement(selectCategoryOption);
        option.click();
        Allure.step("Submit form");
        WaitUtils.waitForElementPresent(submitButton);
        driver.findElement(submitButton).click();
        WaitUtils.waitForElementPresent(createSportButton);
        Allure.step("Validate success message");
        WaitUtils.waitForElementPresent(successMessage);
        String message = driver.findElement(successMessage).getText();
        assert message.equals("Se ha guardado correctamente");
        WaitUtils.waitForElementInvisible(successMessage);
    }

    @Step("delete a sport")
    public void deleteTwerk(){
        Allure.step("Delete Twerk");
        WaitUtils.waitForElementPresent(listResutls);
        WaitUtils.waitForElementPresent(deleteButton);
        driver.findElement(deleteButton).click();
        WaitUtils.waitForElementPresent(deletePopUp);
        driver.findElement(okDeleteButton).click();
        WaitUtils.waitForElementPresent(successMessage);
        String message = driver.findElement(successMessage).getText();
        assertEquals("Elemento eliminado correctamente", message);
    }

}
