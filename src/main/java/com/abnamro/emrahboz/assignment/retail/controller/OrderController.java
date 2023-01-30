package com.abnamro.emrahboz.assignment.retail.controller;

import com.abnamro.emrahboz.assignment.retail.controller.model.AddToCartRequest;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Validated
@RequestMapping(value = "/api/v1/order")
public interface OrderController {


    @ApiOperation(value = "Adding new product to shopping card", authorizations = {@Authorization("ROLE_CUSTOMER")})
    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    OrderDto addToCart(@RequestBody AddToCartRequest request);

    @ApiOperation(value = "Complete an exist cart to order", authorizations = {@Authorization("ROLE_CUSTOMER")})
    @RequestMapping(value = "{orderId}", method = RequestMethod.PATCH)
    OrderDto completeOrder(@PathVariable("orderId") String orderId);

    @ApiOperation(value = "Get an order with it's id", authorizations = {@Authorization("ROLE_EMPLOYEE")})
    @RequestMapping(value = "{orderId}", method = RequestMethod.GET)
    OrderDto get(@PathVariable("orderId") String orderId);



}
