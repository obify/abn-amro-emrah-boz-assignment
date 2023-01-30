package com.abnamro.emrahboz.assignment.retail.service.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SaleAmountsDto {
    private BigDecimal saleAmount;
    private String day;
}
