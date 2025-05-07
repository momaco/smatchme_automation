package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BasePage {
    private final WebDriver driver;



    public BasePage(WebDriver driver) {
        this.driver = driver;
    }
}
