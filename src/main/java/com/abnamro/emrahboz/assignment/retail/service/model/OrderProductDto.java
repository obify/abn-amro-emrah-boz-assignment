package com.abnamro.emrahboz.assignment.retail.service.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderProductDto {

    private Long productId;
    private String productName;
    private Integer productQuantity;
    private BigDecimal productAmount;
}
