package com.inditex.product.domain;

import com.inditex.product.application.PriceResponse;
import com.inditex.product.domain.interfaces.PriceRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Servicio para la recuperación de precios de productos.
 * Utiliza {@link PriceRepository} para obtener los datos de precios de la base de datos.
 * Implementa mecanismos de caché y tolerancia a fallos con Circuit Breaker.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PriceService {
    private final PriceRepository priceRepository;

    /**
     * Obtiene el precio de un producto específico en una fecha determinada.
     * Usa caché para mejorar la eficiencia y Circuit Breaker para manejar fallos.
     *
     * @param brandId   Identificador de la marca.
     * @param productId Identificador del producto.
     * @param date      Fecha para la cual se quiere obtener el precio.
     * @return Un {@link Optional} con la respuesta del precio si se encuentra, o vacío si no hay datos disponibles.
     */
    @Cacheable(value = "pricesCache", key = "{#brandId, #productId, #date}")
    @CircuitBreaker(name = "priceService", fallbackMethod = "doFallback")
    public Optional<PriceResponse> getPrice(Integer brandId, Integer productId, String date) {
        return priceRepository.findPrice(brandId, productId, date)
                .map(price -> new PriceResponse(price.getBrandId(), price.getProductId(), price.getFareId(),
                        price.getStartDate(), price.getEndDate(), price.getPrice(), price.getCurrency()));
    }

    private Optional<PriceResponse> doFallback(Integer brandId, Integer productId, String date, Throwable t) {
        log.warn("[ {} ]Circuit Breaker activado por fallo: {}",productId, t.getMessage());
        return Optional.empty();
    }
}


