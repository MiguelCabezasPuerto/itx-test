package com.inditex.product.domain.exception;

import lombok.Getter;

/**
 * Excepción personalizada para errores relacionados con precios.
 * Contiene un tipo de error específico que permite identificar la causa del problema.
 */
@Getter
public class PriceException extends RuntimeException {
    private final ErrorType errorType;

    /**
     * Construye una nueva excepción de precios con un mensaje descriptivo y un tipo de error específico.
     *
     * @param errorType Tipo de error asociado a la excepción.
     * @param message   Mensaje que describe la causa del error.
     */
    public PriceException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
}

