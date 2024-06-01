package swagger.api.demo.util;

import java.net.InetAddress;
import java.util.Arrays;

public class ReportUtil {

    public static void pass() {
        String testDetail = ExtentTestManager.TEST_NAME.get() + " " + ExtentTestManager.TEST_DESCRIPTION.get();
        StringBuilder markup = new StringBuilder("<details><summary><b><font color=black>"
                +testDetail+"</font></b></summary>"+BaseTest.TEST_LOGS.get().toString().replaceAll("\n","<br>")+"</details>");
        ExtentTestManager.getTest().pass(markup.toString());
    }

    public static void fail() {
        String testDetail = ExtentTestManager.TEST_NAME.get() + " " + ExtentTestManager.TEST_DESCRIPTION.get();
        StringBuilder markup = new StringBuilder("<details><summary><b><font color=red>"
                +testDetail+"</font></b></summary>"+BaseTest.TEST_LOGS.get().toString().replaceAll("\n","<br>")+"</details>");
        ExtentTestManager.getTest().fail(markup.toString());
    }

    public static void fail(String message) {
        String testDetail = ExtentTestManager.TEST_NAME.get() + " " + ExtentTestManager.TEST_DESCRIPTION.get();
        StringBuilder markup = new StringBuilder("<details><summary><b><font color=red>"+testDetail+"</font></b></summary>"+BaseTest.TEST_LOGS.get().toString().replaceAll("\n","<br>")+"</details>");
        markup.append("</br>");
        markup.append("<b><font color=red>Error Message</font></b>");
        markup.append("</br>"+message);
        ExtentTestManager.getTest().fail(markup.toString());
    }

    public static void fail(String message, StackTraceElement[] stacktrace) {
        String testDetail = ExtentTestManager.TEST_NAME.get() + " " + ExtentTestManager.TEST_DESCRIPTION.get();
        StringBuilder markup = new StringBuilder("<details>");
        markup.append("<summary><b><font color=red>"+testDetail+"</font></b></summary>"+BaseTest.TEST_LOGS.get().toString().replaceAll("\n","<br>"));
        markup.append("</br>");
        markup.append("<b>Failure Details & Stacktrace</b>");
        markup.append("</br>Error Message - <u>"+message+"</u>");
        markup.append("</br><font color=red><i>"+ Arrays.toString(stacktrace).replaceAll(",","<br>")+"</i></font>");
        markup.append("</details>");
        ExtentTestManager.getTest().fail(markup.toString());
    }

    public static void writeReportSkipLog(String step, String log) {
        StringBuilder markup = new StringBuilder("<details><summary><b><font color=gray>"
                +step+"</font></b></summary>"+log.replaceAll("\n","<br>")+"</details>");
        ExtentTestManager.getTest().skip(markup.toString());
    }

    public static String getHostName() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostName();
        } catch(Exception ex) {
            //DO nothing
        }
        return null;
    }
}
