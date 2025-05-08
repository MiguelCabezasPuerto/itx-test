package com.inditex.product.application;

import com.inditex.product.domain.PriceService;
import com.inditex.product.domain.exception.ErrorType;
import com.inditex.product.domain.exception.PriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceFinderTest {

    @Mock
    private PriceService priceService;

    @InjectMocks
    private PriceFinder priceFinder;

    private final Integer brandId = 1;
    private final Integer productId = 100;
    private final OffsetDateTime date = OffsetDateTime.now();
    private final String dateString = date.toString();
    private final String startDate= date.minusDays(1).toString();
    private final String endDate= date.plusDays(1).toString();
    private final PriceResponse mockPriceResponse = new PriceResponse(
            brandId, productId, 1, startDate, endDate, BigDecimal.valueOf(99.99), "EUR"
    );

    @BeforeEach
    void setUp() {
        lenient().when(priceService.getPrice(brandId, productId, dateString)).thenReturn(Optional.of(mockPriceResponse));
    }

    @Test
    void execute_returnPriceResponse_whenPriceExists() {
        //When
        when(priceService.getPrice(brandId, productId, dateString)).thenReturn(Optional.of(mockPriceResponse));
        Optional<PriceResponse> result = priceFinder.execute(brandId, productId, dateString);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockPriceResponse, result.get());

        verify(priceService, times(1)).getPrice(brandId, productId, dateString);
    }

    @Test
    void execute_returnEmptyOptional_whenPriceNotFound() {
        // When
        when(priceService.getPrice(brandId, productId, dateString)).thenReturn(Optional.empty());
        Optional<PriceResponse> result = priceFinder.execute(brandId, productId, dateString);

        // Then
        assertFalse(result.isPresent());
        verify(priceService, times(1)).getPrice(brandId, productId, dateString);
    }

    @Test
    void execute_returnEmptyOptional_whenExceptionOccurs() {
        // When
        when(priceService.getPrice(brandId, productId, dateString)).thenThrow(new PriceException(ErrorType.DATABASE_ERROR,"Database exception"));
        Optional<PriceResponse> result = priceFinder.execute(brandId, productId, dateString);

        // Then
        assertFalse(result.isPresent());
        verify(priceService, times(1)).getPrice(brandId, productId, dateString);
    }
}
