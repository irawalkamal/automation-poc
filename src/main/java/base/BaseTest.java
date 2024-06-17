package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import db.LocatorsDAO;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reporting.ExtentManager;
import utils.ConfigReader;
import utils.FileManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class BaseTest {

    protected WebDriver driver;
    private Map<String, String> locators;
    protected ExtentReports extent;
    protected ExtentTest logger;

    @BeforeSuite
    public void setUp() throws IOException {
        FileManager.createDailyFolders();
    }

    @BeforeClass
    public void getDBDetails()
    {
        locators = LocatorsDAO.getLocators(this.getClass().getSimpleName());

        if (locators.isEmpty()) {
            throw new RuntimeException("Locators map is empty or null!");
        }
    }
    @BeforeMethod
    public void setup(){
        String browser = ConfigReader.getProperty("browser");
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            // Add other browser setups as needed
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        driver.manage().window().maximize();
        driver.get(ConfigReader.getProperty("url"));
        String dateSuffix = new SimpleDateFormat("yyyyMMdd").format(new Date());  // Current date in YYYYMMDD format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String reportPath = System.getProperty("user.dir") + "/reports/reports_"+dateSuffix+"/Report_" + timestamp + ".html";
        extent = ExtentManager.getInstance(reportPath);
        ExtentManager.setDriver(driver);

    }

    @BeforeMethod
    public void startTest(Method method) {
        String testName = method.getName();
        logger = ExtentManager.createTest(testName);
    }


    protected WebElement getElement(String elementName) {
        String locator = locators.get(elementName);
        if (locator == null) {
            throw new RuntimeException("Locator not found for element: " + elementName);
        }

        String[] locatorParts = locator.split("=", 2);
        String locatorType = locatorParts[0];
        String locatorValue = locatorParts[1];

        switch (locatorType) {
            case "id":
                return driver.findElement(By.id(locatorValue));
            case "name":
                return driver.findElement(By.name(locatorValue));
            case "xpath":
                return driver.findElement(By.xpath(locatorValue));
            case "css":
                return driver.findElement(By.cssSelector(locatorValue));
            case "className":
                return driver.findElement(By.className(locatorValue));
            case "tagName":
                return driver.findElement(By.tagName(locatorValue));
            case "linkText":
                return driver.findElement(By.linkText(locatorValue));
            case "partialLinkText":
                return driver.findElement(By.partialLinkText(locatorValue));
            default:
                throw new RuntimeException("Invalid locator type: " + locatorType);
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.skip(result.getThrowable());
        } else {
            logger.pass("Test passed");
        }
        ExtentManager.flushReport();
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }
}
