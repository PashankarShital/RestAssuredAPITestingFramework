package com.testing.automation.utils;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void beforeMethod() {
       // RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            Throwable t = result.getThrowable();

            StringWriter error = new StringWriter();
            t.printStackTrace(new PrintWriter(error));

            logger.info(error.toString());
        }
    }
}
