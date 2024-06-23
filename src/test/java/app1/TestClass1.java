package app1;

import base.BaseTest;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import reporting.ExtentManager;

public class TestClass1 extends BaseTest {

    @Test
    public void testMethod1() {
        try {
            WebElement searchBox = getElement("txtBox_SearchBox");
            ExtentManager.logTestStep("Step 1", "Entering data in Search Box");
            searchBox.sendKeys("Selenium", Keys.ENTER);
            ExtentManager.logTestStep("Step 2", "Verifying outcome");
            System.out.println(driver.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMethod2() {
        try {
            ExtentManager.logTestStep("Step 1", "Entering data in Search Box");
            getElement("txtBox_SearchBox").sendKeys("Automation",Keys.ENTER);
            System.out.println(driver.getTitle());
            ExtentManager.logTestStep("Step 2", "Verifying outcome");
            getElement("kamal");
        } catch (Exception e) {
            ExtentManager.handleException(e);
        }
    }

    @Test
    public void testMethod3() {
        try {
            ExtentManager.logTestStep("Step 1", "Entering data in Search Box");
            getElement("txtBox_SearchBox").sendKeys("Hello World",Keys.ENTER);
            System.out.println(driver.getTitle());
            ExtentManager.logTestStep("Step 2", "Verifying outcome");
            getElement("kamal");
        } catch (Exception e) {
            ExtentManager.handleException(e);
        }
    }
}
