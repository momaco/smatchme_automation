package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static WebDriver initializerDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/var/lib/jenkins/workspace/SC-AUTOMATION/chrome");
        WebDriver driver = new ChromeDriver(options);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manaseager_enabled", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--incognito");
        options.addArguments("--start-maximized");

        return new ChromeDriver(options);
    }
}
