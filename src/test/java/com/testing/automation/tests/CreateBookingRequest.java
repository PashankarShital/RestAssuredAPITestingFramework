package com.testing.automation.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.automation.listener.RestAssuredListener;
import com.testing.automation.pojos.requestPojos.BookingRequestPojo;
import com.testing.automation.pojos.shared.BookingDates;
import com.testing.automation.pojos.responsePojos.BookingResponsePojo;
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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

@Epic("Epic-Booking API CRUD Operations")
@Feature("Create Booking Feature")
public class CreateBookingRequest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CreateBookingRequest.class);


    @Story("Create Booking Story")
    @Description("User should be able to do booking.")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Create Booking API Test", groups = {"sanity"},
            dataProvider="TestDataJson", dataProviderClass = Util.class)
    public void createBooking(LinkedHashMap<String, String> testData) throws IOException {

        logger.info("createBooking test execution started...");
        String jsonSchema = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA),"UTF-8");

        BookingDates bookingDates = new BookingDates("2018-01-01", "2019-01-01");
        BookingRequestPojo bookingRequestPojo = new BookingRequestPojo(testData.get("firstname"),
                testData.get("lastname"), 111, true, bookingDates, "Breakfast");

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
                    .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema))
                    //.body("booking.firstname", Matchers.equalTo("Jim"))
                    .body("booking.totalprice", Matchers.equalTo(111))
                    .body("booking.bookingdates.checkin", Matchers.equalTo("2018-01-01"))
                .extract()
                    .response();
        //.as(BookingResponse.class);

        //Assert.assertEquals("Jim", response.getBooking().getFirstname());

        ObjectMapper objectMapper = new ObjectMapper();
        BookingResponsePojo bookingResponsePojo;
        try {
            bookingResponsePojo = objectMapper.readValue(response.asString(), BookingResponsePojo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize response: " + response.asString(), e);
        }

        //Assert.assertEquals("Jim", bookingResponsePojo.getBooking().getFirstname());

        logger.info("createBooking test execution completed...");

        /*int bookingId = response.path("bookingid");

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .pathParams("bookingId", bookingId)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                    .get("{bookingId}")
                .then()
                    .statusCode(200)
                    .body("firstname", Matchers.equalTo( "Jim"));*/
    }
}
