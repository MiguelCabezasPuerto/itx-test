package com.inditex.product.infraestructure;

import com.inditex.product.domain.exception.ExceptionFactory;
import com.inditex.product.domain.interfaces.PriceRepository;
import com.inditex.product.domain.model.Price;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio para la consulta de precios de productos.
 * Utiliza {@link EntityManager} para ejecutar consultas en la base de datos.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class PriceRepositoryImpl implements PriceRepository {
    private final EntityManager entityManager;

    /**
     * Busca el precio de un producto en una fecha determinada.
     *
     * @param brandId   Identificador de la marca.
     * @param productId Identificador del producto.
     * @param dateString      Fecha para la cual se quiere obtener el precio.
     * @return Un {@link Optional} con el precio encontrado o vacío si no hay resultados.
     */
    @Override
    public Optional<Price> findPrice(Integer brandId, Integer productId, String dateString) {
        try {
            LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String query = """
                SELECT p FROM Price p 
                WHERE p.brandId = :brandId 
                  AND p.productId = :productId
                  AND TO_CHAR(p.startDate, 'YYYY-MM-DD HH24:MI:SS') <= :date
                  AND TO_CHAR(p.endDate, 'YYYY-MM-DD HH24:MI:SS') >= :date
                ORDER BY p.priorityMod DESC
                """;

            List<Price> prices = entityManager.createQuery(query, Price.class)
                    .setParameter("brandId", brandId)
                    .setParameter("productId", productId)
                    .setParameter("date", date)
                    .getResultList();

            return prices.stream().findFirst();
        } catch (Exception e) {
            log.error("[{}]Error de base de datos: {}", productId,e.getMessage());
            throw ExceptionFactory.createDatabaseException("["+productId+"] Error consultando la base de datos.");
        }
    }
}




