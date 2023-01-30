package com.abnamro.emrahboz.assignment.retail.controller.model;

import lombok.Data;

@Data
public class AddToCartRequest {

    String orderId;
    Long productId;
    Integer quantity;
}
