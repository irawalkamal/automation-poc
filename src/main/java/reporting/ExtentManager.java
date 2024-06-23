package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {
    private static ExtentReports extent;
    private static ExtentSparkReporter htmlReporter;

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static WebDriver driver;

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }
    public static synchronized ExtentReports getInstance(String reportPath) {
        if (extent == null) {
            extent = new ExtentReports();
            htmlReporter = new ExtentSparkReporter(reportPath);
            extent.attachReporter(htmlReporter);
        }
        return extent;
    }

    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    public static synchronized ExtentTest  createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
        return test;
    }

    public static synchronized ExtentTest getTest() {
        return extentTest.get();
    }

    public static synchronized void logTestStep(String stepName, String details) {
        ExtentTest test = extentTest.get();
        if (test == null) {
            throw new IllegalStateException("ExtentTest has not been initialized. Call setExtentTest before logging steps.");
        }

        String screenshotPath = captureScreenshot(stepName);
        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build();
        String stepDetails = stepName + ": " + details;

        if (!screenshotPath.isEmpty()) {
            test.info("<a href='../../" + screenshotPath + "'>"+stepDetails+"</a>");
        } else {
            test.info(stepDetails);
        }
    }

    public static synchronized void logTestFailure(String stepName, String errorMessage) {
        ExtentTest test = extentTest.get();
        if (test == null) {
            throw new IllegalStateException("ExtentTest has not been initialized. Call setExtentTest before logging steps.");
        }

        String screenshotPath = captureScreenshot(stepName + "_Failure");
        String stepDetails = "FAIL: " + stepName + ": " + errorMessage;

        if (!screenshotPath.isEmpty()) {
            test.fail("<a href='../../" + screenshotPath + "'>" + stepDetails + "</a>");
        } else {
            test.fail(stepDetails);
        }
    }

    public static synchronized void handleException(Exception e) {
        String stepName = Thread.currentThread().getStackTrace()[2].getMethodName();
        logTestFailure(stepName, e.getMessage());
        throw new RuntimeException(e);
    }
    public static synchronized void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    private static String captureScreenshot(String stepName) {
        if (driver == null) {
            return "";
        }
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String dateSuffix = new SimpleDateFormat("yyyyMMdd").format(new Date());  // Current date in YYYYMMDD format
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = System.getProperty("user.dir") + "/screenshots/screenshots_"+dateSuffix;
        String screenshotPath = screenshotDir +"/"+ stepName + "_" + timestamp + ".png";
        String customPath = "/screenshots/screenshots_"+dateSuffix+"/"+stepName+"_"+timestamp+".png";

        try {
            FileUtils.copyFile(srcFile, new File(screenshotPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customPath;
    }
}
