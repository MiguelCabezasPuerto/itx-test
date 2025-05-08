package com.inditex.product.domain.exception;

/**
 * Factoría para la creación de excepciones específicas del sistema.
 * Proporciona métodos para generar excepciones relacionadas con errores de base de datos.
 */
public class ExceptionFactory {

    /**
     * Crea una excepción de base de datos con un mensaje específico.
     *
     * @param message Mensaje descriptivo del error ocurrido en la base de datos.
     * @return Una instancia de {@link PriceException} con el tipo de error {@link ErrorType#DATABASE_ERROR}.
     */
    public static PriceException createDatabaseException(String message) {
        return new PriceException(ErrorType.DATABASE_ERROR, message);
    }
}

