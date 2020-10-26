package org.minibus.aqa.core.common.handlers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.minibus.aqa.core.common.env.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class TestListener implements ITestListener, IConfigurationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);
    private static long startTimeMs;
    private static TestListener instance;

    public TestListener() {
    }

    public static TestListener getInstance() {
        if (instance == null) {
            instance = new TestListener();
        }
        return instance;
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        startTimeMs = iTestResult.getStartMillis();
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        LOGGER.info(String.format("[TEST PASSED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String methodName = iTestResult.getName();

        if(!iTestResult.isSuccess()) {
            File scrFile = Device.getScreenshot();
            String screenshotFileName = methodName + "_" + localDateTime.toString() + "." + FilenameUtils.getExtension(scrFile.getAbsolutePath());
            try {
                String reportDir = new File(System.getProperty("user.dir")).getAbsolutePath() + "/target/surefire-reports";
                File destFile = new File(reportDir + "/screenshots/" + screenshotFileName);
                FileUtils.moveFile(scrFile, destFile);
                // Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LOGGER.info(String.format("[TEST FAILED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        LOGGER.info(String.format("[TEST SKIPPED][TEST NAME: %s][TIME: %dms]",
                iTestResult.getMethod().getMethodName(), getDuration(iTestResult.getEndMillis())));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        LOGGER.info(String.format("[TESTING HAS STARTED][TESTS FOUND: %d]",
                iTestContext.getSuite().getXmlSuite().getTests().size()));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        LOGGER.info(String.format("[TESTING HAS FINISHED][FAILED: %d][SUCCEEDED: %d]",
                iTestContext.getFailedTests().size(), iTestContext.getPassedTests().size()));
    }

    @Override
    public void onConfigurationSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onConfigurationFailure(ITestResult iTestResult) {
        iTestResult.setStatus(ITestResult.FAILURE);
        onTestFailure(iTestResult);
    }

    @Override
    public void onConfigurationSkip(ITestResult iTestResult) {

    }

    private static long getDuration(long endTimeMs) {
        return startTimeMs == 0 ? startTimeMs : endTimeMs - startTimeMs;
    }
}
