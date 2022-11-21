package utils;

import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class ExecutionWatcher implements TestWatcher {
    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        ExtentTestManager.getTest().log(Status.SKIP, "Tests in " + extensionContext.getDisplayName() + " are aborted.");
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional <String> optional) {
        ExtentTestManager.getTest().log(Status.SKIP, "Tests in " + extensionContext.getDisplayName() + " are disabled.");
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        ExtentTestManager.getTest().fail("TEST FAILED: " + extensionContext.getDisplayName() + ". \r\n " + "Error: " + throwable.toString());
        flushReports();
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        ExtentTestManager.getTest().pass("TEST PASSED: " + extensionContext.getDisplayName());
        flushReports();
    }

    private void flushReports() {
        ExtentTestManager.endTest();
        ExtentManager.getInstance().flush();
    }
}
