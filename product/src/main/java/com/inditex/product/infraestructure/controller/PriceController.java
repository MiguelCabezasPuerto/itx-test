package com.inditex.product.infraestructure.controller;

import com.inditex.product.application.DateValidator;
import com.inditex.product.application.PriceFinder;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.PriceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Controlador REST para la gestión de precios.
 * Proporciona una API para obtener el precio de un producto en una fecha determinada.
 */
@RestController
@RequiredArgsConstructor
public class PriceController implements PricesApi {

    private final PriceFinder priceFinder;

    @Override
    public ResponseEntity<PriceResponse> getPrice(Integer brandId, Integer productId, String date) {
        if(!DateValidator.isValidTimestamp(date)){
            return ResponseEntity.badRequest().build();
        }
        var priceResponse = priceFinder.execute(brandId, productId, date);

        return priceResponse.map(record -> {
            PriceResponse priceClassResponse = new PriceResponse();
            priceClassResponse.setBrandId(record.brandId());
            priceClassResponse.setProductId(record.productId());
            priceClassResponse.setFareId(record.fareId());
            priceClassResponse.setStartDate(record.startDate());
            priceClassResponse.setEndDate(record.endDate());
            priceClassResponse.setPrice(Optional.ofNullable(record.price()).map(BigDecimal::floatValue).orElse(0.0f));
            priceClassResponse.setCurrency(Optional.ofNullable(record.currency()).orElse(""));

            return ResponseEntity.ok(priceClassResponse);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}


