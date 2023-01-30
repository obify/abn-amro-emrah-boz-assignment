package com.abnamro.emrahboz.assignment.retail.service.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {

    private String id;
    private List<OrderProductDto> products;
    private BigDecimal orderTotalAmount;
    private Integer totalItemCount;
    private LocalDateTime lastUpdate;

}
