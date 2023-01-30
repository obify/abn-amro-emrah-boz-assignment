package com.abnamro.emrahboz.assignment.retail.mock;

import com.abnamro.emrahboz.assignment.retail.data.model.OrderEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.OrderProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MockData {

    public UserEntity getCustomerRoleUserEntity() {

        return UserEntity.builder()
                .name("Bob Dylan")
                .email("mail@mail.com")
                .id(1L)
                .password("0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e")
                .userName("Bob")
                .phoneNumber("3168954712")
                .role("ROLE_CUSTOMER")
                .build();
    }


    public OrderEntity getOrderEntity(OrderEntity.OrderStatus status) {
        return OrderEntity.builder()
                .orderTotalAmount(BigDecimal.valueOf(14.5))
                .status(status)
                .totalItemCount(2)
                .lastUpdate(LocalDateTime.now())
                .userId(1L)
                .id(UUID.randomUUID().toString())
                .build();
    }


    public OrderProductEntity getOrderProductEntity() {
        return OrderProductEntity.builder()
                .productAmount(BigDecimal.valueOf(14.5))
                .orderId(UUID.randomUUID().toString())
                .productId(2L)
                .lastUpdate(LocalDateTime.now())
                .productQuantity(2)
                .id(1L)
                .build();
    }

    public ProductEntity getProductEntity() {
        return ProductEntity.builder()
                .price(BigDecimal.valueOf(14.5))
                .name(UUID.randomUUID().toString())
                .id(2L)
                .lastUpdate(LocalDateTime.now())
                .availableStock(2)
                .build();
    }

    public List<OrderProductEntity> getOrderProductEntityList() {

        List<OrderProductEntity> orderProductEntities = new ArrayList<>();
        orderProductEntities.add(getOrderProductEntity());
        orderProductEntities.add(getOrderProductEntity());
        return orderProductEntities;
    }
}
