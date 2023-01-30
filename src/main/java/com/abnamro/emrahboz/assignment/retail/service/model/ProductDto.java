package com.abnamro.emrahboz.assignment.retail.service.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private Integer availableStock;
    private BigDecimal price;
    private LocalDateTime lastUpdate;
}
