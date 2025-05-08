package com.inditex.product.domain;

import com.inditex.product.domain.exception.ErrorType;
import com.inditex.product.domain.exception.ExceptionFactory;
import com.inditex.product.domain.exception.PriceException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionFactoryTest {

    @Test
    void createDatabaseException_returnPriceException() {
        // Given
        String expectedMessage = "Error en la base de datos";

        // When
        PriceException exception = ExceptionFactory.createDatabaseException(expectedMessage);

        // Then
        assertNotNull(exception);
        assertEquals(ErrorType.DATABASE_ERROR, exception.getErrorType());
        assertEquals(expectedMessage, exception.getMessage());
    }
}
