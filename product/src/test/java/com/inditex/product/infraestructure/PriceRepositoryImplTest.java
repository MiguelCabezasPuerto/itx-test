package com.inditex.product.infraestructure;

import com.inditex.product.domain.exception.PriceException;
import com.inditex.product.domain.model.Price;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PriceRepositoryImpl priceRepository;

    @Mock
    private TypedQuery<Price> query;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Integer brandId = 1;
    private final Integer productId = 100;
    private final String dateString = "2025-05-08 10:12:02";
    private final LocalDateTime date = LocalDateTime.parse(dateString, TIMESTAMP_FORMATTER);
    private final String startDate= date.minusDays(1).format(TIMESTAMP_FORMATTER);
    private final String endDate= date.plusDays(1).format(TIMESTAMP_FORMATTER);
    private final Price mockPrice = new Price(1,productId,brandId,startDate,endDate,1,1,new BigDecimal(11),"EUR");

    @BeforeEach
    void setUp() {
        lenient().when(entityManager.createQuery(anyString(), eq(Price.class))).thenReturn(query);
        lenient().when(query.setParameter(anyString(), any())).thenReturn(query);
    }

    @Test
    void findPrice_returnPrice_whenExists() {
        // When
        when(query.getResultList()).thenReturn(List.of(mockPrice));
        Optional<Price> result = priceRepository.findPrice(brandId, productId, dateString);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockPrice, result.get());
        verify(entityManager, times(1)).createQuery(anyString(), eq(Price.class));
        verify(query, times(1)).setParameter("brandId", brandId);
        verify(query, times(1)).setParameter("productId", productId);
        verify(query, times(1)).setParameter("date", date);
    }

    @Test
    void findPrice_returnEmpty_whenNoResultsFound() {
        // When
        when(query.getResultList()).thenReturn(List.of());
        Optional<Price> result = priceRepository.findPrice(brandId, productId, dateString);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void findPrice_throwException_whenDatabaseFails() {
        // When
        when(query.getResultList()).thenThrow(new RuntimeException("Database Error"));

        // Then
        PriceException exception = assertThrows(PriceException.class, () -> priceRepository.findPrice(brandId, productId, dateString));
        assertEquals("[" + productId + "] Error consultando la base de datos.", exception.getMessage());
    }
}
