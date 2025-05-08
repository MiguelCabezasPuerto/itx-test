package com.inditex.product.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PriceE2ETest {


    @BeforeEach
    public void setUp() {
        RestAssured.port = 8080;
    }

    static Stream<TestInputs> testCases() {
        return Stream.of(
                new TestInputs(1, 35455, "2020-06-14 10:00:00", 35.50f, 1),
                new TestInputs(1, 35455, "2020-06-14 16:00:00", 25.45f, 2),
                new TestInputs(1, 35455, "2020-06-14 21:00:00", 35.50f, 1),
                new TestInputs(1, 35455, "2020-06-15 10:00:00", 30.50f, 3),
                new TestInputs(1, 35455, "2020-06-16 21:00:00", 38.95f, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void shouldReturnCorrectPrice(TestInputs inputs) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("brandId", inputs.brandId())
                .queryParam("productId", inputs.productId())
                .queryParam("date", inputs.date())
                .when()
                .get("/prices")
                .then()
                .statusCode(200)
                .body("price", equalTo(inputs.expectedPrice()))
                .body("fareId", equalTo(inputs.expectedFare()));
    }

    @Test
    public void return_404() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .queryParam("brandId", 2)
                .queryParam("productId", 35455)
                .queryParam("date", "2020-06-14 10:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(404);
    }
    @Test
    public void return_400() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .queryParam("brandId", 2)
                .queryParam("productId", 35455)
                .queryParam("date", "202-06-14 10:00:00")
                .when()
                .get("/prices")
                .then()
                .statusCode(400);
    }

    private record TestInputs(int brandId, int productId, String date, float expectedPrice, int expectedFare) {}
}

