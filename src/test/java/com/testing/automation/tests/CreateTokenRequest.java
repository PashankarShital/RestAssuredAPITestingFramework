package com.testing.automation.tests;

import com.testing.automation.pojos.requestPojos.CreateTokenRequestPojo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateTokenRequest {


    public String createToken() {

        CreateTokenRequestPojo createTokenRequestBody = new CreateTokenRequestPojo(
                "admin", "password123");

        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .baseUri("https://restful-booker.herokuapp.com")
                    .basePath("/auth")
                    .body(createTokenRequestBody)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
        String token = response.path("token");
        return token;
    }
}
