package swagger.api.demo.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
    public static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    public static ExtentReports extent = ExtentManager.getExtentReports();
    public static ThreadLocal<String> TEST_NAME = new ThreadLocal<>();
    public static ThreadLocal<String> TEST_DESCRIPTION = new ThreadLocal<>();
    public static ThreadLocal<String> TEST_NAME_CATEGORY = new ThreadLocal<>();
    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        TEST_NAME.set(testName);
        TEST_DESCRIPTION.set(desc);
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized void assignCategory(ExtentTest test) {
        if(TEST_NAME_CATEGORY!=null && TEST_NAME_CATEGORY.get()!=null)
            test.assignCategory(TEST_NAME_CATEGORY.get());
    }

    public static synchronized void setTestCategory(String name) {
        TEST_NAME_CATEGORY.set(name);
    }



}
