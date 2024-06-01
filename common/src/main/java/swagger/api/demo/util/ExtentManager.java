package swagger.api.demo.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extentReports;
    public synchronized static ExtentReports getExtentReports() {
        if(extentReports==null) {
            extentReports = new ExtentReports();
            String userDirectory = System.getProperty("user.dir");
            String moduleName = userDirectory.substring(userDirectory.lastIndexOf(File.separator));
            String reportLocation = userDirectory.substring(0, userDirectory.lastIndexOf(File.separator))
                    +moduleName+File.separator+"report"+File.separator+"report.html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportLocation);
            reporter.config().setReportName("API Test Automation Report");
            reporter.config().setTheme(Theme.STANDARD);
            reporter.config().setDocumentTitle("API Test Automation Report");
            reporter.config().setEncoding("utf-8");
            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo("Author", "Automation Team");
        }
        return extentReports;
    }
}
