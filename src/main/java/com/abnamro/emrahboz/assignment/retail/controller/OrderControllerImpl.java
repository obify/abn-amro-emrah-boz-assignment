package com.abnamro.emrahboz.assignment.retail.controller;

import com.abnamro.emrahboz.assignment.retail.controller.model.AddToCartRequest;
import com.abnamro.emrahboz.assignment.retail.service.OrderService;
import com.abnamro.emrahboz.assignment.retail.service.model.AddToCartDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderCompleteDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"Order API"}, description = "Order Operations API")
public class OrderControllerImpl implements OrderController {

    private final OrderService service;


    @Override
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderDto addToCart(AddToCartRequest request) {

        return service.addToCard(AddToCartDto.builder()
                .orderId(request.getOrderId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .userName(SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal().toString())
                .build());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public OrderDto completeOrder(String orderId) {

        return service.completeOrder(OrderCompleteDto.builder()
                .orderId(orderId)
                .userName(SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal().toString())
                .build());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public OrderDto get(String orderId) {
        return service.get(orderId);
    }
}
