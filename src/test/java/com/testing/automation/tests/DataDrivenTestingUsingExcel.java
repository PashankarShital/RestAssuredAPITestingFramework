package com.testing.automation.tests;

import com.testing.automation.listener.RestAssuredListener;
import com.testing.automation.pojos.requestPojos.BookingRequestPojo;
import com.testing.automation.pojos.shared.BookingDates;
import com.testing.automation.utils.BaseTest;
import com.testing.automation.utils.Util;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

@Epic("Data Driven Testing")
@Feature("Create Booking Using Excel Feature")
public class DataDrivenTestingUsingExcel extends BaseTest {

    private static final Logger logger = LogManager.getLogger(DataDrivenTestingUsingExcel.class);


    @Story("Create Booking Using Excel Story")
    @Description("User should be able to do booking Using Excel.")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Create Booking API Test", groups = {"DataDriven"},
            dataProvider="TestDataExcel", dataProviderClass = Util.class)
    public void createBooking(String firstname, String lastname, double totalprice) throws IOException {

        logger.info("Create booking using csv test execution started...");

        BookingDates bookingDates = new BookingDates("2018-01-01", "2019-01-01");
        BookingRequestPojo bookingRequestPojo = new BookingRequestPojo(firstname,
                lastname, (int)totalprice, true, bookingDates, "Breakfast");

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
