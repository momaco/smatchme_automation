import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import listener.JiraIssueListener;
import listener.PostJiraFailures;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.catalogs.DashboardPage;
import pages.catalogs.SportsPage;
import utils.ConfigReader;
import utils.ScreenshotUtil;
import utils.WaitUtils;


@Epic("Demo Automation")
@Listeners(JiraIssueListener.class)
public class DemoTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private MenuPage menuPage;
    private BreadcrumbsPage breadcrumbsPage;
    private CatalogsPage catalogsPage;
    private SportsPage sportsPage;
    private PostsPage postsPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = DriverFactory.initializerDriver();
        String baseUrl = ConfigReader.getProperty("baseUrl");
        driver.get(baseUrl);
        loginPage = new LoginPage(driver);
        menuPage = new MenuPage(driver);
        breadcrumbsPage = new BreadcrumbsPage(driver);
        catalogsPage = new CatalogsPage(driver);
        sportsPage = new SportsPage(driver);
        postsPage = new PostsPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Test(priority = 1)
    @Feature("Login")
    @Story("Success login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("SmatchMe login with valid credentials")
    public void testLogin() throws InterruptedException {
        loginPage.login("ngonzalez@strappcorp.com", "Sepaktakraw!!9");
        String message = loginPage.getSuccessMessage();
        assert message.contains("Ingreso correcto");
        loginPage.validateLoginSuccessMessageIsDismissed();
        loginPage.closeSession();
        String title = loginPage.getTitle();
        assert title.equalsIgnoreCase("Iniciar Sesión");
        Thread.sleep(2000);
    }

    @Test(priority = 2)
    @Feature("Login")
    @Story("Failed login")
    @Severity(SeverityLevel.NORMAL)
    @Description("SmatchMe login with not valid credentials")
    public void testLoginErrorMessage() {
        loginPage.login("xxxx", "xxx!");
        String message = loginPage.getErrorMessage();
        assert message.contains("Acceso incorrecto, revise sus credenciales");
        loginPage.validateLoginSuccessMessageIsDismissed();
    }

    @Test(priority = 3)
    @Feature("Login")
    @Story("Failed login")
    @Severity(SeverityLevel.NORMAL)
    @Description("SmatchMe login with not valid credentials")
    public void testLoginError() {
        loginPage.login("usuario no existe", "xxdfghh");
        String message = loginPage.getSuccessMessage();
        assert message.contains("Ingreso correcto");
    }

    @Test(priority = 4)
    @Feature("Menu Principal")
    @Severity(SeverityLevel.MINOR)
    @Description("Navigate through all options available in principal menu")
    public void testNavigationBetweenMenuOptions() throws InterruptedException {
        loginPage.login("ngonzalez@strappcorp.com", "Sepaktakraw!!9");
        String message = loginPage.getSuccessMessage();
        assert message.contains("Ingreso correcto");
        loginPage.validateLoginSuccessMessageIsDismissed();
        navigateAndBackIntoMenuOption("Catálogos");
        navigateAndBackIntoMenuOption("Retos");
        navigateAndBackIntoMenuOption("Reportes de posts");
        navigateAndBackIntoMenuOption("Notificaciones");
        navigateAndBackIntoMenuOption("Registro del lanzamiento");
        navigateAndBackIntoMenuOption("Anuncios");
        navigateAndBackIntoMenuOption("Dashboard usuarios");
        navigateAndBackIntoMenuOption("Experiencias");
        navigateAndBackIntoMenuOption("Empresas");
        navigateAndBackIntoMenuOption("Solicitudes de premios");
        navigateAndBackIntoMenuOption("Órdenes");
        navigateAndBackIntoMenuOption("Publicaciones");
    }

    private void navigateAndBackIntoMenuOption(String menuOption) throws InterruptedException {
        menuPage.selectAMenuOption(menuOption);
        Thread.sleep(2000);
        breadcrumbsPage.verifyUserIsInAMenuPage(menuOption);
        Thread.sleep(2000);
        breadcrumbsPage.goBack();
    }


    @Test(priority = 5)
    @Feature("Sports")
    @Severity(SeverityLevel.MINOR)
    @Description("Create a new sport")
    public void createANewSportOption() throws InterruptedException {
        loginPage.login("ngonzalez@strappcorp.com", "Sepaktakraw!!9");
        String message = loginPage.getSuccessMessage();
        assert message.contains("Ingreso correcto");
        loginPage.validateLoginSuccessMessageIsDismissed();
        menuPage.selectAMenuOption("Catálogos");
        Thread.sleep(2000);
        breadcrumbsPage.verifyUserIsInAMenuPage("Catálogos");
        Thread.sleep(2000);
        catalogsPage.selectSportsOption();
        Thread.sleep(2000);
        sportsPage.searchASport("Twerk");
        sportsPage.validateSportDoesNotExists();
        Thread.sleep(2000);
        sportsPage.clickInCreateSportButton();
        sportsPage.createASport("Twerk");
        Thread.sleep(2000);
        driver.navigate().refresh();
        sportsPage.searchASport("Twerk");
        sportsPage.deleteTwerk();
    }

    @Test(priority = 6)
    @Feature("Posts")
    @Severity(SeverityLevel.MINOR)
    @Description("Validate a Post")
    public void validateAPost() throws InterruptedException {
        loginPage.login("ngonzalez@strappcorp.com", "Sepaktakraw!!9");
        String message = loginPage.getSuccessMessage();
        assert message.contains("Ingreso correcto");
        loginPage.validateLoginSuccessMessageIsDismissed();
        menuPage.selectAMenuOption("Publicaciones");
        breadcrumbsPage.verifyUserIsInAMenuPage("Publicaciones");
        Thread.sleep(2000);
        postsPage.navigateToAPost();
        breadcrumbsPage.verifyUserIsInAMenuPage("Publicaciones");
    }

    @Test(priority = 7)
    @Feature("Language")
    @Severity(SeverityLevel.MINOR)
    @Description("Change the language")
    public void changeTheAppLanguage() throws InterruptedException {
        loginPage.login("ngonzalez@strappcorp.com", "Sepaktakraw!!9");
        String message = loginPage.getSuccessMessage();
        assert message.contains("Ingreso correcto");
        loginPage.validateLoginSuccessMessageIsDismissed();
        breadcrumbsPage.changeLanguageToEnglish();
        Thread.sleep(2000);
        loginPage.closeSession();
        loginPage.login("ngonzalez@strappcorp.com", "Sepaktakraw!!9");
        String englishMessage = loginPage.getSuccessMessage();
        assert englishMessage.contains("Success login");
        loginPage.validateLoginSuccessMessageIsDismissed();
    }

    @Test(priority = 8)
    @Feature("Dashboard de usuarios")
    @Severity(SeverityLevel.MINOR)
    @Description("Navigate through map")
    public void navigateThroughMap() throws InterruptedException {
        loginPage.login("ngonzalez@strappcorp.com", "Sepaktakraw!!9");
        String message = loginPage.getSuccessMessage();
        assert message.contains("Ingreso correcto");
        menuPage.selectAMenuOption("Dashboard usuarios");
        Thread.sleep(2000);
        breadcrumbsPage.verifyUserIsInAMenuPage("Dashboard usuarios");
        dashboardPage.verifyMapNavigation();
    }


    @AfterMethod
    public void tearDown(ITestResult result) {
        ScreenshotUtil.captureScreenshot(driver, result.getName());
        if (driver != null) {
            driver.quit();
        }
    }
}