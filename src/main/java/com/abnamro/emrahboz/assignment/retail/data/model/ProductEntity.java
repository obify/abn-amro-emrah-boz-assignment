package com.abnamro.emrahboz.assignment.retail.data.model;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity(name = "ProductEntity")
@Table(name = "PRODUCTS")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
public class ProductEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer availableStock;
    private BigDecimal price;
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public ProductEntity() {

    }
}
