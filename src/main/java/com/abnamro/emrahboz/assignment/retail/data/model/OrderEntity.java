package com.abnamro.emrahboz.assignment.retail.data.model;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity(name = "OrderEntity")
@Table(name = "ORDERS")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
public class OrderEntity {

    @Id
    private String id;
    private Long userId;
    private BigDecimal orderTotalAmount;
    private Integer totalItemCount;
    private OrderStatus status;
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public OrderEntity() {

    }

    public enum OrderStatus{
        ON_CART,
        ORDERED
    }
}
