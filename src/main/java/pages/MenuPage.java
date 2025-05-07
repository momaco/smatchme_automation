package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class MenuPage {
    private final WebDriver driver;

    private final By catalogOption = By.cssSelector("div[routerlink='/admin/catalogs'] div[class = 'btn-admin-label bg-strapp']");
    private final By challengesOption = By.cssSelector("div[routerlink='/admin/challenge'] div[class = 'btn-admin-label bg-strapp']");
    private final By postReportsOption = By.cssSelector("div[routerlink='/admin/post-report'] div[class = 'btn-admin-label bg-strapp']");
    private final  By notificationOption = By.cssSelector("div[routerlink='/admin/notification'] div[class = 'btn-admin-label bg-strapp']");
    private final By launchRegisterOption = By.cssSelector("div[routerlink='/admin/launch-register'] div[class = 'btn-admin-label bg-strapp']");
    private final By adsOption = By.cssSelector("div[routerlink='/admin/ads'] div[class = 'btn-admin-label bg-strapp']");
    private final  By userDashboardOption = By.cssSelector("div[routerlink='/admin/dashboard-users'] div[class = 'btn-admin-label bg-strapp']");
    private final By experiencesOption = By.cssSelector("div[routerlink='/admin/experiences'] div[class = 'btn-admin-label bg-strapp']");
    private final By companiesOptions = By.cssSelector("div[routerlink='/admin/companies'] div[class = 'btn-admin-label bg-strapp']");
    private final  By awardRequestOption = By.cssSelector("div[routerlink='/admin/award-request'] div[class = 'btn-admin-label bg-strapp']");
    private final By ordersOption = By.cssSelector("div[routerlink='/admin/order'] div[class = 'btn-admin-label bg-strapp']");
    private final By postOptions = By.cssSelector("div[routerlink='/admin/post'] div[class = 'btn-admin-label bg-strapp']");


    public MenuPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Select a menu option")
    public void selectAMenuOption(String menuOption) {
       Allure.step("Navigate to: " + menuOption);
       By optionToNavigate = switch (menuOption) {
           case "Catálogos" -> catalogOption;
           case "Retos" -> challengesOption;
           case "Reportes de posts" -> postReportsOption;
           case "Notificaciones" -> notificationOption;
           case "Registro del lanzamiento" -> launchRegisterOption;
           case "Anuncios" -> adsOption;
           case "Dashboard usuarios" -> userDashboardOption;
           case "Experiencias" -> experiencesOption;
           case "Empresas" -> companiesOptions;
           case "Solicitudes de premios" -> awardRequestOption;
           case "Órdenes" -> ordersOption;
           case "Publicaciones" -> postOptions;
           default -> null;
       };
       WaitUtils.waitForElementPresent(optionToNavigate);
       driver.findElement(optionToNavigate).click();
   }
}



