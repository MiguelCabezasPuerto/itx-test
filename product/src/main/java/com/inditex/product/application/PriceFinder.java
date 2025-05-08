package com.inditex.product.application;

import com.inditex.product.domain.PriceService;
import com.inditex.product.domain.exception.PriceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Servicio para la búsqueda de precios de productos.
 * Utiliza {@link PriceService} para recuperar la información de precios basada en la marca,
 * el producto y la fecha proporcionada.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PriceFinder {
    private final PriceService priceService;

    /**
     * Ejecuta la búsqueda del precio de un producto en una fecha específica.
     *
     * @param brandId   Identificador de la marca.
     * @param productId Identificador del producto.
     * @param date      Fecha para la cual se quiere obtener el precio.
     * @return Un {@link Optional} que contiene el precio si se encuentra, o vacío si ocurre un error.
     */
    public Optional<PriceResponse> execute(Integer brandId, Integer productId, String date) {
        try {
            return priceService.getPrice(brandId, productId, date);
        } catch (PriceException e) {
            log.error("[{}] Error en la recuperacion del precio del producto: {}",productId, e.getMessage());
            return Optional.empty();
        }
    }
}
