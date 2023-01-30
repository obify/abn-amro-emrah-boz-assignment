package com.abnamro.emrahboz.assignment.retail.data.model;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity(name = "OrderProductEntity")
@Table(name = "ORDER_PRODUCTS")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
public class OrderProductEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String orderId;
    private Long productId;
    private Integer productQuantity;
    private BigDecimal productAmount;
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public OrderProductEntity() {

    }
}
