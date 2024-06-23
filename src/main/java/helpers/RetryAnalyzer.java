package helpers;

import db.LocatorsDAO;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1;

    @Override
    public boolean retry(ITestResult result) {
        // Check if retry attempts are exhausted
        if (retryCount < MAX_RETRY_COUNT) {
            String testMethodName = result.getMethod().getMethodName();
            boolean shouldRetry = shouldRetryTestMethod(testMethodName);
            if (shouldRetry) {
                retryCount++;
                return true; // Retry the test method
            }
        }
        return false;
    }

    private boolean shouldRetryTestMethod(String testMethodName) {
        // Fetch defectnumber from the database using LocatorsDAO
        String defectNumber = LocatorsDAO.getDefectNumberForTestMethod(testMethodName);

        // Retry if defectnumber is null, otherwise do not retry
        return defectNumber == null || defectNumber.isEmpty();
    }
}