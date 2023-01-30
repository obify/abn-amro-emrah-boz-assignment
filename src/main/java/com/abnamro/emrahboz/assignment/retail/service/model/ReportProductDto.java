package com.abnamro.emrahboz.assignment.retail.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportProductDto {

    private String name;
    private Integer availableStock;
    private BigDecimal price;
    private boolean lowQuantity;
}
