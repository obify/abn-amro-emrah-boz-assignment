package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.OrderEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.OrderProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.OrderProductRepository;
import com.abnamro.emrahboz.assignment.retail.data.repository.OrderRepository;
import com.abnamro.emrahboz.assignment.retail.data.repository.ProductRepository;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceNotFoundException;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceViolationException;
import com.abnamro.emrahboz.assignment.retail.mapper.MapperUtil;
import com.abnamro.emrahboz.assignment.retail.mock.MockData;
import com.abnamro.emrahboz.assignment.retail.service.model.AddToCartDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderCompleteDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    private MockData mockData;

    ProductService mockProductService;
    NotificationService mockNotificationService;
    UserService mockuUserService;
    OrderRepository mockOrderRepository;
    OrderProductRepository mockOrderProductRepository;
    ProductRepository mockProductRepository;

    MapperUtil mapperUtil;
    @Spy
    private OrderService orderService;


    @BeforeAll
    public void setup() {

        mockData = new MockData();

        mockProductService = Mockito.mock(ProductService.class);
        mockNotificationService = Mockito.mock(NotificationService.class);
        mockuUserService = Mockito.mock(UserService.class);
        mockOrderRepository = Mockito.mock(OrderRepository.class);
        mockOrderProductRepository = Mockito.mock(OrderProductRepository.class);
        mockProductRepository = Mockito.mock(ProductRepository.class);

        mapperUtil = new MapperUtil(mockProductRepository);


        orderService = new OrderServiceImpl(mockProductService, mockNotificationService,
                mockuUserService, mockOrderRepository, mockOrderProductRepository, mapperUtil);


        Optional<OrderEntity> optionalOrder = Optional.of(mockData.getOrderEntity(OrderEntity.OrderStatus.ON_CART));
        Mockito.doReturn(optionalOrder)
                .when(mockOrderRepository).findById(any(String.class));

        Mockito.doReturn(mockData.getOrderEntity(OrderEntity.OrderStatus.ON_CART))
                .when(mockOrderRepository).saveAndFlush(any(OrderEntity.class));

        Mockito.doReturn(new ArrayList<OrderEntity>())
                .when(mockOrderRepository).findByLastUpdateLessThan(any(LocalDateTime.class));

        List<OrderProductEntity> orderProductList = mockData.getOrderProductEntityList();
        Mockito.doReturn(orderProductList)
                .when(mockOrderProductRepository).findAllByOrderId(any(String.class));
        Mockito.doReturn(Optional.of(mockData.getOrderProductEntity()))
                .when(mockOrderProductRepository).findByOrderIdAndProductId(any(String.class), any(Long.class));

        Optional<ProductEntity> productEntity = Optional.of(mockData.getProductEntity());
        Mockito.doReturn(productEntity)
                .when(mockProductRepository).findById(any(Long.class));
        Mockito.doReturn(productEntity.get())
                .when(mockProductService).update(any(Long.class), any(Integer.class));

        UserEntity userEntity = mockData.getCustomerRoleUserEntity();
        Mockito.doReturn(userEntity)
                .when(mockuUserService).findByUserName(any(String.class));


    }

    @Test
    public void givenExistingOrder_whenSelectThisOrder_thenSucceeded() {

        Mockito.doReturn(Optional.of(
                        mockData.getOrderEntity(OrderEntity.OrderStatus.ORDERED)))
                .when(mockOrderRepository).findById(any(String.class));

        Assertions.assertNotNull(orderService.get("order1"));
    }

    @Test
    public void givenNotExistingOrder_whenSelectThisOrder_thenThrowException() {

        Mockito.doReturn(Optional.empty())
                .when(mockOrderRepository).findById(any(String.class));

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            orderService.get("order1");
        });
        Assertions.assertEquals("Order Id : order1 not found", thrown.getMessage());

    }

    @Test
    public void givenNotCompletedOrder_whenSelectThisOrder_thenThrowException() {

        Mockito.doReturn(Optional.of(mockData.getOrderEntity(OrderEntity.OrderStatus.ON_CART)))
                .when(mockOrderRepository).findById(any(String.class));

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            orderService.get("order1");
        });
        Assertions.assertEquals("Order Id : order1 not found", thrown.getMessage());

    }

    @Test
    public void givenAlreadyCompletedOrder_whenCompleteThisOrder_thenThrowException() {

        Mockito.doReturn(Optional.of(mockData.getOrderEntity(OrderEntity.OrderStatus.ORDERED)))
                .when(mockOrderRepository).findById(any(String.class));
        OrderCompleteDto orderCompleteDto = OrderCompleteDto.builder()
                .orderId("order1")
                .userName("Bob")
                .build();

        ResourceViolationException thrown = Assertions.assertThrows(ResourceViolationException.class, () -> {
            orderService.completeOrder(orderCompleteDto);
        });
        Assertions.assertEquals("You can only create order over active carts", thrown.getMessage());

    }


    @Test
    public void givenExistingOrder_whenCompletingThisOrder_thenSucceeded() {

        Mockito.doReturn(Optional.of(mockData.getOrderEntity(OrderEntity.OrderStatus.ON_CART)))
                .when(mockOrderRepository).findById(any(String.class));
        OrderCompleteDto orderCompleteDto = OrderCompleteDto.builder()
                .orderId("order1")
                .userName("Bob")
                .build();

        Assertions.assertNotNull(orderService.completeOrder(orderCompleteDto));

    }

    @Test
    public void givenOtherUserExistingOrder_whenCompletingThisOrder_thenSucceeded() {

        Mockito.doReturn(Optional.of(mockData.getOrderEntity(OrderEntity.OrderStatus.ON_CART)))
                .when(mockOrderRepository).findById(any(String.class));
        OrderCompleteDto orderCompleteDto = OrderCompleteDto.builder()
                .orderId("order1")
                .userName("Neil")
                .build();

        UserEntity userEntity = mockData.getCustomerRoleUserEntity();
        userEntity.setId(2L);
        Mockito.doReturn(userEntity)
                .when(mockuUserService).findByUserName(any(String.class));

        ResourceViolationException thrown = Assertions.assertThrows(ResourceViolationException.class, () -> {
            orderService.completeOrder(orderCompleteDto);
        });
        Assertions.assertEquals("You can not make any action to the other customer's cart", thrown.getMessage());

    }


    @Test
    public void givenAddProductToNewCart_whenCreatingNewOrder_thenSucceeded() {

        AddToCartDto addToCartDto = AddToCartDto.builder()
                .productId(1L)
                .quantity(2)
                .userName("user1")
                .build();
        Assertions.assertNotNull(orderService.addToCard(addToCartDto));
    }

    @Test
    public void givenAddProductToExistingCart_whenUpdatingNewOrder_thenSucceeded() {

        Mockito.doReturn(mockData.getCustomerRoleUserEntity())
                .when(mockuUserService).findByUserName(any(String.class));

        AddToCartDto addToCartDto = AddToCartDto.builder()
                .productId(1L)
                .quantity(2)
                .userName("user1")
                .orderId("order1")
                .build();
        Assertions.assertNotNull(orderService.addToCard(addToCartDto));
    }


}
