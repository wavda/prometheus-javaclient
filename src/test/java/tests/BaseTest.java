package tests;

import com.aventstack.extentreports.MediaEntityBuilder;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.exporter.PushGateway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExecutionWatcher;
import utils.ExtentTestManager;
import utils.Logger;
import utils.ScreenshotTaker;

import java.io.IOException;
import java.net.URL;

@ExtendWith(ExecutionWatcher.class)
public class BaseTest {
    public WebDriver driver;
    public WebDriverWait wait;
    public String testEnv;
    public String isRemote;
    public String fileName;

    static CollectorRegistry registry;
    static Counter requests;

    @BeforeAll
    public static void setUp() {
        registry = new CollectorRegistry();
        requests = Counter.build()
                .name("total_tests")
                .help("Number of tests run.")
                .register(registry);
    }

    @BeforeEach
    public void startTest(TestInfo testInfo) throws Exception {
        ExtentTestManager.startTest(testInfo.getDisplayName());
        String className = testInfo.getTestClass().toString();
        Logger.info(className);

        testEnv = System.getProperty("env", "prod");
        isRemote = System.getProperty("remote", "false");

        WebDriverManager.chromedriver().setup();
        driver = getDriver();
        wait = new WebDriverWait(driver, 60);
    }

    @AfterEach
    public void teardown(TestInfo testInfo) throws Exception {
        String filePath = "target/report/screenshot/";
        fileName = System.currentTimeMillis() + ".png";
        new ScreenshotTaker(driver).capturePage(filePath + fileName);

        ExtentTestManager.getTest().info("TEST COMPLETED: " + testInfo.getDisplayName(),
                MediaEntityBuilder.createScreenCaptureFromPath("screenshot/" + fileName).build());

        if (driver != null) {
            driver.quit();
        }

        requests.inc();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        PushGateway pg = new PushGateway("127.0.0.1:9091");
        pg.pushAdd(registry, "my_batch_job");
    }

    public WebDriver getDriver() throws Exception {
        WebDriverManager.chromedriver().setup();
        DesiredCapabilities dc = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1366,738");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");

        switch (isRemote.toUpperCase()) {
            case "FALSE":
                driver = new ChromeDriver(options);
                break;
            case "TRUE":
                dc.setCapability(ChromeOptions.CAPABILITY, options);
                dc.setBrowserName(BrowserType.CHROME);
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
            }

        return driver;
    }
}
