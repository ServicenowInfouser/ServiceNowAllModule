package listener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class CustomReport implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        StringBuilder report = new StringBuilder();

        // HTML header with improved styling for class headers
        report.append("""
                <html>
                <head>
                  <title>Custom TestNG Report</title>
                  <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    h3 { background-color: #444; color: white; padding: 10px; margin-top: 30px; }
                    table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
                    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
                    th { background: #f7f7f7; }
                    .status-PASS { color: #167a25; font-weight: bold; }
                    .status-FAIL { color: #b00020; font-weight: bold; }
                    .status-SKIP { color: #9a6700; font-weight: bold; }
                    .error { white-space: pre-wrap; color: #b00020; font-size: 11px; }
                  </style>
                </head>
                <body>
                <h2>Test Execution Summary</h2>""");

        for (ISuite suite : suites) {
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                ITestContext context = suiteResult.getTestContext();

                // Group results by Class Name
                Map<String, List<ITestResult>> resultsByClass = new LinkedHashMap<>();
                
                List<ITestResult> allResults = new ArrayList<>();
                addAll(allResults, context.getPassedTests());
                addAll(allResults, context.getFailedTests());
                addAll(allResults, context.getSkippedTests());
                
                // Sort chronologically before grouping
                allResults.sort(Comparator.comparingLong(ITestResult::getStartMillis));

                for (ITestResult result : allResults) {
                    String className = result.getTestClass().getName();
                    resultsByClass.computeIfAbsent(className, k -> new ArrayList<>()).add(result);
                }

                // Generate a table for each class
                for (Map.Entry<String, List<ITestResult>> entry : resultsByClass.entrySet()) {
                    String className = entry.getKey();
                    List<ITestResult> classResults = entry.getValue();

                    report.append("<h3>Class: ").append(className).append("</h3>");
                    report.append("<table>");
                    report.append("<tr>")
                            .append("<th style='width: 50px;'>No</th>")
                            .append("<th>Method Name</th>")
                            .append("<th>Description</th>")
                            .append("<th>Test Data</th>")
                            .append("<th style='width: 80px;'>Result</th>")
                            .append("<th>Failure Reason</th>")
                            .append("<th style='width: 80px;'>Time (ms)</th>")
                            .append("</tr>");

                    int methodNo = 1;
                    for (ITestResult result : classResults) {
                        long timeTaken = Math.max(0L, result.getEndMillis() - result.getStartMillis());
                        Object testData = result.getAttribute("TestData");

                        String status = switch (result.getStatus()) {
                            case ITestResult.SUCCESS -> "PASS";
                            case ITestResult.FAILURE -> "FAIL";
                            case ITestResult.SKIP -> "SKIP";
                            default -> "UNKNOWN";
                        };

                        String failureReason = (result.getThrowable() != null) ? 
                                               stackTraceToString(result.getThrowable()) : "";

                        report.append("<tr>");
                        report.append("<td>").append(methodNo++).append("</td>");
                        report.append("<td>").append(result.getMethod().getMethodName()).append("</td>");
                        report.append("<td>").append(result.getMethod().getDescription() != null ? 
                                escapeHtml(result.getMethod().getDescription()) : "").append("</td>");
                        report.append("<td>").append(testData != null ? 
                                escapeHtml(String.valueOf(testData)) : "").append("</td>");
                        report.append("<td class='status-").append(status).append("'>").append(status).append("</td>");
                        report.append("<td>").append(!failureReason.isEmpty() ? 
                                "<div class='error'>" + escapeHtml(failureReason) + "</div>" : "").append("</td>");
                        report.append("<td>").append(timeTaken).append("</td>");
                        report.append("</tr>");
                    }
                    report.append("</table>");
                }
            }
        }

        report.append("</body></html>");

        try (FileWriter writer = new FileWriter(outputDirectory + "/custom-report.html")) {
            writer.write(report.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAll(List<ITestResult> target, IResultMap resultMap) {
        if (resultMap == null) return;
        Collection<ITestResult> results = resultMap.getAllResults();
        if (results != null) target.addAll(results);
    }

    private String stackTraceToString(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.toString()).append("\n");
        for (StackTraceElement ste : t.getStackTrace()) {
            sb.append("    at ").append(ste.toString()).append("\n");
        }
        return sb.toString();
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}