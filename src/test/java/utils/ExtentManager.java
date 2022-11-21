package utils;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentManager {
    private static final String reportFileName = "execution-report" + ".html";
    private static final String reportFilepath = "target/report/";
    private static final String reportFileLocation = reportFilepath + reportFileName;
    private static ExtentReports extent;

    private static final String environment = System.getProperty("env", "prod");
    private static final String testPlan = System.getProperty("groups", "desktop");

    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance();
        return extent;
    }

    //Create an extent report instance
    public static void createInstance() {
        String fileName = getReportPath();

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Test Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName(reportFileName);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

        extent = new ExtentReports();
        extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Environment", environment);
        extent.setSystemInfo("Test Suite", testPlan);
    }

    //Create the report path
    private static String getReportPath() {
        File testDirectory = new File(reportFilepath);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + reportFilepath + " is created!");
                return reportFileLocation;
            } else {
                System.out.println("Failed to create directory: " + reportFilepath);
                return System.getProperty("user.dir");
            }
        } else {
            System.out.println("Directory already exists: " + reportFilepath);
        }
        return reportFileLocation;
    }
}
