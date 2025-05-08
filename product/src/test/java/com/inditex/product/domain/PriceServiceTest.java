package com.inditex.product.domain;

import com.inditex.product.application.PriceResponse;
import com.inditex.product.domain.interfaces.PriceRepository;
import com.inditex.product.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PriceServiceTest {

    @MockBean
    private PriceRepository priceRepository;

    @Autowired
    private PriceService priceService;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Integer brandId = 1;
    private final Integer productId = 100;
    private final String dateString = "2025-05-08 10:12:02";
    private final LocalDateTime date = LocalDateTime.parse(dateString, TIMESTAMP_FORMATTER);
    private final String startDate= date.minusDays(1).format(TIMESTAMP_FORMATTER);
    private final String endDate= date.plusDays(1).format(TIMESTAMP_FORMATTER);
    private final Price mockPriceResponse = new Price(1,productId,brandId,startDate,endDate,1,1, BigDecimal.valueOf(99.99),"EUR");


    @BeforeEach
    void setUp() {
        lenient().when(priceRepository.findPrice(brandId, productId, dateString)).thenReturn(Optional.of(mockPriceResponse));
    }

    @Test
    void getPrice_returnPriceResponse_whenPriceExists() {
        //Given
        PriceResponse expected = new PriceResponse(mockPriceResponse.getBrandId(),mockPriceResponse.getProductId(),mockPriceResponse.getFareId(),mockPriceResponse.getStartDate(),mockPriceResponse.getEndDate(),mockPriceResponse.getPrice(),mockPriceResponse.getCurrency());
        // When
        when(priceRepository.findPrice(brandId, productId, dateString)).thenReturn(Optional.of(mockPriceResponse));
        Optional<PriceResponse> result = priceService.getPrice(brandId, productId, dateString);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(priceRepository, times(1)).findPrice(brandId, productId, dateString);
    }

    @Test
    void getPrice_returnEmptyOptional_whenPriceNotFound() {
        // When
        when(priceRepository.findPrice(brandId, productId, dateString)).thenReturn(Optional.empty());
        Optional<PriceResponse> result = priceService.getPrice(brandId, productId, dateString);

        // Then
        assertFalse(result.isPresent());
        verify(priceRepository, times(1)).findPrice(brandId, productId, dateString);
    }

    @Test
    void getPrice_triggerFallback_whenCircuitBreakerFails() {
        // When
        lenient().when(priceRepository.findPrice(anyInt(), anyInt(), any(String.class)))
                .thenThrow(new RuntimeException("DB Down"));

        for (int i = 0; i < 10; i++) {
            try {
                priceService.getPrice(1, 100, OffsetDateTime.now().toString());
            } catch (Exception ignored) {

            }
        }

        Optional<PriceResponse> result = priceService.getPrice(1, 100, OffsetDateTime.now().toString());

        // Then
        assertFalse(result.isPresent());
    }
}
