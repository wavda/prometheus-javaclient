package utils;

import com.aventstack.extentreports.Status;

public class Logger {
    public static void info(String message) {
        ExtentTestManager.getTest().log(Status.INFO, message);
    }

    public static void warning(String message) {
        ExtentTestManager.getTest().log(Status.WARNING, message);
    }

    public static void error(String message) {
        ExtentTestManager.getTest().log(Status.ERROR, message);
    }
}
