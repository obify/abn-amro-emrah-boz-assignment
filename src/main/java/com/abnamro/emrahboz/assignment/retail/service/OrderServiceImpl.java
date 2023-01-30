package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.mapper.MapperUtil;
import com.abnamro.emrahboz.assignment.retail.data.model.OrderEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.OrderProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.OrderRepository;
import com.abnamro.emrahboz.assignment.retail.data.repository.OrderProductRepository;

import com.abnamro.emrahboz.assignment.retail.exceptions.*;
import com.abnamro.emrahboz.assignment.retail.service.model.*;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final ProductService productService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final MapperUtil mapperUtil;

    @Override
    public OrderDto get(String orderId) {

        OrderEntity orderEntity = orderResourceGet(orderId);

        if (orderEntity.getStatus().equals(OrderEntity.OrderStatus.ON_CART)) {
            throw new ResourceNotFoundException(
                    String.format("Order Id : %s not found", orderId));
        }

        return getOrderDetails(orderEntity);
    }

    @Override
    @Synchronized
    @Transactional
    public OrderDto addToCard(AddToCartDto addToCartDto) {

        ProductEntity updatedProduct = productService.update(addToCartDto.getProductId(), addToCartDto.getQuantity());
        UserEntity userEntity = userService.findByUserName(addToCartDto.getUserName());

        //new order
        if (addToCartDto.getOrderId() == null || addToCartDto.getOrderId().isBlank()) {
            return addToCartForNewOrder(addToCartDto, userEntity, updatedProduct);
        } else {// already created order with new product
            return addToCartForExistingOrder(addToCartDto, userEntity, updatedProduct);
        }

    }

    @Override
    public OrderDto completeOrder(OrderCompleteDto orderCompleteDto) {

        OrderEntity orderEntity = orderResourceGet(orderCompleteDto.getOrderId());
        UserEntity userEntity = userService.findByUserName(orderCompleteDto.getUserName());

        validateOrderOwner(orderEntity, userEntity);
        validateOrderForCompleting(orderEntity);

        orderEntity.setStatus(OrderEntity.OrderStatus.ORDERED);
        orderEntity = orderRepository.saveAndFlush(orderEntity);

        OrderDto orderDto = getOrderDetails(orderEntity);

        CompletableFuture.supplyAsync(() -> notificationService.sendNotification(orderDto, userEntity));

        return orderDto;

    }

    private OrderDto getOrderDetails(OrderEntity orderEntity) {

        List<OrderProductDto> allProductDto = orderProductRepository.findAllByOrderId(orderEntity.getId())
                .stream().map(mapperUtil::mapOrderProductToDto)
                .toList();


        return OrderDto.builder()
                .id(orderEntity.getId())
                .products(allProductDto)
                .totalItemCount(orderEntity.getTotalItemCount())
                .orderTotalAmount(orderEntity.getOrderTotalAmount())
                .lastUpdate(orderEntity.getLastUpdate())
                .build();

    }

    private OrderDto addToCartForNewOrder(AddToCartDto addToCartDto, UserEntity userEntity, ProductEntity productEntity) {

        OrderEntity newOrder = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .orderTotalAmount(productEntity.getPrice().multiply(BigDecimal.valueOf(addToCartDto.getQuantity())))
                .totalItemCount(addToCartDto.getQuantity())
                .userId(userEntity.getId())
                .status(OrderEntity.OrderStatus.ON_CART)
                .build();
        newOrder = orderRepository.saveAndFlush(newOrder);

        orderProductRepository.saveAndFlush(OrderProductEntity.builder()
                .productAmount(productEntity.getPrice())
                .productId(productEntity.getId())
                .productQuantity(addToCartDto.getQuantity())
                .orderId(newOrder.getId())
                .build());


        return getOrderDetails(newOrder);
    }

    private OrderDto addToCartForExistingOrder(AddToCartDto addToCartDto, UserEntity userEntity, ProductEntity productEntity) {

        OrderEntity orderEntity = orderRepository.findById(addToCartDto.getOrderId())
                .map(x -> {
                    //first validate ownership, then continue
                    validateOrderOwner(x, userEntity);

                    BigDecimal addedAmount = productEntity.getPrice().multiply(BigDecimal.valueOf(addToCartDto.getQuantity()));
                    x.setOrderTotalAmount(x.getOrderTotalAmount().add(addedAmount));
                    x.setTotalItemCount(x.getTotalItemCount() + addToCartDto.getQuantity());
                    return x;
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Shopping Cart not found with cart id : %s", addToCartDto.getOrderId())));


        OrderProductEntity orderProductEntity =
                orderProductRepository.findByOrderIdAndProductId(
                                addToCartDto.getOrderId(), addToCartDto.getProductId())
                        .map(x -> {
                            x.setProductQuantity(x.getProductQuantity() + addToCartDto.getQuantity());
                            return x;
                        })
                        .orElseGet(() ->
                                OrderProductEntity.builder()
                                        .lastUpdate(LocalDateTime.now())
                                        .productAmount(productEntity.getPrice())
                                        .productId(productEntity.getId())
                                        .productQuantity(addToCartDto.getQuantity())
                                        .orderId(addToCartDto.getOrderId())
                                        .build());

        orderProductRepository.saveAndFlush(orderProductEntity);
        orderEntity = orderRepository.saveAndFlush(orderEntity);
        return getOrderDetails(orderEntity);
    }

    private void validateOrderOwner(OrderEntity orderEntity, UserEntity userEntity) {

        if (!orderEntity.getUserId().equals(userEntity.getId())) {
            throw new ResourceViolationException(
                    "You can not make any action to the other customer's cart");
        }
    }

    private void validateOrderForCompleting(OrderEntity orderEntity) {

        if (orderEntity.getStatus().equals(OrderEntity.OrderStatus.ORDERED)) {
            throw new ResourceViolationException(
                    "You can only create order over active carts");
        }
    }

    private OrderEntity orderResourceGet(String orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Order Id : %s not found", orderId)));
    }

}
