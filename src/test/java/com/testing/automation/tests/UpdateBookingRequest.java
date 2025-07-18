package com.testing.automation.tests;

import com.jayway.jsonpath.JsonPath;
import com.testing.automation.pojos.requestPojos.BookingRequestPojo;
import com.testing.automation.pojos.responsePojos.BookingResponsePojo;
import com.testing.automation.pojos.shared.BookingDates;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateBookingRequest {

    @Test(groups = {"smoke", "regression"})
    public void updateBooking() {

        BookingDates bookingDates = new BookingDates("2018-01-01", "2019-01-01");
        BookingRequestPojo bookingRequestPojo = new BookingRequestPojo("Jim", "Brown", 111, true, bookingDates, "Breakfast");

        BookingResponsePojo createBookingResponse = RestAssured
                .given()
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/booking")
                    .contentType(ContentType.JSON)
                    .body(bookingRequestPojo)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .response()
                    .as(BookingResponsePojo.class);

        int bookingId = createBookingResponse.getBookingId();

        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        String token = createTokenRequest.createToken();

        BookingRequestPojo bookingRequestPojo1 = new BookingRequestPojo("Shital", "Brown", 111, true, bookingDates, "Breakfast");

        Response updateBookingResponse = RestAssured
                .given()
                    .filter(new AllureRestAssured())
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/booking")
                    .header("Cookie", "token="+token)
                    .contentType(ContentType.JSON)
                    .pathParams("bookingId", bookingId)
                    .body(bookingRequestPojo1)
                .when()
                    .put("/{bookingId}")
                .then()
                    .statusCode(200)
                    .extract()
                    .response();

        String updatedFirstName = JsonPath.read(updateBookingResponse.asString(), "$.firstname");
        Assert.assertEquals(updatedFirstName, "Shital");

    }
}
