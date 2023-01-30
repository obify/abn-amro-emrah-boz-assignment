package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.service.model.AddToCartDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderCompleteDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderDto;

public interface OrderService {

    OrderDto get(String orderId);

    OrderDto addToCard(AddToCartDto addToCartDto);

    OrderDto completeOrder(OrderCompleteDto orderCompleteDto);
}
