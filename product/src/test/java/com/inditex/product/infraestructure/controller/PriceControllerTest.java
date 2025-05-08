package com.inditex.product.infraestructure.controller;

import com.inditex.product.application.PriceFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.PriceResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

    @Mock
    private PriceFinder priceFinder;

    @InjectMocks
    private PriceController priceController;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Integer brandId = 1;
    private final Integer productId = 100;
    private final String dateString = "2025-05-08 10:12:02";
    private final LocalDateTime date = LocalDateTime.parse(dateString, TIMESTAMP_FORMATTER);
    private final String startDate= date.minusDays(1).format(TIMESTAMP_FORMATTER);
    private final String endDate= date.plusDays(1).format(TIMESTAMP_FORMATTER);
    private final PriceResponse mockPriceResponse = new PriceResponse();

    @BeforeEach
    void setUp() {
        mockPriceResponse.setBrandId(brandId);
        mockPriceResponse.setProductId(productId);
        mockPriceResponse.setFareId(1);
        mockPriceResponse.setStartDate(startDate);
        mockPriceResponse.setEndDate(endDate);
        mockPriceResponse.setPrice(BigDecimal.valueOf(99.99).floatValue());
        mockPriceResponse.setCurrency("EUR");
    }

    @Test
    void getPrice_returnPriceResponse_whenPriceExists() {
        //Given
        com.inditex.product.application.PriceResponse priceResponse = new com.inditex.product.application.PriceResponse(mockPriceResponse.getBrandId(),mockPriceResponse.getProductId(),mockPriceResponse.getFareId(),mockPriceResponse.getStartDate(),mockPriceResponse.getEndDate(),new BigDecimal(mockPriceResponse.getPrice()),mockPriceResponse.getCurrency());
        // When
        when(priceFinder.execute(brandId, productId,dateString )).thenReturn(Optional.of(priceResponse));
        ResponseEntity<PriceResponse> response = priceController.getPrice(brandId, productId, dateString);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPriceResponse, response.getBody());
        verify(priceFinder, times(1)).execute(brandId, productId, dateString);
    }

    @Test
    void getPrice_return404_whenPriceNotFound() {
        // When
        when(priceFinder.execute(brandId, productId, dateString)).thenReturn(Optional.empty());
        ResponseEntity<PriceResponse> response = priceController.getPrice(brandId, productId, dateString);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(priceFinder, times(1)).execute(brandId, productId, dateString);
    }

    @Test
    void getPrice_return400_whenIncorrectInputDate() {
        // When
        ResponseEntity<PriceResponse> response = priceController.getPrice(brandId, productId, "202-06-15 10:00:00");

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(priceFinder, times(0)).execute(brandId, productId, dateString);
    }
}
