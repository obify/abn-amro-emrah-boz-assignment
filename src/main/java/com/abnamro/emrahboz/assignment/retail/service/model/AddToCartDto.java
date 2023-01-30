package com.abnamro.emrahboz.assignment.retail.service.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AddToCartDto {


    private String orderId;
    private Long productId;
    private Integer quantity;
    private String userName;

}
