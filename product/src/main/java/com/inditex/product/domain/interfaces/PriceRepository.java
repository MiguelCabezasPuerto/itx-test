package com.inditex.product.domain.interfaces;

import com.inditex.product.domain.model.Price;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findPrice(Integer brandId, Integer id, String date);
}
