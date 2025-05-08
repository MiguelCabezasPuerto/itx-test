package com.inditex.product.acceptance;

import io.cucumber.java.en.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

public class GetPriceStepDefs {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private ResponseEntity<String> response;
    private Integer brandId;
    private Integer productId;
    private String date;

    @Given("la marca con id {int} y el producto con id {int}")
    public void setUpRequest(int brand, int product) {
        this.brandId = brand;
        this.productId = product;
    }

    @When("consulto el precio para la fecha {string}")
    public void callApi(String dateStr) {
        this.date = dateStr;
        String url = String.format("http://localhost:8080/prices?brandId=%d&productId=%d&date=%s", brandId, productId, date);
        response = restTemplate.getForEntity(url, String.class);
    }

    @Then("el sistema debe devolver un precio de {double} con la tarifa {int}")
    public void validateResponse(double expectedPrice, int expectedFare) {
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"price\":" + expectedPrice));
        assertTrue(body.contains("\"fareId\":" + expectedFare));
    }
    @Then("el sistema debe devolver un codigo de respuesta {int}")
    public void validateCodeResponse(int codeResponse) {
        int httpCode= response.getStatusCode().value();
        assertEquals(codeResponse, httpCode);
    }
}
