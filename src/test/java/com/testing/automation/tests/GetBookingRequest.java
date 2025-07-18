package com.testing.automation.tests;

import com.testing.automation.listener.RestAssuredListener;
import com.testing.automation.utils.BaseTest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetBookingRequest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    public void getBooking() {

        Response response =
        RestAssured
            .given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .filter(new RestAssuredListener())
           .when()
                .get("/0000")
           .then()
                .assertThat()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .header("Content-Type", "application/json; charset=utf-8")
           .extract()
                .response();
    }
}
