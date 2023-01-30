package com.abnamro.emrahboz.assignment.retail.data.repository;

import com.abnamro.emrahboz.assignment.retail.data.model.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {

    Optional<OrderProductEntity> findByOrderIdAndProductId(String orderId, Long productId);

    List<OrderProductEntity> findAllByOrderId(String orderId);


    @Query(value = "SELECT p.name,p.available_stock,p.price" +
            "    FROM ORDERS o" +
            "    LEFT JOIN ORDER_PRODUCTS op ON o.id = op.order_id" +
            "    LEFT JOIN PRODUCTS p ON p.id = op.product_id" +
            "    WHERE o.status = 1 AND" +
            "    o.last_update >= DATEADD('DAY',-1, CURRENT_DATE) and o.last_update < DATEADD('DAY',1, CURRENT_DATE)" +
            "    GROUP BY p.id, p.name" +
            "    ORDER BY sum(op.product_quantity)  DESC" +
            "    LIMIT 5", nativeQuery = true)
    List<Tuple> findTopFiveDailyProduct();

    @Query(value = "SELECT p.id,p.name, sum(op.product_quantity),p.available_stock" +
            "FROM PRODUCTS p " +
            "LEFT JOIN ORDER_PRODUCTS op ON p.id = op.product_id" +
            "WHERE " +
            "op.last_update >= SELECT DATE_TRUNC('MONTH', LOCALTIMESTAMP - INTERVAL '0' MONTH)" +
            "GROUP BY p.id, p.name " +
            "ORDER BY sum(op.product_quantity)  ASC" +
            "LIMIT 1", nativeQuery = true)
    Tuple findLeastProductSellingOfMonth();


}
