package com.inditex.product.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Representa la respuesta con información del precio de un producto.
 * Esta clase almacena detalles sobre la tarifa aplicada, fechas de validez y la moneda utilizada.
 *
 * @param brandId   Identificador de la marca.
 * @param productId Identificador del producto.
 * @param fareId    Identificador de la tarifa aplicada.
 * @param startDate Fecha de inicio de la validez del precio.
 * @param endDate   Fecha de finalización de la validez del precio.
 * @param price     Valor del precio del producto.
 * @param currency  Código de la moneda en la que se expresa el precio.
 */
public record PriceResponse(
        Integer brandId,
        Integer productId,
        Integer fareId,
        String startDate,
        String endDate,
        BigDecimal price,
        String currency) {}

