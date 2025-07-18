package com.testing.automation.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.automation.listener.RestAssuredListener;
import com.testing.automation.pojos.requestPojos.BookingRequestPojo;
import com.testing.automation.pojos.responsePojos.BookingResponsePojo;
import com.testing.automation.pojos.shared.BookingDates;
import com.testing.automation.utils.BaseTest;
import com.testing.automation.utils.FileNameConstants;
import com.testing.automation.utils.Util;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Epic("Data Driven Testing")
@Feature("Create Booking Using CSV Feature")
public class DataDrivenTestingUsingCSV extends BaseTest {

    private static final Logger logger = LogManager.getLogger(DataDrivenTestingUsingCSV.class);


    @Story("Create Booking Using CSV Story")
    @Description("User should be able to do booking Using CSV.")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Create Booking API Test", groups = {"DataDriven"},
            dataProvider="TestDataCSV", dataProviderClass = Util.class)
    public void createBooking(Map<String, String> testData) throws IOException {

        logger.info("Create booking using csv test execution started...");

        BookingDates bookingDates = new BookingDates("2018-01-01", "2019-01-01");
        BookingRequestPojo bookingRequestPojo = new BookingRequestPojo(testData.get("firstname"),
                testData.get("lastname"), Integer.parseInt(testData.get("totalprice")), true, bookingDates, "Breakfast");

        Response response = RestAssured
                .given()
                    .filter(new AllureRestAssured())
                    .filter( new RestAssuredListener())
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/booking")
                    .contentType(ContentType.JSON)
                    .body(bookingRequestPojo)
                //.log().all()
                .when()
                    .post()
                .then()
                    .assertThat()
                //.log().ifValidationFails()
                .statusCode(200)
                .extract()
                    .response();

        logger.info("Create booking using csv test execution completed...");
    }
}
