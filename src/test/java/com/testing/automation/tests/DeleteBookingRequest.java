package com.testing.automation.tests;

import com.jayway.jsonpath.JsonPath;
import com.testing.automation.pojos.requestPojos.BookingRequestPojo;
import com.testing.automation.pojos.responsePojos.BookingResponsePojo;
import com.testing.automation.pojos.shared.BookingDates;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Epic-Booking API CRUD Operations")
@Feature("Delete Booking Feature")
public class DeleteBookingRequest {

    @Story("Delete Booking Story")
    @Test(description = "Delete Booking API Test", groups = {"smoke", "regression"})
    @Description("User should be able to cancel the booking.")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBooking() {

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

        RestAssured
                .given()
                    .filter(new AllureRestAssured())
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/booking")
                    .header("Cookie", "token="+token)
                    .contentType(ContentType.JSON)
                    .pathParams("bookingId", bookingId)
                .when()
                    .delete("/{bookingId}")
                .then()
                    .statusCode(201);
    }
}
