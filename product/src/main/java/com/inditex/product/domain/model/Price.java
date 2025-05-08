package com.inditex.product.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "PRICES", indexes = {
        @Index(name = "idx_brand", columnList = "brandId"),
        @Index(name = "idx_start_date", columnList = "startDate"),
        @Index(name = "idx_end_date", columnList = "endDate"),
        @Index(name = "idx_product_brand_date", columnList = "productId, brandId, startDate, endDate")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRegister;
    private Integer productId;
    private Integer brandId;
    private String startDate;
    private String endDate;
    private Integer fareId;
    private Integer priorityMod;
    private BigDecimal price;
    private String currency;
}

