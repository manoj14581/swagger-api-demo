package swagger.api.demo.util;


import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;

public abstract class BaseTest {
    protected static ThreadLocal<StringBuilder> TEST_LOGS = new ThreadLocal<>();
    private String reportPath = null;


    @BeforeSuite
    public static void init() throws ApiException {
        PropertyReader.loadEnvProperties();
        System.out.println("API Test Execution Started...");
        System.out.println("env --> "+System.getProperty("env"));
        System.out.println("BaseUrl --> "+ System.getProperty("base_Url"));
    }

    @BeforeMethod
    public void before(Method method) {
        String name = method.getAnnotation(Test.class).testName();
        if(name==null || name.isEmpty())
            name = method.getName();
        String description = method.getAnnotation(Test.class).description();
        if(description==null || description.isEmpty())
            description = splitCamelCase(method.getName());
        startTest(this.getClass().getSimpleName(), name, description);
    }

    @AfterMethod
    public void after(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String testResult = result.getThrowable().getMessage();
            StackTraceElement[] stacktrace = result.getThrowable().getStackTrace();
            ReportUtil.fail(testResult, stacktrace);
        } else {
            ReportUtil.pass();
        }
    }

    public static ExtentTest startTest(String category, String name, String description) {
        TEST_LOGS.set(new StringBuilder(0));
        ExtentTestManager.setTestCategory(name);
        ExtentTest test = ExtentTestManager.startTest(name, description);
        test.assignCategory(category);
        return test;
    }

    @AfterSuite
    public void afterSuite() {
        ExtentManager.getExtentReports().flush();
        this.cleanup();
        System.out.println("API Test Execution Completed!");
    }

    public void cleanup() {

    }

    public static void log(String logMessage) {
        System.out.println(logMessage);
        if(TEST_LOGS.get()==null)
            TEST_LOGS.set(new StringBuilder(0));
        if(TEST_LOGS.get().length()>0)
            TEST_LOGS.get().append("\n");
        TEST_LOGS.get().append(logMessage);
    }

    static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public static OffsetDateTime getCurrentOffsetDateTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return OffsetDateTime.parse(f.format(new Date()));
    }

    public static OffsetDateTime getOffsetDateTime(String offsetDateTime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return OffsetDateTime.parse(offsetDateTime);
    }
}
